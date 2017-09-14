package com.summarization.dataset;

import java.io.File;


public class ProcessDatatypeRelationAssertions {
	public static void processDatatypeRelationAssertions(File sourceDirectory, File minTypeResult, String patternsPath ) throws Exception {
		
		Events.summarization();
		
		File datatypes = new File(new File(patternsPath), "count-datatype.txt");
		File properties = new File(new File(patternsPath), "count-datatype-properties.txt");
		File akps = new File(new File(patternsPath), "datatype-akp.txt");
		
		OverallDatatypeRelationsCounting counts = new OverallDatatypeRelationsCounting(datatypes, properties, akps, minTypeResult);
		
		new ParallelProcessing(sourceDirectory, "_dt_properties.nt").process(counts);
		
		
	    counts.endProcessing();
	}
}
