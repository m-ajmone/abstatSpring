package com.summarization.dataset;

import java.io.File;
import com.summarization.expetiments.PropertyMinimalizator;


public class CalculatePropertyMinimalization {
	public static void calculatePropertyMinimalization(String ontPath, String patterns_DirPath) {
		Events.summarization();
		
		File ontology = new File(ontPath);
		
		PropertyMinimalizator onDatatype= new PropertyMinimalizator(new File(patterns_DirPath + "datatype-akp_grezzo.txt"),
				new File(patterns_DirPath +"datatype-akp_grezzo_Updated.txt"),
				new File(patterns_DirPath +"datatype-akp_Updated.txt"),
				ontology, true);
		
		onDatatype.readAKPs_Grezzo();
		
		
		PropertyMinimalizator onObject= new PropertyMinimalizator(new File(patterns_DirPath + "object-akp_grezzo.txt"),
				new File(patterns_DirPath +"object-akp_grezzo_Updated.txt"),
				new File(patterns_DirPath +"object-akp_Updated.txt"),
				ontology, true);
		
		onObject.readAKPs_Grezzo();
	}
}

