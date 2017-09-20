package com.controller;



import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.export.AggregateConceptCounts;
import com.export.CalculatePropertyMinimalization;
import com.export.DatatypeSplittedPatternInference;
import com.export.Events;
import com.export.MainCardinality;
import com.export.ObjectSplittedPatternInference;
import com.export.ProcessDatatypeRelationAssertions;
import com.export.ProcessObjectRelationAssertions;
import com.google.code.externalsorting.ExternalSort;
import com.hp.hpl.jena.ontology.OntModel;
import com.model.Dataset;
import com.model.SubmitConfig;
import com.service.DatasetService;
import com.service.OntologyService;
import com.service.SubmitConfigService;
import com.summarization.dataset.FileDataSupport;
import com.summarization.dataset.MinimalTypesCalculation;
import com.summarization.dataset.ParallelProcessing;
import com.summarization.ontology.ConceptExtractor;
import com.summarization.ontology.Concepts;
import com.summarization.ontology.EqConceptExtractor;
import com.summarization.ontology.EquivalentConcepts;
import com.summarization.ontology.Model;
import com.summarization.ontology.OntologySubclassOfExtractor;
import com.summarization.ontology.Properties;
import com.summarization.ontology.PropertyExtractor;
import com.summarization.ontology.SubClassOf;


@Controller
public class SummarizationController {
	
	@Autowired
	SubmitConfigService submitConfigService;
	@Autowired
	OntologyService ontologyService;
	@Autowired
	DatasetService datasetService;
	
	
	
