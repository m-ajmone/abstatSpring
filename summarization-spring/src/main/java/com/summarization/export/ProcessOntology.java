package com.summarization.export;


import com.summarization.dataset.FileDataSupport;
import com.summarization.ontology.ConceptExtractor;
import com.summarization.ontology.Concepts;
import com.summarization.ontology.EqConceptExtractor;
import com.summarization.ontology.EquivalentConcepts;
import com.summarization.ontology.Model;
import com.summarization.ontology.OntologySubclassOfExtractor;
import com.summarization.ontology.Properties;
import com.summarization.ontology.PropertyExtractor;
import com.summarization.ontology.SubClassOf;

import com.hp.hpl.jena.ontology.OntModel;


public class ProcessOntology {

	public static void main(String[] args) throws Exception {
		Events.summarization();
		
	//	String owlBaseFileArg = null;
		String datasetSupportFileDirectory = null;
		
	//	owlBaseFileArg=args[0];
		String owlBaseFile = args[0];
		datasetSupportFileDirectory=args[1];

		/*
		File folder = new File(owlBaseFileArg);
		Collection<File> listOfFiles = FileUtils.listFiles(folder, new String[]{"owl"}, false);
		String fileName = listOfFiles.iterator().next().getName();
		
		String owlBaseFile = "file://" + owlBaseFileArg + "/" + fileName; 
		*/

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
		cExtract.setConcepts(ontologyModel, true);
		
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

}
