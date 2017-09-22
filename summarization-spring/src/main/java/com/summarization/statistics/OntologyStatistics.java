package com.summarization.statistics;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.hp.hpl.jena.vocabulary.OWL;

import com.summarization.experiments.Concept;
import com.summarization.ontology.ConceptExtractor;
import com.summarization.ontology.Concepts;
import com.summarization.ontology.EqConceptExtractor;
import com.summarization.ontology.Model;
import com.summarization.ontology.OntologySubclassOfExtractor;
import com.summarization.ontology.SubClassOf;
import com.summarization.ontology.TypeGraphExperimental;

public class OntologyStatistics {
	HashSet<OntClass> concepts = new HashSet<OntClass>();
	HashSet<OntProperty> objectProperties = new HashSet<OntProperty>();
	HashSet<OntProperty> datatypeProperties = new HashSet<OntProperty>();
	ArrayList<List<OntResource>> equivalentClass = new ArrayList<List<OntResource>>();
	ArrayList<List<OntClass>> subClassOf = new ArrayList<List<OntClass>>();
	ArrayList<List<OntResource>> disjointClasses = new ArrayList<List<OntResource>>();
	HashSet<Concept> typeGraphRoots;
	int minDepth;
	int maxDepth;
	
	
	// Extracts the concepts defined in the ontology
	private void extractConcepts(String ontology_path){
		OntModel ontology = new Model(ontology_path, "RDF/XML").getOntologyModel();
		ConceptExtractor ce = new ConceptExtractor();
		ce.setConcepts(ontology, false);                 //non voglio fare l'enrichment perchè desidero solo quelli definiti nell'ontologia
		List<OntClass>  conceptsOntClass = ce.getExtractedConcepts();
		for(OntClass concept : conceptsOntClass)
			concepts.add(concept); 
	}
	
	// Extracts the properties defined in the ontology then separates datatype from object properties
	private void extractProperties(String ontology_path){
		OntModel ontology = new Model(ontology_path, "RDF/XML").getOntologyModel();
		ExtendedIterator<OntProperty> TempExtractedPropery = ontology.listAllOntProperties();
		while(TempExtractedPropery.hasNext()) {
			OntProperty property = TempExtractedPropery.next();
			if(property.isDatatypeProperty())
				datatypeProperties.add(property);
			else if(property.isObjectProperty())
				objectProperties.add(property);
			else 
				System.out.println(property + " HAS NO TYPE");
		}
		
	}
	
	// Returns all the properties defined in the ontology
	private HashSet<OntProperty> totalProperties() throws Exception{
		HashSet<OntProperty> properties = new HashSet<OntProperty>();
		properties.addAll(datatypeProperties);
		properties.addAll(objectProperties);
		return properties;
	}
	
	
	// Finds every pair of concepts which are involved in a subClassOf relationship (even between internal and external concepts)
	private void subClassOf(String ontology_path){
		OntModel ontologyModel = new Model(ontology_path,"RDF/XML").getOntologyModel();
		
		//Extract Concept from Ontology Model
		ConceptExtractor cExtract = new ConceptExtractor();
		cExtract.setConcepts(ontologyModel, false);  //irrilevante true/false poichè la relazione subClassOf è tra un concetto interno(subj) e un concetto interno/esterno(obj)
		
		Concepts concepts = new Concepts();
		concepts.setConcepts(cExtract.getConcepts());
		concepts.setExtractedConcepts(cExtract.getExtractedConcepts());
		concepts.setObtainedBy(cExtract.getObtainedBy());
		
		//Extract SubClassOf Relation from OntologyModel
		OntologySubclassOfExtractor SbExtractor = new OntologySubclassOfExtractor();
		//The Set of Concepts will be Updated if Superclasses are not in It
		SbExtractor.setConceptsSubclassOf(concepts, ontologyModel);
		SubClassOf SubClassOfRelation = SbExtractor.getConceptsSubclassOf();
		
		this.subClassOf = SubClassOfRelation.getConceptsSubclassOf();
	}
	
	// Finds every pair of concepts which are involved in a ewuivalentClass relationship (even between internal and external concepts)
	private void equivalentClass(String ontology_path){
		OntModel ontologyModel = new Model(ontology_path,"RDF/XML").getOntologyModel();
		
		ConceptExtractor cExtract = new ConceptExtractor();
		cExtract.setConcepts(ontologyModel, false);         
		
		Concepts concepts = new Concepts();
		concepts.setConcepts(cExtract.getConcepts());
		concepts.setExtractedConcepts(cExtract.getExtractedConcepts());
		concepts.setObtainedBy(cExtract.getObtainedBy());
		
		EqConceptExtractor equConcepts = new EqConceptExtractor();
		equConcepts.setEquConcept(concepts, ontologyModel);
		
		HashMap<OntResource, List<OntResource>> map = equConcepts.getExtractedEquConcept();
		for(OntResource concept : map.keySet()){
			List<OntResource> list = map.get(concept);
			for(OntResource eqConcept : list){
				ArrayList<OntResource> lista2 = new ArrayList<OntResource>();
				lista2.add(concept);
				lista2.add(eqConcept);
				this.equivalentClass.add(lista2);
			}
		}
	}
	
