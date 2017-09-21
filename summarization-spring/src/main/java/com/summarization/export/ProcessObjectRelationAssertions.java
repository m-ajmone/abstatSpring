package com.summarization.export;

import java.io.File;

import com.summarization.dataset.OverallObjectRelationsCounting;
import com.summarization.dataset.ParallelProcessing;

import com.summarization.export.Events;

public class ProcessObjectRelationAssertions {
	
	public static void main(String[] args) throws Exception {
		
		Events.summarization();
		
		File sourceDirectory = new File(args[0]);
		File minimalTypesDirectory = new File(args[1]);
		File properties = new File(new File(args[2]), "count-object-properties.txt");
		File akps = new File(new File(args[2]), "object-akp.txt");
		
		OverallObjectRelationsCounting counts = new OverallObjectRelationsCounting(properties, akps, minimalTypesDirectory);
		
		new ParallelProcessing(sourceDirectory, "_obj_properties.nt").process(counts);
		
		
	    counts.endProcessing();
	}	
}
