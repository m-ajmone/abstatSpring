package com.summarization.export;

import java.io.File;
import java.io.FileOutputStream;

import java.util.ArrayList;

import com.summarization.export.CalculateCardinality;
import com.summarization.export.Events;
import com.summarization.export.Split;

public class MainCardinality {

	public static void main(String[] args) throws Exception{
		
		Events.summarization();
		
		String path = args[0];

		File obj_grezzo = new File(path+"/object-akp_grezzo.txt");
		File dt_grezzo = new File(path+"/datatype-akp_grezzo.txt");
		ArrayList<String> listP = new ArrayList<String>();
		ArrayList<String> listAKP = new ArrayList<String>();

		Split.readFromFiles(obj_grezzo, path, listP, listAKP);
		Split.readFromFiles(dt_grezzo, path, listP, listAKP);

		
		File folderAkps = new File(path+"/Akps");
		File folderProps = new File(path+"/Properties");
		File globalCard = new File(path+"/globalCardinalities.txt");
		File patternCard = new File(path+"/patternCardinalities.txt");
		
		CalculateCardinality.concurrentWork(folderProps, listP, globalCard);
		CalculateCardinality.concurrentWork(folderAkps, listAKP, patternCard);

		//mappatura di akp+nome file di testo
		File mapAkps = new File(path+"/mapAkps.txt");
		for(String akp : listAKP){
			String index = Integer.toString(listAKP.indexOf(akp));
			String map = akp+" - AKP"+index+".txt";
			FileOutputStream fos;
			fos = new FileOutputStream(new File(mapAkps.getPath()), true);
			fos.write((map+"\n").getBytes());
			fos.close();

		}

	}


}