	// Finds every pair of concepts which are involved in a disjointClass relationship (even between internal and external concepts)
	private void disjointClass(String ontology_path){
		OntModel ontologyModel = new Model(ontology_path,"RDF/XML").getOntologyModel();
		for(OntClass concept : concepts){
			String queryString = "PREFIX owl:<" + OWL.getURI() + ">" + 
								 "SELECT ?obj " +
								 "WHERE {" +
								 "      <" + concept.getURI() + "> owl:disjointWith ?obj" +
								 "      }";
			
			Query query = QueryFactory.create(queryString) ;
			QueryExecution qexec = QueryExecutionFactory.create(query, ontologyModel) ;
			
			try {
				ResultSet results = qexec.execSelect();
			    OntModel ontologyTempModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM, null); 
			    
			    for ( ; results.hasNext() ; ){ 
					List<OntResource> disjointConcepts = new ArrayList<OntResource>();
					QuerySolution soln = results.nextSolution() ;
					Resource obj = soln.getResource("obj");
					String URIObj = obj.getURI();
					OntResource disjointConc = ontologyTempModel.createOntResource(URIObj);
					disjointConcepts.add(concept);
					disjointConcepts.add(disjointConc);
					this.disjointClasses.add(disjointConcepts);
				}
				  
			} finally { qexec.close() ; }
		}
		
	}
	
	// Calculates the max/min depth of the ontology's concepts(internals & externals)
	// Calculates the root concepts in the typegraph (considering internals & externals)
	private void typeGraphMinMaxDepthAndRoots(String ontology_path){
		TypeGraphExperimental typeGraph = new TypeGraphExperimental(new File(ontology_path), false);
		Set<Concept> vertices = typeGraph.getVertices();
		
		int maxDepth = 0;
		int minDepth = 1000;
		for(Concept vertex : vertices){
			for(int depth : vertex.getDepths()){
				if(depth > maxDepth)
					maxDepth = depth;
				if(depth < minDepth)
					minDepth = depth;
			}
		}
		this.minDepth = minDepth;
		this.maxDepth = maxDepth;
		this.typeGraphRoots = typeGraph.findRoots();
	}
	
	
	
	public void printer(boolean ontologyExists) throws Exception{
		if(ontologyExists){
			System.out.println("# Internal Concepts: " + concepts.size());
			System.out.println("# Datatype Properties: " + datatypeProperties.size());
			System.out.println("# Object Properties: " + objectProperties.size());
			System.out.println("# Total properties: " + totalProperties().size());
			System.out.println("# Concept couples involved in a subClassOf relationship: " + subClassOf.size());
			System.out.println("# Concept couples involved in a equivalentClass relationship: " + equivalentClass.size());
			System.out.println("# Concept couples involved in a disjointConceptWith relationship: " + this.disjointClasses.size());
			System.out.println("# Max depth of concepts in ontology: " + this.maxDepth);
			System.out.println("# Min depth of concepts in ontology: " + this.minDepth);
			System.out.println("# TypeGraph roots considering external concepts: " + this.typeGraphRoots.size());	
		}
		else
			System.out.println("***** NO ONTOLOGY OR EMPTY ONTOLOGY *****");
	}
	
	
	public static void main(String[] args) throws Exception{
		OntologyStatistics os = new OntologyStatistics();
		
		String ontology_path = "";
		boolean ontologyExists = false;
		if(args.length > 0){    //if ontology file exists
			File folder = new File(args[0]);
			Collection<File> listOfFiles = FileUtils.listFiles(folder, new String[]{"owl"}, false);
			ontology_path = listOfFiles.iterator().next().getAbsolutePath();
			if(!ontology_path.contains("empty_ontology.owl"))  //if is not an empty ontology
				ontologyExists = true;
		}
		
		if(ontologyExists){
			os.extractConcepts(ontology_path);
			os.extractProperties(ontology_path);
			os.subClassOf(ontology_path);
			os.equivalentClass(ontology_path);
			os.disjointClass(ontology_path);
			os.typeGraphMinMaxDepthAndRoots(ontology_path);
		}
		os.printer(ontologyExists);

	}

}