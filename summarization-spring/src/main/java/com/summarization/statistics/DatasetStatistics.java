package com.summarization.statistics;

import java.util.regex.Pattern;  
import java.util.regex.Matcher;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;

public class DatasetStatistics {
	File dataset;
	HashMap<String, int[]> map;
	HashSet<String> concepts;
	HashSet<String> datatypes;
	HashSet<String> objectProperties;
	HashSet<String> datatypeProperties;
	int numWellFormedTriples = 0;
	int numMalformedTriples = 0;
	float[] propertiesPerEntity = new float[]{(float)0,(float)Double.POSITIVE_INFINITY,(float)0};   // [max,min,avg] 
	float totPropsPerEntity = 0;
	float[] assertsPerEntity  = new float[]{(float)0,(float)Double.POSITIVE_INFINITY,(float)0};;      // [max,min,avg] 
	float totAssertsPerEntity = 0;
	int numResourcesNoEntity = 0;
	int numEntity = 0;
	int numEntityWTypo = 0;
	float[] classesPerEntity = new float[]{(float)0,(float)Double.POSITIVE_INFINITY, (float)0};
	float totClassesPerEntity = 0;
	float totAssertsCurrentEnt = 0;

	
	public DatasetStatistics(File dataset){
		this.dataset = dataset;
		map = new HashMap<String, int[]>();
		concepts = new HashSet<String>();
		datatypes = new HashSet<String>();
		objectProperties = new HashSet<String>();
		datatypeProperties = new HashSet<String>();
	}
	
	
	private void readDataset() throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(dataset));
		String line;
		
		String currentSubj = "";
		HashSet<String> outgoingPropsCurrentEnt = new HashSet<String>();
		String subj ="";
		
		while ((line = br.readLine()) != null) {
			if(matcher(line, "triple")){
				numWellFormedTriples++;
				subj = line.substring(1, line.indexOf("> <"));
				line = line.substring(subj.length() + 3);
				String pred = line.substring(1, line.indexOf(">"));
				line = line.substring(pred.length() + 2);
				String obj = line.substring(1, line.lastIndexOf(" ."));
				
				
				if(!subj.equals(currentSubj)){
					map.put(subj, new int[]{0,0});       //inizializzo
					
					if(!currentSubj.equals("") && map.get(currentSubj)[0]==1){    //solo se currentSubj(che è ancora quello vecchio) è entità
						int totPropsCurrentEnt = outgoingPropsCurrentEnt.size();
						if(totPropsCurrentEnt > propertiesPerEntity[0])
							propertiesPerEntity[0] = totPropsCurrentEnt;
						else if(totPropsCurrentEnt < propertiesPerEntity[1])
							propertiesPerEntity[1] = totPropsCurrentEnt;
						totPropsPerEntity += totPropsCurrentEnt; 
						
						if(totAssertsCurrentEnt > assertsPerEntity[0])
							assertsPerEntity[0] = totAssertsCurrentEnt;
						else if(totAssertsCurrentEnt < assertsPerEntity[1])
							assertsPerEntity[1] = totAssertsCurrentEnt;							
						totAssertsPerEntity += totAssertsCurrentEnt;
					}
					
					totAssertsCurrentEnt = 0;
					outgoingPropsCurrentEnt.clear();
					currentSubj = subj;
				}
				
				if(pred.equals("http://www.w3.org/1999/02/22-rdf-syntax-ns#type")){
					int[] temp = map.get(subj);
					temp[1]++;
					map.put(subj, temp);
					concepts.add(obj);
				}
				else{
					int[] temp = map.get(subj);
					if(temp[0] == 0){
						temp[0] = 1;
						map.put(subj, temp);
					}
					outgoingPropsCurrentEnt.add(pred);
					totAssertsCurrentEnt++;
					
					if(matcher(obj, "datatype")){
						datatypeProperties.add(pred);
						if(obj.contains("^^"))
							datatypes.add(obj.substring(obj.lastIndexOf("^^")));
						else 
							datatypes.add("Literal");
					}
					else
						objectProperties.add(pred);
				}
			}
			
			else
				numMalformedTriples++;
		}
		
		/*-------------------------------------- ultimo set di triple -------------------------------------*/
		if(line == null){
			if(map.get(currentSubj)[0]==1){    //solo se currentSubj(che è ancora quello vecchio) è entità
				int totPropsCurrentEnt = outgoingPropsCurrentEnt.size();
				if(totPropsCurrentEnt > propertiesPerEntity[0])
					propertiesPerEntity[0] = totPropsCurrentEnt;
				else if(totPropsCurrentEnt < propertiesPerEntity[1])
					propertiesPerEntity[1] = totPropsCurrentEnt;
				totPropsPerEntity += totPropsCurrentEnt; 
				
				if(totAssertsCurrentEnt > assertsPerEntity[0])
					assertsPerEntity[0] = totAssertsCurrentEnt;
				else if(totAssertsCurrentEnt < assertsPerEntity[1])
					assertsPerEntity[1] = totAssertsCurrentEnt;					
				totAssertsPerEntity += totAssertsCurrentEnt;
			}
		}
		
		/*-------------------------------------- calcolo statistihe rimanenti -------------------------------------*/
		for(String key : map.keySet()){
			int[] value = map.get(key);
			if(value[0] == 1){                // passano solo entità
				numEntity++;
				if(value[1] > 0){
					numEntityWTypo++;	
					totClassesPerEntity += value[1];
					if(value[1] > classesPerEntity[0])
						classesPerEntity[0] = value[1];
					else if(value[1] < classesPerEntity[1])
						classesPerEntity[1] = value[1];
				}
			}
			else
				numResourcesNoEntity++;
		}
		
		classesPerEntity[2] = totClassesPerEntity/(float)numEntity;
		propertiesPerEntity[2] = totPropsPerEntity/numEntity;
		assertsPerEntity[2] = totAssertsPerEntity/numEntity;
		
		
	//	for(String key : map.keySet()){
	//		System.out.println(key + "  " + map.get(key)[0]);
	//	}
		br.close(); 
	}
	
	
	public void printer(){
		System.out.println("#wellformed triples: " + numWellFormedTriples );
		System.out.println("#malformed triples: " + numMalformedTriples );
		System.out.println("#entita: " + numEntity );
		System.out.println("#entita tipizzate: " + numEntityWTypo);
		System.out.println("#subj nelle typing asserts che non sono entita: " + numResourcesNoEntity );
		System.out.println("#concepts: " + concepts.size());
		System.out.println("#datatypes: " + datatypes.size());
		System.out.println("#datatype properties: " + datatypeProperties.size());
		System.out.println("#object properties: " + objectProperties.size());
		System.out.println("#total properties: " + (datatypeProperties.size() + objectProperties.size()));
		System.out.println("max, min, avg outgoing properties per entity " + propertiesPerEntity[0] + "  " + propertiesPerEntity[1] + "  " + propertiesPerEntity[2]);
		System.out.println("max, min, avg relational assertions per entity "+ assertsPerEntity[0] + "  " + assertsPerEntity[1] + "  " + assertsPerEntity[2]);
		System.out.println("max, min, avg classes per entity "+ classesPerEntity[0] + "  " + classesPerEntity[1] + "  " + classesPerEntity[2]);
	}
	
	
	public boolean matcher(String s, String mode){
		boolean output;
		//recognize datatypes and literals
		if(mode.equals("datatype")){
			Pattern p1 = Pattern.compile("^\".+\"\\^\\^.+");
			Matcher m1 = p1.matcher(s);
			Pattern p2 = Pattern.compile("^\".+\"@.+"); 
			Matcher m2 = p2.matcher(s);
			output = m1.matches() || m2.matches();
		}
		//recognize well formed triples
		else{
			Pattern p3 = Pattern.compile("<.*>\\s<.*>\\s\".+\"\\^\\^.+"); 
			Matcher m3 = p3.matcher(s);
			Pattern p4 = Pattern.compile("<.*>\\s<.*>\\s\".+\"@.+"); 
			Matcher m4 = p4.matcher(s);
			Pattern p5 = Pattern.compile("<.*>\\s<.*>\\s<.*>\\s."); 
			//Pattern p5 = Pattern.compile("[<.*>\\s]3."); 
			Matcher m5 = p5.matcher(s);
			output =  m3.matches() || m4.matches() || m5.matches();
		}
		return output;
	}
	
	
	public static void main(String[] args) throws Exception{
		DatasetStatistics ds = new DatasetStatistics(new File(args[0]));
		ds.readDataset();
		ds.printer();
	}
}