	@RequestMapping(value = "summarization" , method = RequestMethod.POST)
	public String processOntology2(@RequestParam("subCfgId") String subCfgId)  throws Exception  {
		Events.summarization();
		SubmitConfig subCfg = submitConfigService.findSubmitConfigById(subCfgId);
		String ontId = subCfg.getListOntId().get(0);
		
		String ontPath = "";
		String ontName = "";
		String dsId = submitConfigService.findSubmitConfigById(subCfgId).getDsId();
		Dataset dataset = datasetService.findDatasetById(dsId);
		String datasetName = dataset.getName();
		String inf = ""; String minTp = ""; String propMin = ""; String card = "";

		if(subCfg.isTipoMinimo())
			minTp = "MinTp";
		if(subCfg.isCardinalita())
			card = "Card";
		if(subCfg.isInferences())
			inf = "Inf";
		if(subCfg.isPropertyMinimaliz())
			propMin = "PropMin";
		if(!subCfg.isTipoMinimo() || subCfg.getListOntId().isEmpty()) {
			ontPath = new File(System.getProperty("user.dir")).getParentFile().getAbsolutePath() + "/data/DsAndOnt/ontology/emptyOnt.owl";
			ontName = "emptyOnt";
		}
		else {
			ontPath = new File(ontologyService.findOntologyById(ontId).getPath()).getCanonicalPath();
			ontName = ontologyService.findOntologyById(ontId).getName();
		}
		

		File  summary_dir = new File(new File(System.getProperty("user.dir")).getParentFile().getAbsolutePath() + "/data/summaries/" + datasetName + "_" + ontName + "_" + minTp + propMin + card + inf +"/");
		String datasetSupportFileDirectory = summary_dir + "/reports/tmp-data-for-computation/";
		
 		if(summary_dir.exists())
 			FileUtils.deleteDirectory(summary_dir);
		File file = new File(datasetSupportFileDirectory);
        if (!file.exists()) {
            if (file.mkdirs()) 
                System.out.println(file.getPath() + "is created!");
            else 
                System.out.println("Failed to create directory!");
        }
        String owlBaseFile = ontologyService.findOntologyById(ontId).getPath();
        processOntology(owlBaseFile, datasetSupportFileDirectory);
			

    
        // script bash/awk invocation
 		String[] cmd = {"../pipeline/split-dataset.sh", datasetName};
 		Process p = Runtime.getRuntime().exec(cmd);
         
 	    BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
 	    String line = "";
 	    while ((line = reader.readLine()) != null)
 	        System.out.println(line);

        String summary_dir_path = summary_dir.getPath();

 		if(summary_dir.exists())
 			FileUtils.deleteDirectory(summary_dir);
 		submitConfigService.findSubmitConfigById(subCfgId).setSummaryPath(summary_dir_path);
 		String minTypeResultPath = summary_dir_path + "/min-types/min-type-results/";
 		String patternsPath = summary_dir_path + "/patterns/";
 		//creazione cartelle output CalculateMinTypes
 		File file2 = new File(minTypeResultPath);
         if (!file2.exists()) {
             if (file2.mkdirs()) 
                 System.out.println( file2.getPath() + "Directory is created!");
             else 
                 System.out.println("Failed to create directory!");
         }
 		
         
        File typesDirectory = new File("../data/DsAndOnt/dataset/" + dataset.getName() + "/organized-splitted-deduplicated/");
 		File minTypeResult = new File(minTypeResultPath);
 		
 		//Minimal types
 		calculateMinimalTypes(owlBaseFile, minTypeResult, typesDirectory);
 		//Aggregate concepts
 		aggregateConceptCounts(minTypeResultPath, patternsPath);
 		//Process dt relatioanl asserts
 		processDatatypeRelationAssertions(typesDirectory, minTypeResult, patternsPath);
 		//Process obj relatioanl asserts
 		processObjectRelationAssertions(typesDirectory, minTypeResult, patternsPath);
 
 		
 		
 		//sposto i file akp_grezzo
 		Path objectAkpGrezzo_target = Paths.get(patternsPath+"object-akp_grezzo.txt");
 		Path datatypeAkpGrezzo_target = Paths.get(patternsPath+"datatype-akp_grezzo.txt");
 		Path datatypeAkpGrezzo_src = Paths.get("datatype-akp_grezzo.txt");
 		Path objectAkpGrezzo_src = Paths.get("object-akp_grezzo.txt");
 		Files.move(datatypeAkpGrezzo_src, datatypeAkpGrezzo_target, REPLACE_EXISTING);
 		Files.move(objectAkpGrezzo_src, objectAkpGrezzo_target,REPLACE_EXISTING);
 		
 		
 		//Property minimalization
 		if(subCfg.isPropertyMinimaliz()) 
 			propertyMinimalization(objectAkpGrezzo_target, datatypeAkpGrezzo_target, patternsPath, ontPath);
 		//Inferenze
 		if(subCfg.isInferences()) 
 			inference(patternsPath, ontPath);
 		//Cardinalità
 		if(subCfg.isCardinalita()) 
 			cardinality(patternsPath);
    
 		return "redirect:home";
	}	
	
	
	private void processOntology(String owlBaseFile, String datasetSupportFileDirectory){
		//Model
		OntModel ontologyModel = new Model(owlBaseFile,"RDF/XML").getOntologyModel();
		
		//Extract Property from Ontology Model
		PropertyExtractor pExtract = new PropertyExtractor();
		pExtract.setProperty(ontologyModel);
		
		Properties properties = new Properties();
		properties.setProperty(pExtract.getProperty());
		properties.setExtractedProperty(pExtract.getExtractedProperty());
		properties.setCounter(pExtract.getCounter());
		
		//Extract Concept from Ontology Model
		ConceptExtractor cExtract = new ConceptExtractor();
		cExtract.setConcepts(ontologyModel);
		
		Concepts concepts = new Concepts();
		concepts.setConcepts(cExtract.getConcepts());
		concepts.setExtractedConcepts(cExtract.getExtractedConcepts());
		concepts.setObtainedBy(cExtract.getObtainedBy());
		
		//Extract SubClassOf Relation from OntologyModel
		OntologySubclassOfExtractor SbExtractor = new OntologySubclassOfExtractor();
		//The Set of Concepts will be Updated if Superclasses are not in It
		SbExtractor.setConceptsSubclassOf(concepts, ontologyModel);
		SubClassOf SubClassOfRelation = SbExtractor.getConceptsSubclassOf();
		
		//Extract EquivalentClass from Ontology Model - Qui per considerare tutti i concetti
		EqConceptExtractor equConcepts = new EqConceptExtractor();
		equConcepts.setEquConcept(concepts, ontologyModel);
		
		EquivalentConcepts equConcept = new EquivalentConcepts();
		equConcept.setExtractedEquConcept(equConcepts.getExtractedEquConcept());
		equConcept.setEquConcept(equConcepts.getEquConcept());
		
		concepts.deleteThing();
		SubClassOfRelation.deleteThing();
		
        FileDataSupport writeFileSupp = new FileDataSupport(SubClassOfRelation, datasetSupportFileDirectory + "SubclassOf.txt", datasetSupportFileDirectory + "Concepts.txt");
        
        writeFileSupp.writeSubclass(equConcept);
        writeFileSupp.writeConcept(concepts);
	}
	
