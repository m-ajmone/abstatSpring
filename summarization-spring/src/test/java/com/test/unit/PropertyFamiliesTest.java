package com.test.unit;

import static org.junit.Assert.assertThat;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jgraph.graph.DefaultEdge;
import org.junit.Test;


import static org.hamcrest.Matchers.equalTo;

import com.summarization.experiments.Property;
import com.summarization.ontology.PropertyGraph;

public class PropertyFamiliesTest {

	private PropertyGraph propG;
	private List<ArrayList<Property>> pseudoSCS;
	
	public PropertyFamiliesTest(){
	//	File folder = new File(Paths.get("").toAbsolutePath().getParent()+ "/data/datasets/system-test/ontology");
	//	Collection<File> listOfFiles = FileUtils.listFiles(folder, new String[]{"owl"}, false);
		File ontology = new File( new File(System.getProperty("user.dir")).getParentFile().getAbsolutePath() + "/data/DsAndOnt/ontology/dbpedia_2014.owl");
		
		propG = new PropertyGraph(ontology);
		pseudoSCS = propG.pseudoStronglyConnectedSets();
		
		
	}
	
	@Test
	public void eachPropertyShouldBelongToExactlyOneSet(){

		for(Property vertex : propG.getGraph().vertexSet()){
			int cont=0;
			for(ArrayList<Property> set : pseudoSCS){
				if(set.contains(vertex))
					cont++;
			}
			assertThat(cont, equalTo(1));
		}
	}
	
	@Test
	public void aPropertyOfAOnePropSetShouldBeIsolated(){
		for(ArrayList<Property> set : pseudoSCS)
			if(set.size()==1){
				Property prop = (Property)set.toArray()[0];
				Set<DefaultEdge> edges = propG.getGraph().edgesOf(propG.returnV(prop));
				assertThat(edges.size(), equalTo(0));
			}	
	}
	
	
	@Test
	public void eachPropertyShouldBeOnTheSameSetOfItsAncestors(){
		for(ArrayList<Property> set : pseudoSCS){
			for(Property prop : set){
				for(List<Property> path  : propG.pathsToFartherAncestors(prop)){
					Object[] array = path.toArray();
					assertThat(set.contains( (Property)array[array.length-1] ), equalTo(true));
				}
			}
		}
	}
	
	
}
