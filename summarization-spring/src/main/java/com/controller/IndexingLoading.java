package com.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.model.AKPSolr;
import com.model.IndexSummary;
import com.model.ResourceSolr;
import com.model.SubmitConfig;
import com.repository.AKPRepo;
import com.repository.ResourceRepo;
import com.service.AKPService;
import com.service.DatasetService;
import com.service.OntologyService;
import com.service.ResourceService;
import com.service.SubmitConfigService;
import com.summarization.dataset.FileSystemConnector;
import com.summarization.dataset.InputFile;
import com.summarization.dataset.TextInput;
import com.summarization.ontology.LDSummariesVocabulary;
import com.summarization.ontology.RDFResource;
import com.summarization.ontology.RDFTypeOf;
import com.summarization.ontology.TypeOf;

@Controller
public class IndexingLoading {

	@Autowired
	DatasetService datasetService;
	@Autowired
	OntologyService ontologyService;
	@Autowired
	ResourceService resourceService;
	@Autowired
	AKPService AKPService;
	@Autowired
	SubmitConfigService submitConfigService;
	@Autowired
	AKPRepo akpRepo;
	@Autowired
	ResourceRepo resRepo;
	
	String domain;
	String dataset;
	private HashMap<String, Long> datatypes;
	private HashMap<String, Long> concepts;
	private HashMap<String, Long> datatype_properties;
	private HashMap<String, Long> object_properties;
	static final int SolrBufferSize = 2000;
	
