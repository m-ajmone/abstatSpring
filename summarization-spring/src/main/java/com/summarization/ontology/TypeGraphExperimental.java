package com.summarization.ontology;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JFrame;

import org.jgraph.graph.DefaultEdge;
import org.jgrapht.experimental.dag.DirectedAcyclicGraph;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.vocabulary.OWL;

import com.summarization.experiments.Concept;
import com.summarization.experiments.JgraphGUI;

public class TypeGraphExperimental {

	 DirectedAcyclicGraph<Concept, DefaultEdge> graph = new DirectedAcyclicGraph<Concept, DefaultEdge>(DefaultEdge.class);
	 HashMap<Concept, List< List<Concept>>> conceptMediumGeneralDepth = new  HashMap<Concept, List< List<Concept>>>();
	 
	 public TypeGraphExperimental(File ontology, boolean forceUniqueRoot){
			OntModel ontologyModel = new Model(ontology.getAbsolutePath(),"RDF/XML").getOntologyModel();
			
			ConceptExtractor cExtract = new ConceptExtractor();
			cExtract.setConcepts(ontologyModel, true);
			Concepts concepts = new Concepts();
			concepts.setConcepts(cExtract.getConcepts());
			concepts.setExtractedConcepts(cExtract.getExtractedConcepts());
			concepts.setObtainedBy(cExtract.getObtainedBy());
			OntologySubclassOfExtractor extractor = new OntologySubclassOfExtractor();
			extractor.setConceptsSubclassOf(concepts, ontologyModel);
				
			
			//I need only the internal concepts
			OntModel ontologyModelTemp = new Model(ontology.getAbsolutePath(),"RDF/XML").getOntologyModel();
			ConceptExtractor cExtractTemp = new ConceptExtractor();
			cExtractTemp.setConcepts(ontologyModelTemp, false);
			
			//I separate internal from external concepts
			for(List<OntClass> subClasses : extractor.getConceptsSubclassOf().getConceptsSubclassOf()){
				String tipoString = subClasses.get(0).getURI();
				String superTipoString = subClasses.get(1).getURI();
				Concept tipo = null;
				Concept superTipo = null;
				if(cExtractTemp.getConcepts().keySet().contains( tipoString ))
					tipo = new Concept(tipoString, true);			
				else
					tipo = new Concept(tipoString, false);
				
				if(cExtractTemp.getConcepts().keySet().contains( superTipoString ) || superTipoString.equals(OWL.Thing.getURI()))
					superTipo = new Concept(superTipoString, true);		
				else
					superTipo = new Concept(superTipoString, false);
				addEdge(tipo, superTipo);
			}
			
			if(forceUniqueRoot)
				forceUniqueRoot();
			
			calculateDepth();
	 }
	

	public Concept returnV(Concept p){
		Set<Concept> vertices = new HashSet<Concept>();
		   vertices.addAll(graph.vertexSet());
		   for (Concept vertex : vertices)    
		   	if(p.equals(vertex))
		    	return vertex; 
		   return null;
	}
	
		
	private void addEdge(Concept tipo, Concept supertipo){
		if(!graph.containsVertex(tipo))
			graph.addVertex(tipo);
			
		if(graph.containsVertex(supertipo))
			graph.addEdge(tipo, returnV(supertipo));
		else{
			graph.addVertex(supertipo);
			graph.addEdge(tipo, supertipo);
		}
	}
	
	
	public Set<Concept> getVertices(){
		return graph.vertexSet();
	}
	
	
	private void calculateDepth(){
		Set<Concept> vertices = new HashSet<Concept>();
		vertices.addAll(graph.vertexSet());
	    for(Concept vertex : vertices)
	    	depthFirstVisit(vertex);
	}
	
	private List<Set<Integer>> depthFirstVisit(Concept vertex){
		if(vertex.getColor().equals("white")){
			Set<DefaultEdge> outgoingEdges = graph.outgoingEdgesOf(vertex);
	    	for (DefaultEdge edge : outgoingEdges){
	    		Concept parent = graph.getEdgeTarget(edge);
	    		List<Set<Integer>> parent_depths = depthFirstVisit(parent);
	    		
	    		TreeSet<Integer> tempInternal = new TreeSet<Integer>();
	    		TreeSet<Integer> tempAll = new TreeSet<Integer>();
	    		for(Integer n : parent_depths.get(0)){
	    			if(parent.isInternal())
	    				tempInternal.add(n+1);
	    			tempAll.add(n+1);
	    		}
	    		for(Integer n : parent_depths.get(1))
	    			tempAll.add(n+1); 
	    		
	    		vertex.addDepthsInternal(tempInternal);
	    		vertex.addDepths(tempAll);
	    	}
	    	
	    	if(outgoingEdges.isEmpty()){  //se è un root
	    		TreeSet<Integer> temp = new TreeSet<Integer>();
	    		temp.add(0);
	    		vertex.addDepthsInternal(temp);
	    		vertex.addDepths(temp);
	    	}
	    	
	    	vertex.setColor("black");
		}
		List<Set<Integer>> depths = new ArrayList<Set<Integer>>();
		depths.add(vertex.getDepthsInternal());
		depths.add(vertex.getDepths());
		return depths;
	}    	
   	
	
	//Ritorna tutti i vertici orfani, ovvero senza un padre
	public HashSet<Concept> findRoots(){
		HashSet<Concept> orfani = new HashSet<Concept>();
		
		Set<Concept> vertices = new HashSet<Concept>();
	    vertices.addAll(graph.vertexSet());
	    for (Concept vertex : vertices) { 
	    	boolean isOrphan = true;
	    	Set<DefaultEdge> relatedEdges = graph.edgesOf(vertex);
			for (DefaultEdge edge : relatedEdges) {
				if(graph.getEdgeSource(edge).equals(vertex))
					isOrphan = false;
			}
			if(isOrphan)
				orfani.add(vertex);
	    }
	    return orfani;   
	}
	
