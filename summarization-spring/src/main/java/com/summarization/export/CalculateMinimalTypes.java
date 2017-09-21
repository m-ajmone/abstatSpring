package com.summarization.export;

import com.summarization.dataset.MinimalTypesCalculation;
import com.summarization.dataset.ParallelProcessing;
import com.summarization.ontology.Model;

import java.io.File;

import com.hp.hpl.jena.ontology.OntModel;

public class CalculateMinimalTypes {

	public static void main(String[] args) throws Exception {
		
		Events.summarization();
		/*
		File folder = new File(args[0]);
		Collection<File> listOfFiles = FileUtils.listFiles(folder, new String[]{"owl"}, false);
		File ontology = listOfFiles.iterator().next();
		*/
		String owlBaseFile = args[0];
		File typesDirectory = new File(args[1]);
		File targetDirectory = new File(args[2]);
		
		//OntModel ontologyModel = new Model(ontology.getAbsolutePath(),"RDF/XML").getOntologyModel();
		OntModel ontologyModel = new Model(owlBaseFile,"RDF/XML").getOntologyModel();
		
		MinimalTypesCalculation minimalTypes = new MinimalTypesCalculation(ontologyModel, targetDirectory);
		
		new ParallelProcessing(typesDirectory, "_types.nt").process(minimalTypes);
	}
}