	@RequestMapping(value = "/indexingLoading", method = RequestMethod.POST)
	public String func(@ModelAttribute("indexingReq") IndexSummary dataloading, @RequestParam("domain") String domain) throws Exception {
		SubmitConfig config = submitConfigService.findSubmitConfigById(dataloading.getIdSummary());
		String patternsPath = config.getSummaryPath() + "/patterns/";
		dataset = datasetService.findDatasetById(config.getDsId()).getName();
		this.domain = domain;
		
		File countConcepts = new File(patternsPath + "/count-concepts.txt");
		File countDatatype = new File(patternsPath + "/count-datatype.txt");
		File countObjectProperties = new File(patternsPath + "/count-object-properties.txt");
		File countDatatypeProperties = new File(patternsPath + "/count-datatype-properties.txt");
		File objectAKP = new File(patternsPath + "/object-akp.txt");
		File datatypeAKP = new File(patternsPath + "/datatype-akp.txt");
		
		if(dataloading.getLoadOnMongoDB() & !config.isLoadedMongoDB()) {
			loadResources(new TextInput(new FileSystemConnector(countConcepts)), config, "Concept");
			loadResources(new TextInput(new FileSystemConnector(countDatatype)), config,  "Datatype");
			loadResources(new TextInput(new FileSystemConnector(countObjectProperties)), config, "Object Property");
			loadResources(new TextInput(new FileSystemConnector(countDatatypeProperties)), config, "Datatype Property");
			loadAKPs(new TextInput(new FileSystemConnector(objectAKP)), config, "Object AKP");
			loadAKPs(new TextInput(new FileSystemConnector(datatypeAKP)), config, "Datatype AKP");
		}
		
		if(dataloading.getIndexOnSolr() & !config.isIndexedSolr()) {
			datatypes = getFreqs(countDatatype);
			concepts = getFreqs(countConcepts);
			datatype_properties = getFreqs(countDatatypeProperties);
			object_properties = getFreqs(countObjectProperties);

			indexSummary(new TextInput(new FileSystemConnector(countConcepts)), "concept");
			indexSummary(new TextInput(new FileSystemConnector(countDatatype)),  "datatype");
			indexSummary(new TextInput(new FileSystemConnector(countObjectProperties)), "objectProperty");
			indexSummary(new TextInput(new FileSystemConnector(countDatatypeProperties)), "datatypeProperty");
			indexSummary(new TextInput(new FileSystemConnector(objectAKP)), "objectAkp");
			indexSummary(new TextInput(new FileSystemConnector(datatypeAKP)), "datatypeAkp");
			indexAKPsAutocomplete(new TextInput(new FileSystemConnector(objectAKP)), "objectAkp");
			indexAKPsAutocomplete(new TextInput(new FileSystemConnector(datatypeAKP)), "datatypeAkp");	
		}
		
		// update summary config 
		if(dataloading.getIndexOnSolr())
				config.setIndexedSolr(dataloading.getIndexOnSolr());
		if(dataloading.getLoadOnMongoDB())
			config.setLoadedMongoDB(dataloading.getLoadOnMongoDB());
		submitConfigService.update(config);
		
		
		return "redirect:home";
	}
	
	
	public void loadResources(InputFile file, SubmitConfig config, String type) throws Exception{
		while (file.hasNextLine()) {
			String line = file.nextLine();
			if(!line.equals("")){
				String[] splitted = line.split("##");
				String globalResource = splitted[0];
				String frequency = splitted[1];
				
				
				Model model = ModelFactory.createDefaultModel();
				LDSummariesVocabulary vocabulary = new LDSummariesVocabulary(model, dataset);
				RDFTypeOf typeOf = new RDFTypeOf(domain);
			
				com.model.Resource resource = new com.model.Resource();
				String localResource = "";
				switch(type) {
				case "Concept":
					localResource = vocabulary.addConcept(globalResource).getURI();
					break;
				case "Datatype":
					localResource = vocabulary.asLocalResource(globalResource).getURI();
					break;
				case "Object Property":
					localResource = vocabulary.asLocalObjectProperty(globalResource).getURI();
					break;
				case "Datatype Property":
					localResource = vocabulary.asLocalDatatypeProperty(globalResource).getURI();
					break;
				default:
					localResource = "NONE";
				}
				
				List<String> ontList = new ArrayList<String>();
				for(String id : config.getListOntId()) {
					String ontology = ontologyService.findOntologyById(id).getName();
					ontList.add(ontology);
				}
				
				resource.setLocalUrl(localResource);
				resource.setSeeAlso(globalResource);
				resource.setType(type);
				resource.setSubType(typeOf.resource(globalResource).getURI());
				resource.setDatasetOfOrigin(dataset);
				resource.setOntologiesOfOrigin(ontList);
				resource.setSummary_conf(config.getId());
				resource.setFrequency(Long.parseLong(frequency));
				resourceService.add(resource);	
			}
		}
	}
	
	
	public void loadAKPs(InputFile file,SubmitConfig config, String type) throws Exception{
		while (file.hasNextLine()) {
			String line = file.nextLine();
			if(!line.equals("")){
				String[] splitted = line.split("##");
				String globalSubject = splitted[0];
				String globalPredicate = splitted[1];
				String globalObject = splitted[2];
				String frequency = splitted[3];
				
				Model model = ModelFactory.createDefaultModel();
				LDSummariesVocabulary vocabulary = new LDSummariesVocabulary(model, dataset);
				RDFTypeOf typeOf = new RDFTypeOf(domain);
				
				String localSubject = vocabulary.asLocalResource(globalSubject).getURI();
				String localObject = vocabulary.asLocalResource(globalObject).getURI();
			
				com.model.AKP AKP = new com.model.AKP();
				String localPredicate = null;
				String internal = null;
				switch(type) {
				case "Object AKP":
					localPredicate = vocabulary.asLocalObjectProperty(globalPredicate).getURI();
					internal = typeOf.objectAKP(globalSubject, globalObject).getURI();
					break;
				case "Datatype AKP":
					localPredicate = vocabulary.asLocalDatatypeProperty(globalPredicate).getURI();
					internal = typeOf.datatypeAKP(globalSubject).getURI();
					break;
				default:
					AKP.setType("NONE");
				}
				
				String akpInstance = vocabulary.akpInstance(localSubject, localPredicate, localObject).getURI();
				
				List<String> ontList = new ArrayList<String>();
				for(String id : config.getListOntId()) {
					String ontology = ontologyService.findOntologyById(id).getName();
					ontList.add(ontology);
				}
				
				AKP.setURL(akpInstance);
				AKP.setPatternType("minimal");
				AKP.setType(type);
				AKP.setSubType(internal);
				AKP.setSubject(globalSubject);
				AKP.setPredicate(globalPredicate);
				AKP.setObject(globalObject);
				AKP.setFrequency(Long.parseLong(frequency));
				AKP.setDatasetOfOrigin(dataset);
				AKP.setOntologiesOfOrigin(ontList);
				AKP.setSummary_conf(config.getId());
				AKPService.add(AKP);
			}
		}
	}

	
	public void indexSummary(InputFile input, String type) throws Exception{
		ArrayList<ResourceSolr> buffer = new ArrayList<ResourceSolr>();
		while(input.hasNextLine()){
			String[] line = input.nextLine().split("##");
			ResourceSolr res = new ResourceSolr();
			res.setType(type);
			res.setDataset(dataset);
			if(type.contains("Akp")){
				String subject = line[0];
				String subjectLocalName = new RDFResource(subject).localName();
				String property = line[1];
				String propertyLocalName = new RDFResource(property).localName();
				String object = line[2];
				String objectLocalName = new RDFResource(object).localName();
				Long occurrences = Long.parseLong(line[3]);
				String subtype = typeOf(subject, object, type);
				
				res.setURI(new String[]{subject, property, object});
				res.setSubtype(subtype);
				res.setFullTextSearchField(new String[]{subjectLocalName, propertyLocalName, objectLocalName});
				res.setOccurrence(occurrences);
			}
			else {
				String resource = line[0];
				String localName = new RDFResource(resource).localName();
				Long occurrences = Long.parseLong(line[1]);
				String subtype = new TypeOf(domain).resource(resource);
				
				res.setURI(new String[]{resource});
				res.setSubtype(subtype);
				res.setFullTextSearchField(new String[]{localName});
				res.setOccurrence(occurrences);
			}
			if(buffer.size() >= SolrBufferSize) {
				resRepo.save(buffer);
				buffer.clear();
			}
			buffer.add(res);
		}
		resRepo.save(buffer);
	}
	
	
	public void indexAKPsAutocomplete(InputFile input, String type) throws Exception{
		ArrayList<AKPSolr> buffer = new ArrayList<AKPSolr>();
		while(input.hasNextLine()){
			String[] line = input.nextLine().split("##");
			String subject = line[0];
			String subjectLocalName = new RDFResource(subject).localName();
			String property = line[1];
			String propertyLocalName = new RDFResource(property).localName();
			String object = line[2];
			String objectLocalName = new RDFResource(object).localName();
			Long occurrences = Long.parseLong(line[3]);
			String subtype = typeOf(subject, object, type);
			
			AKPSolr akp = new AKPSolr();
			akp.setURI(new String[]{
					subject, property, object
			});
			akp.setType(type);
			akp.setDataset(dataset);
			akp.setSubtype(subtype);
			akp.setFullTextSearchField(new String[]{
					subjectLocalName, propertyLocalName, objectLocalName
			});
			akp.setSubject(subject);
			akp.setPredicate(property);
			akp.setObject(object);
			akp.setSubject_ngram(subjectLocalName);
			akp.setPredicate_ngram(propertyLocalName);
			akp.setObject_ngram(objectLocalName);
			akp.setOCcurrence(occurrences);
			
			if(concepts.containsKey(subject))                     // questo if è necessario perchè i concetti esterni non sono elecnati in count-concepts.txt e quidni non sono nella collection concepts
				akp.setSubjectFreq(concepts.get(subject));
			else
				akp.setSubjectFreq(0);
			
			if(type.equals("datatypeAkp")){
				akp.setPredicateFreq(datatype_properties.get(property));
				akp.setObjectFreq(datatypes.get(object));
			}
			else{
				akp.setPredicateFreq(object_properties.get(property));
				if(concepts.containsKey(object))                     // questo if è necessario perchè i concetti esterni non sono elecnati in count-concepts.txt e quidni non sono nella collection concepts
					akp.setObjectFreq(concepts.get(object));
				else
					akp.setObjectFreq(0);
			}
			
			akp.setSubject_plus_dataset(subjectLocalName + "_" + dataset);
			akp.setPredicate_plus_dataset(propertyLocalName + "_" + dataset);
			akp.setObject_plus_dataset(objectLocalName + "_" + dataset);
			
			if(buffer.size() >= SolrBufferSize) {
				akpRepo.save(buffer);
				buffer.clear();
			}
			buffer.add(akp);
		}
		akpRepo.save(buffer);
	}
	
	
	
	// Extracts frequencies from the input file
	public HashMap<String, Long> getFreqs(File file){
		HashMap<String, Long> map = new HashMap<String, Long>();
		try{
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;
			while ((line = br.readLine()) != null) {
				if(!line.equals("")){
					String[] splitted = line.split("##");
					String key = splitted[0];
					Long value = Long.parseLong(splitted[1]);
					map.put(key, value);
				}
			}
			br.close();
		} catch(Exception e){
		}
		return map;
	}
	
	
	private String typeOf(String subject, String object, String type) {
		TypeOf typeOf = new TypeOf(domain);
		if(type.equals("datatypeAkp")) return typeOf.datatypeAKP(subject);
		return typeOf.objectAKP(subject, object);
	}
	
}
