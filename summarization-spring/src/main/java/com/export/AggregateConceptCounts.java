package com.export;

import java.io.File;

import com.summarization.dataset.ConceptCount;
import com.summarization.dataset.Files;


public class AggregateConceptCounts {
	
	public static void aggregateConceptCounts(String minTypesDirectory, String targetDirectory) throws Exception {
		Events.summarization();
		File file2 = new File(targetDirectory);
        if (!file2.exists()) {
            if (file2.mkdirs()) {
                System.out.println("Directory is created!");
            } else {
                System.out.println("Failed to create directory!");
            }
        }
        
        
		File sourceDirectory = new File(minTypesDirectory);
		File targetFile = new File(targetDirectory, "count-concepts.txt");//creazione cartelle output AggregateConceptCounts
		

		
		ConceptCount counts = new ConceptCount();
		for(File file : new Files().get(sourceDirectory, "_countConcepts.txt")){
			try{
				counts.process(file);
			}catch(Exception e){
				Events.summarization().error("processing " + file, e);
			}
		}
		counts.writeResultsTo(targetFile);
	}
}

