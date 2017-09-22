/*
 * Given an ontology, an akpfile and an aoutput file, this class 
 * removes every akp that uses a concept that isn't defined 
 * inside the ontology then writes the result in the output file.
 */
package com.summarization.utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.HashSet;
import java.util.List;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;

import com.summarization.ontology.ConceptExtractor;
import com.summarization.ontology.Model;

public class DeleteteExtConceptFromSummary {

	HashSet<String> conceptsOntology = new HashSet<String>();
	HashSet<String> internalConceptsAKPs = new HashSet<String>();
	
	// Extracts the concepts defined in the ontology
	private void getConceptsFromOnt(String ontology_path){
		OntModel ontology = new Model(ontology_path, "RDF/XML").getOntologyModel();
		ConceptExtractor ce = new ConceptExtractor();
		ce.setConcepts(ontology, false);
		List<OntClass>  conceptsOntClass = ce.getExtractedConcepts();
		for(OntClass concept : conceptsOntClass)
			conceptsOntology.add(concept.toString()); 
	}
	
	private void cleanAKPs(String summary) throws Exception{
		BufferedReader br = new BufferedReader(new FileReader(summary));
		String line;
		if(summary.contains("datatype")){
			while ((line = br.readLine()) != null) {
				String[] splitted = line.split("##");
				String subj = splitted[0];
				
				if(conceptsOntology.contains(subj))
					internalConceptsAKPs.add(line);
			}
		}
		else if(summary.contains("object")){
			while ((line = br.readLine()) != null) {
				String[] splitted = line.split("##");
				String subj = splitted[0];
				String obj = splitted[2];
				
				if(conceptsOntology.contains(subj) && conceptsOntology.contains(obj))
					internalConceptsAKPs.add(line);
			}
		}
		br.close();
	}
	
	private void printOutput(String output_file) throws Exception{
		FileOutputStream fos = new FileOutputStream(new File(output_file));
		for(String AKP : internalConceptsAKPs)
			fos.write((AKP + "\n").getBytes());
		fos.close();
	}
	
	
	
	public static void main(String[] args) throws Exception{
		System.out.println("Input parameters: \n - Ontology file path \n - AKPs file path \n - output file");
		
		DeleteteExtConceptFromSummary cleaner = new DeleteteExtConceptFromSummary();
		cleaner.getConceptsFromOnt(args[0]);
		cleaner.cleanAKPs(args[1]);
		cleaner.printOutput(args[2]);
		
		System.out.println("Done");
	}
}
