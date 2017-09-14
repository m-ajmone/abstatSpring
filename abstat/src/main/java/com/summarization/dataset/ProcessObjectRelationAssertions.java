package com.summarization.dataset;

import java.io.File;

public class ProcessObjectRelationAssertions {
	public static void processObjectRelationAssertions(File sourceDirectory, File minTypeResult, String patternsPath) throws Exception {
		Events.summarization();
		
		
		File properties = new File(new File(patternsPath), "count-object-properties.txt");
		File akps = new File(new File(patternsPath), "object-akp.txt");
		
		OverallObjectRelationsCounting counts = new OverallObjectRelationsCounting(properties, akps, minTypeResult);
		
		new ParallelProcessing(sourceDirectory, "_obj_properties.nt").process(counts);
		
		
	    counts.endProcessing();
	}
}
