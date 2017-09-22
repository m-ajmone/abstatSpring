/*
 * Given the typegraph of an ontology and the set of types declared in the dataset,
 * this class removes every leaf from ontology that doesn't occur in the datasetConcepts set. 
 * This process is repetead until every leaf occurs in datasetConcepts set.
 */

package com.summarization.utilities;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;

import com.summarization.ontology.ConceptExtractor;
import com.summarization.ontology.Model;

public class UnusedConceptsRemover {
	HashSet<OntClass> ontLeafConcepts = new HashSet<OntClass>();
	HashSet<OntClass> ontNotLeafConcepts = new HashSet<OntClass>();
	HashSet<String> datasetConcepts = new HashSet<String>();
	OntModel ontology;

	
	public UnusedConceptsRemover(String ontology_path, String dataset) throws Exception{
		ontology = new Model(ontology_path, "RDF/XML").getOntologyModel();
		ontology.setStrictMode(false);
		extractDatasetConcepts(dataset);
	}
	
	private void updateOntologyLeafs(){
		ontLeafConcepts.clear();
		ontNotLeafConcepts.clear();
		
		ConceptExtractor ce = new ConceptExtractor();
		ce.setConcepts(ontology, true);               //voglio fare l'enrichment perchè voglio anche i concetti non definiti nell'ont
		List<OntClass>  conceptsOntClass = ce.getExtractedConcepts();
		for(OntClass concept : conceptsOntClass){
			if(concept.hasSubClass())
				ontNotLeafConcepts.add(concept);
			else
				ontLeafConcepts.add(concept);
		}
	}
	
	
	private void extractDatasetConcepts(String dataset) throws Exception{
		BufferedReader br = new BufferedReader(new FileReader(dataset));
		String line;

		while ((line = br.readLine()) != null) {
			if(line.contains("<http://www.w3.org/1999/02/22-rdf-syntax-ns#type>")){
				String[] splitted = line.split("> <");
				String concept = splitted[2].substring(0, splitted[2].length()-3);
				datasetConcepts.add(concept);
			}
		}
		br.close();
	}
	
	
	public void clean(){
		boolean clean = false;
		while(!clean){
			updateOntologyLeafs();
			clean = true;
			Iterator<OntClass> it = ontLeafConcepts.iterator();
			while(it.hasNext()){
				OntClass leaf = it.next();
				if(!datasetConcepts.contains(leaf.getURI())){    //se leaf non è usato come tipo
					clean = false;
					leaf.remove();  // remove from ontology
				}
			}
		}
		updateOntologyLeafs();
	}
	
	
	public OntModel getOntology(){
		return ontology;
	}

	
	//-------------------------------- UTILS-----------------------------------------------
	
	private void printer(Collection<?> col){
		System.out.println("");
		for(Object obj: col){
			System.out.println(obj);
		}
	}

	private HashSet<OntClass> classiOntModel(){
		HashSet<OntClass> set = new HashSet<OntClass>();
		ConceptExtractor ce = new ConceptExtractor();
		ce.setConcepts(ontology, true);                 //non voglio fare l'enrichment perchè desidero solo quelli definiti nell'ontologia
		List<OntClass>  conceptsOntClass = ce.getExtractedConcepts();
		for(OntClass concept : conceptsOntClass)
			set.add(concept);
		
		return set;
	}
	
	//--------------------------------------------------------------------------------------
	
	
	public static void main(String[] args) throws Exception{
		String ontology_file = args[0];
		String dataset_file = args[1];
		
		UnusedConceptsRemover remover = new UnusedConceptsRemover(ontology_file, dataset_file);
		remover.clean();
//		remover.printer(remover.ontLeafConcepts);
//		remover.printer(remover.ontNotLeafConcepts);
		remover.printer(remover.classiOntModel());
		System.out.println("");
		
		new ClassHierarchy().showHierarchy( System.out, remover.ontology );
	}
}

