package com.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.model.IndexSummary;
import com.model.SubmitConfig;
import com.service.DatasetService;
import com.service.OntologyService;
import com.service.ResourceService;
import com.service.AKPService;
import com.service.SubmitConfigService;
import com.summarization.dataset.FileSystemConnector;
import com.summarization.dataset.InputFile;
import com.summarization.dataset.TextInput;
import com.summarization.ontology.LDSummariesVocabulary;
import com.summarization.ontology.RDFTypeOf;

@Controller
public class LoadSummaryOnMongo {

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
	
	String dataset;
	String domain;

	@RequestMapping(value = "/index", method = RequestMethod.POST)
	public String func(@ModelAttribute("indexingReq") IndexSummary indexingReq, @RequestParam("domain") String domain) throws Exception {
		SubmitConfig config = submitConfigService.findSubmitConfigById(indexingReq.getIdSummary());
		String summaryPath = config.getSummaryPath();
		dataset = datasetService.findDatasetById(config.getDsId()).getName();
		this.domain = domain;
		
		File countConcepts_file = new File(summaryPath + "/patterns/count-concepts.txt");
		File countDatatype_file = new File(summaryPath + "/patterns/count-datatype.txt");
		File countObjectProperties_file = new File(summaryPath + "patterns/count-object-properties.txt");
		File countDatatypeProperties_file = new File(summaryPath + "patterns/count-datatype-properties.txt");
		File objectAKP_file = new File(summaryPath + "patterns/object-akp.txt");
		File datatypeAKP_file = new File(summaryPath + "patterns/datatype-akp.txt");
		
		indexResourceFile(new TextInput(new FileSystemConnector(countConcepts_file)), config, "Concept");
		indexResourceFile(new TextInput(new FileSystemConnector(countDatatype_file)), config,  "Datatype");
		indexResourceFile(new TextInput(new FileSystemConnector(countObjectProperties_file)), config, "Object Property");
		indexResourceFile(new TextInput(new FileSystemConnector(countDatatypeProperties_file)), config, "Datatype Property");
		indexAKPFile(new TextInput(new FileSystemConnector(objectAKP_file)), config, "Object AKP");
		indexAKPFile(new TextInput(new FileSystemConnector(datatypeAKP_file)), config, "Datatype AKP");
		
		return "redirect:home";
	}

	
	public void indexResourceFile(InputFile file, SubmitConfig config, String type) throws Exception{
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
	
	
	public void indexAKPFile(InputFile file,SubmitConfig config, String type) throws Exception{
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

}
