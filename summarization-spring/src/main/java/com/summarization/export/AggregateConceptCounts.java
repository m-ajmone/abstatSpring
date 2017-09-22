package com.summarization.export;

import java.io.File;

import com.summarization.dataset.ConceptCount;
import com.summarization.dataset.Files;

import com.summarization.export.Events;

public class AggregateConceptCounts {

	public static void main(String[] args) throws Exception {
		
		Events.summarization();
		
		File sourceDirectory = new File(args[0]);
		File targetFile = new File(args[1], "count-concepts.txt");
		
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