	/*Ritorna i supertipi diretti del concetto in input
	 * SE è Thing o Literal RITORNA NULL*/
   public ArrayList<Concept> superTipo(Concept arg, String type, String positionInPattern){
   	
   	if(arg.getURI().equals("http://www.w3.org/2002/07/owl#Thing") || arg.getURI().equals("http://www.w3.org/2000/01/rdf-schema#Literal"))
           return null;
   	
   	else if(!graph.containsVertex(arg)){
   		ArrayList<Concept> output = new ArrayList<Concept>();
   		if(positionInPattern.equals("subject") || type.equals("object"))
   			output.add(returnV(new Concept("http://www.w3.org/2002/07/owl#Thing")));
   		else
   			output.add(new Concept("http://www.w3.org/2000/01/rdf-schema#Literal"));
   		return output;
   	}
   	
       else{
       	ArrayList<Concept> supertipi = new ArrayList<Concept>();
           Concept source, target;
           Set<Concept> vertices = new HashSet<Concept>();
           vertices.addAll(graph.vertexSet());
      
           for (Concept vertex : vertices) {
               if(vertex.equals(arg)){        //cioè se ho trovato il concetto nel typegraph
                   Set<DefaultEdge> relatedEdges = graph.edgesOf(vertex);
                   for (DefaultEdge edge : relatedEdges) {
                       source = graph.getEdgeSource(edge);
                       target = graph.getEdgeTarget(edge);
                       if(source.equals(vertex))
                           supertipi.add(target);     
                   }
               }
           }
           
           return supertipi;
       }
   }

   private void forceUniqueRoot(){
   	for(Concept c : findRoots()){
   		if(!c.getURI().equals("http://www.w3.org/2002/07/owl#Thing")){
   		//	c.setDepth(1); comentato in seguito alla riscrittura dell'algoritmo per il calcolo della profondità
   			if(!graph.containsVertex(new Concept("http://www.w3.org/2002/07/owl#Thing")))
   				graph.addVertex(new Concept("http://www.w3.org/2002/07/owl#Thing"));
   			addEdge(c, returnV(new Concept("http://www.w3.org/2002/07/owl#Thing")));
   		}
   	}
   }
   
   
	
//--------------------------------------------------------- UTILS ------------------------------------------------------------------------------	
  
   
   public ArrayList<ArrayList<Concept>> gerarchy(Concept concept){
   	ArrayList<ArrayList<Concept>> master = new ArrayList<ArrayList<Concept>>();
   	Set<DefaultEdge> outgoingEdges = graph.outgoingEdgesOf(concept);
   	
   	for (DefaultEdge edge : outgoingEdges){
   		Concept padre = graph.getEdgeTarget(edge);
   		ArrayList<ArrayList<Concept>> gerarchia = gerarchy(padre);	
   		for(ArrayList<Concept> cammino : gerarchia){
   			ArrayList<Concept> temp = new ArrayList<Concept>();
   			temp.add(concept);
   			temp.addAll(cammino);
   			master.add(temp);
   		}
   	}
   	if(outgoingEdges.isEmpty()){
   		ArrayList<Concept> temp = new ArrayList<Concept>();
   		temp.add(concept);
   		master.add(temp);
   	}
 
   		
   	return master;	
   }
   
   public void printPathsToRoot(String nomeFile){
   	try{
			FileOutputStream fos = new FileOutputStream(new File(nomeFile));
			
			Set<Concept> vertices = new HashSet<Concept>();
			vertices.addAll(graph.vertexSet());
		    for(Concept vertex : vertices){
		    	ArrayList<ArrayList<Concept>> gerarchia = gerarchy(vertex);	
	    		for(ArrayList<Concept> cammino : gerarchia){
	    		//	ArrayList<Concept> temp = new ArrayList<Concept>();
	    		//	temp.add(vertex);
	    		//	temp.addAll(cammino);
	    			fos.write(("\n" + cammino.toString()).getBytes());
	    		}
		    }
   	}catch(Exception e){
			System.out.println("Eccezione printPathsToRoot");
   	}
   }
   
   
	//per OGNI nodo v del grafo stampa una riga per ogni padre, del tipo: "v  padre(v)". Se v è orfano stampa la riga "v"
	public void stampatypeGraphSuFile(String nomeFile){	
		try{
			FileOutputStream fos = new FileOutputStream(new File(nomeFile));
			
			Concept source, target;
			Set<Concept> vertices = new HashSet<Concept>();
			vertices.addAll(graph.vertexSet());
  
			for (Concept vertex : vertices) {
				boolean orfano = true;
				Set<DefaultEdge> relatedEdges = graph.edgesOf(vertex);
				for (DefaultEdge edge : relatedEdges) {
                   source = graph.getEdgeSource(edge);
                   target = graph.getEdgeTarget(edge);
                   if(source.equals(vertex)){
                       fos.write((source.getURI()+"$$"+source.getDepths() +"  "+ target.getURI()+"$$"+target.getDepths()+"\n").getBytes());
                       orfano = false;
                   }
               }
				if(orfano)
					fos.write((vertex.getURI()+"$$"+vertex.getDepths()+"\n").getBytes());
			}
			fos.close();
		
		}
		catch(Exception e){
			System.out.println("Eccezione stampatypeGraphSuFile");
		}
	}	
		
   
	public void disegna(){
		JgraphGUI gui = new JgraphGUI(graph);
		JFrame frame = new JFrame();
		frame.getContentPane().add(gui);
		frame.setTitle("Type Graph");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
}

