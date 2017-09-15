package com.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardCopyOption.*;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.code.externalsorting.ExternalSort;
import com.hp.hpl.jena.ontology.OntModel;
import com.model.Dataset;
import com.model.SubmitConfig;
import com.service.DatasetService;
import com.service.OntologyService;
import com.service.SubmitConfigService;
import com.summarization.dataset.MinimalTypesCalculation;
import com.summarization.ontology.DatatypeSplittedPatternInference;
import com.summarization.ontology.Model;
import com.summarization.ontology.ObjectSplittedPatternInference;
import com.summarization.dataset.AggregateConceptCounts;
import com.summarization.dataset.CalculatePropertyMinimalization;
import com.summarization.dataset.Events;
import com.summarization.dataset.MainCardinality;
import com.summarization.dataset.ProcessDatatypeRelationAssertions;
import com.summarization.dataset.ProcessObjectRelationAssertions;
import com.summarization.dataset.ParallelProcessing;

@Controller
public class ProcessTriplesController {
		
		@Autowired
		SubmitConfigService submitConfigService;
		@Autowired
		OntologyService ontologyService;
		@Autowired
		DatasetService datasetService;
		
		@RequestMapping(value = "processTriples", method = RequestMethod.POST)
		public String processTriples(@RequestParam("subCfgId") String subCfgId) throws Exception {
			
			Events.summarization();
			String ontPath = "";
			String ontName = "";
			String dsId = submitConfigService.findSubmitConfigById(subCfgId).getDsId();
			Dataset dataset = datasetService.findDatasetById(dsId);
			String datasetName = dataset.getName();
			String inf = ""; String minTp = ""; String propMin = ""; String card = "";
			SubmitConfig subCfg = submitConfigService.findSubmitConfigById(subCfgId);
			if(subCfg.isTipoMinimo())
				minTp = "MinTp";
			if(subCfg.isCardinalita())
				card = "Card";
			if(subCfg.isInferences())
				inf = "Inf";
			if(subCfg.isPropertyMinimaliz())
				propMin = "PropMin";
			if(!subCfg.isTipoMinimo() || subCfg.getListOntId().isEmpty()) {
				ontPath = "data/DsAndOnt/ontology/emptyOnt.owl";
				ontName = "emptyOnt";
			}
			else {
				String ontId = subCfg.getListOntId().get(0);
				ontPath = ontologyService.findOntologyById(ontId).getPath();
				ontName = ontologyService.findOntologyById(ontId).getName();
			}
			
			// script bash/awk invocation
			String[] cmd = {"../pipeline/split-dataset.sh", datasetName};
			Process p = Runtime.getRuntime().exec(cmd);
	        
		    BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
		    String line = "";
		    while ((line = reader.readLine()) != null) {
		        System.out.println(line);
		    }
	        
			
			
			String summaryPath = "data/summaries/" + datasetName + "_" + ontName + "_" + minTp + propMin + card + inf +"/";
			File summary = new File(summaryPath);
			if(summary.exists())
				FileUtils.deleteDirectory(summary);
			submitConfigService.findSubmitConfigById(subCfgId).setSummaryPath(summaryPath);
			String minTypeResultPath = summaryPath + "min-types/min-type-results/";
			String patternsPath = summaryPath + "patterns/";
			//creazione cartelle output CalculateMinTypes
			File file1 = new File(minTypeResultPath);
	        if (!file1.exists()) {
	            if (file1.mkdirs()) {
	                System.out.println("Directory is created!");
	            } else {
	                System.out.println("Failed to create directory!");
	            }
	        }
			File sourceDirectory = new File("data/DsAndOnt/dataset/" + dataset.getName() + "/organized-splitted-deduplicated/");
			File minTypeResult = new File(minTypeResultPath);
			
			OntModel ontologyModel = new Model(ontPath,"RDF/XML").getOntologyModel();
			
			//CalculateMinimalTypes
			MinimalTypesCalculation minimalTypes = new MinimalTypesCalculation(ontologyModel, minTypeResult);
			
			new ParallelProcessing(sourceDirectory, "_types.nt").process(minimalTypes);
			
			//AggregateConceptCounts
			AggregateConceptCounts.aggregateConceptCounts(minTypeResultPath, patternsPath);
			
			//ProcessDatatypeRelationAssertions
			ProcessDatatypeRelationAssertions.processDatatypeRelationAssertions(sourceDirectory, minTypeResult, patternsPath);
			
			//ProcessObjectRelationAssertions
			ProcessObjectRelationAssertions.processObjectRelationAssertions(sourceDirectory, minTypeResult, patternsPath);
			
			//sposto i file akp_grezzo
			Path objectAkpGrezzo_target = Paths.get(patternsPath+"object-akp_grezzo.txt");
			Path datatypeAkpGrezzo_target = Paths.get(patternsPath+"datatype-akp_grezzo.txt");
			Path datatypeAkpGrezzo_src = Paths.get("datatype-akp_grezzo.txt");
			Path objectAkpGrezzo_src = Paths.get("object-akp_grezzo.txt");
			Files.move(datatypeAkpGrezzo_src, datatypeAkpGrezzo_target, REPLACE_EXISTING);
			Files.move(objectAkpGrezzo_src, objectAkpGrezzo_target,REPLACE_EXISTING);
			
			
			//Property minimalization
			if(subCfg.isPropertyMinimaliz()) {
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
			//Inferenze
			if(subCfg.isInferences()) {
				DatatypeSplittedPatternInference.datatypeSplittedPatternInference(patternsPath, patternsPath + "AKPs_Grezzo-parts", ontPath, patternsPath+"specialParts_outputs");
				ObjectSplittedPatternInference.objectSplittedPatternInference(patternsPath, patternsPath + "AKPs_Grezzo-parts", ontPath, patternsPath+"specialParts_outputs");
			}
			//Cardinalità
			if(subCfg.isCardinalita()) {
				MainCardinality.mainCardinality(patternsPath);
			}
			
			
			return "redirect:home";
		}
}
