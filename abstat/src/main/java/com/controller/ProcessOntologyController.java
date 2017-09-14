package com.controller;



import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.hp.hpl.jena.ontology.OntModel;
import com.service.OntologyService;
import com.service.SubmitConfigService;
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

import com.summarization.dataset.Events;


@Controller
public class ProcessOntologyController {
	
	@Autowired
	SubmitConfigService submitConfigService;
	@Autowired
	OntologyService ontologyService;
	
	@RequestMapping(value = "processOntology" , method = RequestMethod.POST)
	public String processOntology(@RequestParam("subCfgId") String subCfgId) {
		
		Events.summarization();
		
		String ontId = submitConfigService.findSubmitConfigById(subCfgId).getListOntId().get(0);
		
		String datasetSupportFileDirectory="data/summaries/" + ontologyService.findOntologyById(ontId).getName() + "/reports/tmp-data-for-computation/";
		
		File file1 = new File(datasetSupportFileDirectory);
        if (!file1.exists()) {
            if (file1.mkdirs()) {
                System.out.println("Directory is created!");
            } else {
                System.out.println("Failed to create directory!");
            }
        }
			
		String owlBaseFile = ontologyService.findOntologyById(ontId).getPath();
				
		
		System.out.println(owlBaseFile);
		
		

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
        
		return "redirect:home";
	}
}
