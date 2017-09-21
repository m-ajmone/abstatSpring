package com.summarization.export;

import java.io.File;

import com.summarization.experiments.PropertyMinimalizator;
import com.summarization.export.Events;


public class CalculatePropertyMinimalization {
	
	public static void main(String[] args) throws Exception{

		Events.summarization();
		/*	
		File folder = new File(args[0]);
		Collection<File> listOfFiles = FileUtils.listFiles(folder, new String[]{"owl"}, false);
		File ontology = listOfFiles.iterator().next();
		*/
		File ontology = new File(args[0]);
		String patterns_DirPath = args[1];
		
		PropertyMinimalizator onDatatype= new PropertyMinimalizator(new File(patterns_DirPath + "/datatype-akp_grezzo.txt"),
				new File("datatype-akp_grezzo_Updated.txt"),
				new File("datatype-akp_Updated.txt"),
				ontology, true);
		
		onDatatype.readAKPs_Grezzo();
		
		
		PropertyMinimalizator onObject= new PropertyMinimalizator(new File(patterns_DirPath + "/object-akp_grezzo.txt"),
				new File("object-akp_grezzo_Updated.txt"),
				new File("object-akp_Updated.txt"),
				ontology, true);
		
		onObject.readAKPs_Grezzo();
	}
}