	private void calculateMinimalTypes(String owlBaseFile, File targetDirectory, File typesDirectory) throws Exception{
		OntModel ontologyModel = new Model(owlBaseFile,"RDF/XML").getOntologyModel();
 		MinimalTypesCalculation minimalTypes = new MinimalTypesCalculation(ontologyModel, targetDirectory);
 		new ParallelProcessing(typesDirectory, "_types.nt").process(minimalTypes);
	}
	
	
	private void aggregateConceptCounts(String minTypeResultPath, String patternsPath) throws Exception{
 		AggregateConceptCounts.aggregateConceptCounts(minTypeResultPath, patternsPath);
	}
	

	private void processDatatypeRelationAssertions(File typesDirectory, File minTypeResult, String patternsPath) throws Exception{
		ProcessDatatypeRelationAssertions.processDatatypeRelationAssertions(typesDirectory, minTypeResult, patternsPath);
	}
	
	private void processObjectRelationAssertions(File typesDirectory, File minTypeResult, String patternsPath) throws Exception{
		ProcessObjectRelationAssertions.processObjectRelationAssertions(typesDirectory, minTypeResult, patternsPath);
	}

	
	private void propertyMinimalization(Path objectAkpGrezzo_target, Path datatypeAkpGrezzo_target, String patternsPath, String ontPath) throws Exception{
	//ordino i file akp_grezzo
		ExternalSort.mergeSortedFiles(ExternalSort.sortInBatch(new File(objectAkpGrezzo_target.toString())), new File(patternsPath+"object-akp-grezzo_Sorted.txt"));
		ExternalSort.mergeSortedFiles(ExternalSort.sortInBatch(new File(datatypeAkpGrezzo_target.toString())), new File(patternsPath+"datatype-akp-grezzo_Sorted.txt"));
		//rinomino i file ordinati
		Path datatypeAkpGrezzo_sorted = Paths.get(patternsPath+"datatype-akp-grezzo_Sorted.txt");
		Path objectAkpGrezzo_sorted = Paths.get(patternsPath + "object-akp-grezzo_Sorted.txt");
		Files.move(datatypeAkpGrezzo_sorted, datatypeAkpGrezzo_sorted.resolveSibling("datatype-akp_grezzo.txt"), REPLACE_EXISTING);
		Files.move(objectAkpGrezzo_sorted, objectAkpGrezzo_sorted.resolveSibling("object-akp_grezzo.txt"), REPLACE_EXISTING);
		CalculatePropertyMinimalization.calculatePropertyMinimalization(ontPath, patternsPath);
		//sposto i file di output
		Path datatypeAkpGrezzo_Updated = Paths.get(patternsPath + "datatype-akp_grezzo_Updated.txt");
		Path objectAkpGrezzo_Updated = Paths.get(patternsPath + "object-akp_grezzo_Updated.txt");
		Files.move(datatypeAkpGrezzo_Updated, datatypeAkpGrezzo_target, REPLACE_EXISTING);
		Files.move(objectAkpGrezzo_Updated, objectAkpGrezzo_target, REPLACE_EXISTING);
		Path datatypeAkp = Paths.get(patternsPath + "datatype-akp.txt");
		Path objectAkp = Paths.get(patternsPath + "object-akp.txt");
		Path datatypeAkp_Updated = Paths.get(patternsPath + "datatype-akp_Updated.txt");
		Path objectAkp_Updated = Paths.get(patternsPath + "object-akp_Updated.txt");
		Files.move(datatypeAkp_Updated, datatypeAkp, REPLACE_EXISTING);
		Files.move(objectAkp_Updated, objectAkp, REPLACE_EXISTING);
		Path countDatatypeProperties_Updated = Paths.get(patternsPath + "count-datatype-properties_Updated.txt");
		Path countObjectProperties_Updated = Paths.get(patternsPath + "count-object-properties_Updated.txt");
		Path countDatatypeProperties = Paths.get(patternsPath + "count-datatype-properties.txt");
		Path countObjectProperties = Paths.get(patternsPath + "count-object-properties.txt");
		Files.move(countDatatypeProperties_Updated, countDatatypeProperties, REPLACE_EXISTING);
		Files.move(countObjectProperties_Updated, countObjectProperties, REPLACE_EXISTING);
	
	}
	
	private void inference(String patternsPath, String ontPath) throws Exception{
		DatatypeSplittedPatternInference.datatypeSplittedPatternInference(patternsPath, patternsPath + "AKPs_Grezzo-parts", ontPath, patternsPath+"specialParts_outputs");
			ObjectSplittedPatternInference.objectSplittedPatternInference(patternsPath, patternsPath + "AKPs_Grezzo-parts", ontPath, patternsPath+"specialParts_outputs");
	}
	
	private void cardinality(String patternsPath) throws Exception{
		MainCardinality.mainCardinality(patternsPath);
	}
}
