package com.summarization.statistics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.ontology.OntResource;

import com.summarization.ontology.ConceptExtractor;
import com.summarization.ontology.Model;
import com.summarization.experiments.BenchmarkOntology;
import com.summarization.experiments.DatatypeAndObjectProperties;

public class SummaryStatistics {
	HashSet<String> datatypes = new HashSet<String>();
	HashSet<String> concepts = new HashSet<String>();
	HashSet<String> conceptsOntology = new HashSet<String>();
	
	HashMap<String, String> datatypeProperties = new HashMap<String, String>();
	HashMap<String, String> objectProperties = new HashMap<String, String>();
	HashSet<String> multiTypeProperties = new HashSet<String>();
	HashSet<String> propsOntology = new HashSet<String>();
	
	HashSet<String> AKPDatatype = new HashSet<String>();
	HashSet<String> AKPObject = new HashSet<String>();
	
	HashSet<String> misusedProperties = new HashSet<String>();
	HashSet<String> conceptsAsPredicates = new HashSet<String>();
	HashSet<String> predicatesAsSubjOrbj = new HashSet<String>();
	
	HashSet<String> underspecifiedPropertyDomains = new HashSet<String>();
	HashSet<String> underspecifiedPropertyRanges = new HashSet<String>();

	
	//Saves AKPs, concepts, datatypes and properties of the summary
	private void readSummary(String file, String type) throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line;
		while ((line = br.readLine()) != null) {
			String[] splitted = line.split("##");
			String subj = splitted[0];
			String pred = splitted[1];
			String obj = splitted[2];
			
			if(type.equals("object")){
				AKPObject.add(line.substring(0,line.lastIndexOf("##")));
				concepts.add(subj);
				concepts.add(obj);
				objectProperties.put(pred, "null");
			}
			else{
				AKPDatatype.add(line.substring(0,line.lastIndexOf("##")));
				concepts.add(subj);
				datatypes.add(obj);
				datatypeProperties.put(pred, "null");
			}
		}
		br.close();
	}
	
	
	// Assign to every property the "internal" or "external" label.
	// Finds properties which are misused based on ontology
	// Extracts the properties defined in the ontology
	private void propertiesProcessing(String ontologyPath){
		HashMap<String, OntProperty> ontologyPropsMap = DatatypeAndObjectProperties.ontologyProperties(ontologyPath);
		for(String dtProp : datatypeProperties.keySet()){
			if(ontologyPropsMap.containsKey(dtProp)){
				datatypeProperties.put(dtProp, "internal");
				if(!ontologyPropsMap.get(dtProp).isDatatypeProperty())    // if dtProp is not a datatype property
					misusedProperties.add(dtProp);
			}
			else
				datatypeProperties.put(dtProp, "external");		
		}
		for(String objProp : objectProperties.keySet()){
			if(ontologyPropsMap.containsKey(objProp)){
				objectProperties.put(objProp, "internal");
				if(!ontologyPropsMap.get(objProp).isObjectProperty())
					misusedProperties.add(objProp);
			}
			else
				objectProperties.put(objProp, "external");
		}
		
		//Extract properties defined in the ontology
		for(String key : ontologyPropsMap.keySet())
			this.propsOntology.add(key);
	}
	
	// Finds every concept/datype used as predicate and every property used as object/subject
	private void misusedPropsConceptsDatatypes(String summary) throws Exception {		
		// Create the set with all datatypes and concepts
		HashSet<String> allConceptsAndDatatypes = new HashSet<String>();
		allConceptsAndDatatypes.addAll(concepts);
		allConceptsAndDatatypes.addAll(datatypes);
		allConceptsAndDatatypes.addAll(conceptsOntology); 
		
		// Create the set with all properties
		HashSet<String> allProperties = new HashSet<String>();
		allProperties.addAll(totalProperties());
		allProperties.addAll(propsOntology);
		
		BufferedReader br = new BufferedReader(new FileReader(summary));
		String line;
		while ((line = br.readLine()) != null) {
			String[] splitted = line.split("##");
			String subj = splitted[0];
			String pred = splitted[1];
			String obj = splitted[2];
			
			if(allConceptsAndDatatypes.contains(pred))
				conceptsAsPredicates.add(pred);
			if(allProperties.contains(subj))
				predicatesAsSubjOrbj.add(subj);
			if(allProperties.contains(obj))
				predicatesAsSubjOrbj.add(obj);	
		}
		br.close();
	}
	
	// Properties which are used both as datatype and object properties
	private void multiTypeProperties(){
		for(String prop : objectProperties.keySet())
			if(this.datatypeProperties.containsKey(prop))
				multiTypeProperties.add(prop);
	}
	
	// Extracts the concepts defined in the ontology
	private void getConceptsFromOnt(String ontology_path){
		OntModel ontology = new Model(ontology_path, "RDF/XML").getOntologyModel();
		ConceptExtractor ce = new ConceptExtractor();
		ce.setConcepts(ontology, false);
		List<OntClass>  conceptsOntClass = ce.getExtractedConcepts();
		for(OntClass concept : conceptsOntClass)
			conceptsOntology.add(concept.toString()); 
	}
	
	
	private void getUnderspecifiedDomainsOrRanges(String path){
		HashSet<String> underspecifiedPropertyDomainsOnt = new HashSet<String>();
		HashSet<String> underspecifiedPropertyRangesOnt = new HashSet<String>();
		
		for (OntProperty property : new BenchmarkOntology(path).properties()) {
			OntResource range = property.getRange();
			OntResource domain = property.getDomain();
			
			if(range != null && domain != null) {
				continue;
			}
			if(domain == null ) underspecifiedPropertyDomainsOnt.add(property.toString());
			if(range == null ) underspecifiedPropertyRangesOnt.add(property.toString());
		}
		
		for( String property : totalProperties()){
			if(underspecifiedPropertyDomainsOnt.contains(property))
				this.underspecifiedPropertyDomains.add(property);
			if(underspecifiedPropertyRangesOnt.contains(property))
				this.underspecifiedPropertyRanges.add(property);
		}
	}
	
	
	private HashSet<String> getUnderspecifiedProperties(){
		HashSet<String> underspecifiedProperties = new HashSet<String>();
		underspecifiedProperties.addAll(underspecifiedPropertyDomains);
		underspecifiedProperties.addAll(underspecifiedPropertyRanges);
		return underspecifiedProperties;
	}
	
	// Returns all the properties of the summary which are not defined in ontology
	private HashSet<String> getExternalProperties(){
		HashSet<String> externalProperties = new HashSet<String>();
		for(String dtProp: datatypeProperties.keySet())
			if(datatypeProperties.get(dtProp).equals("external"))
				externalProperties.add(dtProp);
		
		for(String objProp: objectProperties.keySet())
			if(objectProperties.get(objProp).equals("external"))
				externalProperties.add(objProp);
		
		return externalProperties;
	}
	
	// Returns the external concepts in the summary
	private HashSet<String> getExternalConcepts(){
		HashSet<String> externalConcepts = new HashSet<String>();
		for(String summaryConcept : concepts)
			if(!conceptsOntology.contains(summaryConcept))
				externalConcepts.add(summaryConcept); 
				
		return externalConcepts;
	}

	// Returns all the properties of the summary
	private HashSet<String> totalProperties(){
		HashSet<String> Properties = new HashSet<String>();
		Properties.addAll(datatypeProperties.keySet());
		Properties.addAll(objectProperties.keySet());
		return Properties;
	}
	
	// Returns all the AKPs of th summary
	private HashSet<String> totalAKPs(){
		HashSet<String> AKPs = new  HashSet<String>();
		AKPs.addAll(AKPDatatype);
		AKPs.addAll(AKPObject);
		return AKPs;
	}
	
	public void printer(String selector){
		if(selector.equals("WithOntology")){
			System.out.println("# Concepts: " + concepts.size());
			System.out.println("# External concepts: " + getExternalConcepts().size() );
			System.out.println("# Datatype Properties: " + datatypeProperties.size());
			System.out.println("# Object Properties: " + objectProperties.size());
			System.out.println("# Total properties: " + totalProperties().size());
			System.out.println("# External properties: " + getExternalProperties().size() );
			System.out.println("# Misused properties (ontology): " + misusedProperties.size() );
			System.out.println("# Misused properties (dataset): " + multiTypeProperties.size());
			System.out.println("# Concepts/datatypes as predicates: " + conceptsAsPredicates.size() );
			System.out.println("# Properties as subject or object: " + predicatesAsSubjOrbj.size() );
			System.out.println("# Datatype AKPs: " + AKPDatatype.size());
			System.out.println("# Object AKPs: " + AKPObject.size());
			System.out.println("# Total AKPs: " + totalAKPs().size());
			System.out.println("# Underspicified properties domains: " + underspecifiedPropertyDomains.size());
			System.out.println("# Underspicified properties ranges: " + underspecifiedPropertyRanges.size());
			System.out.println("# Underspicified properties: " + getUnderspecifiedProperties().size());
		}
		else if (selector.equals("NoOntology")){
			System.out.println("***** NO ONTOLOGY OR EMPTY ONTOLOGY *****");
			System.out.println("# Concepts: " + concepts.size());
			System.out.println("# Datatype Properties: " + datatypeProperties.size());
			System.out.println("# Object Properties: " + objectProperties.size());
			System.out.println("# Total properties: " + totalProperties().size());
			System.out.println("# Misused properties (dataset): " + multiTypeProperties.size());
			System.out.println("# Concepts/datatypes as predicates: " + conceptsAsPredicates.size() );
			System.out.println("# Properties as subject or object: " + predicatesAsSubjOrbj.size() );
			System.out.println("# Datatype AKPs: " + AKPDatatype.size());
			System.out.println("# Object AKPs: " + AKPObject.size());
			System.out.println("# Total AKPs: " + totalAKPs().size());
		}
	}
	
	public static void main(String[] args) throws Exception{
		SummaryStatistics ss = new SummaryStatistics();
		String dir = args[0];
		
		String ontology_path = "";
		boolean ontologyExists = false;
		if(args.length > 1){    //if ontology file exists
			File folder = new File(args[1]);
			Collection<File> listOfFiles = FileUtils.listFiles(folder, new String[]{"owl"}, false);
			ontology_path = listOfFiles.iterator().next().getAbsolutePath();
			if(!ontology_path.contains("empty_ontology.owl"))  //if is not an empty ontology
				ontologyExists = true;
		}
		
		
		if(ontologyExists){    //if ontology exists
			String ontology_path2 = ontology_path.substring(ontology_path.indexOf("data/datasets"));  // because DatatypeAndObjectProperties.ontologyProperties() is weird
			
			ss.readSummary(dir + "/datatype-akp.txt", "datatype");
			ss.readSummary(dir + "/object-akp.txt", "object");
			ss.propertiesProcessing(ontology_path2);
			ss.multiTypeProperties();
			ss.getConceptsFromOnt(ontology_path);
			ss.misusedPropsConceptsDatatypes(dir + "/datatype-akp.txt");
			ss.misusedPropsConceptsDatatypes(dir + "/object-akp.txt");
			ss.getUnderspecifiedDomainsOrRanges(ontology_path2);
			ss.printer("WithOntology");
		}
		else{                //if there's no onotology
			ss.readSummary(dir + "/datatype-akp.txt", "datatype");
			ss.readSummary(dir + "/object-akp.txt", "object");
			ss.multiTypeProperties();
			ss.misusedPropsConceptsDatatypes(dir + "/datatype-akp.txt");
			ss.misusedPropsConceptsDatatypes(dir + "/object-akp.txt");
			ss.printer("NoOntology");
		}
	}
}
	
