package com.summarization.ontology;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.summarization.dataset.Events;
import com.summarization.dataset.ParallelProcessing;
import com.summarization.expetiments.AKPsPartitioner;
import com.summarization.expetiments.PatternGraphMerger;
import com.summarization.expetiments.TriplesRetriever;



public class DatatypeSplittedPatternInference {
	
	private static void parallelProcessing(File specialParts_outputs, final PatternGraphMerger merger){
		ExecutorService executor = Executors.newFixedThreadPool(10);
		for( final File file : specialParts_outputs.listFiles()){
			if(file.getName().contains("_datatype")){
				executor.execute(new Runnable() {
					@Override
					public void run() {
						try {
							merger.process(file);
						} catch (Exception e) {
							Events.summarization().error(file, e);
						}
					}
				});
			}
		}
		executor.shutdown();
	    while(!executor.isTerminated()){}
	}
	
	
	
	public static void datatypeSplittedPatternInference(String akps_dir, String akps_Grezzo_splitted,String ontPath, String specialParts_outputsPath) throws Exception{
		
		Events.summarization();

		File akps_Grezzo_splitted_dir = new File(akps_Grezzo_splitted);
		File ontology = new File(ontPath);
		File specialParts_outputs = new File(specialParts_outputsPath);
        if (!akps_Grezzo_splitted_dir.exists()) {
            if (akps_Grezzo_splitted_dir.mkdirs()) {
                System.out.println("Directory is created!");
            } else {
                System.out.println("Failed to create directory!");
            }
        }
        if (!specialParts_outputs.exists()) {
            if (specialParts_outputs.mkdirs()) {
                System.out.println("Directory is created!");
            } else {
                System.out.println("Failed to create directory!");
            }
        }
		
   //-----------------------------------------------------------      PatternGraph      -------------------------------------------------------------------------------		

		AKPsPartitioner splitter = new AKPsPartitioner(ontology);
		splitter.AKPs_Grezzo_partition(new File(akps_dir+"/datatype-akp_grezzo.txt"), akps_Grezzo_splitted_dir, "_datatype");
		
		TriplesRetriever retriever = new TriplesRetriever(ontology, new File(akps_dir), akps_Grezzo_splitted_dir, specialParts_outputs);
		new ParallelProcessing(akps_Grezzo_splitted_dir, "_datatype.txt").process(retriever);
		
		retriever = null;  
		
		
   //-----------------------------------------------------------    PGs Merge    -------------------------------------------------------------------------------		
		
		PatternGraphMerger merger = new PatternGraphMerger(ontology, new File(akps_dir));
		
		//Dati n patterngraph SPECIALI(cioè con astrazione solo sui concetti) PGS1, PGS2,...PGSn. Dato l'insieme di predicati usati dai PG (uno per ogni PG) appartenenti alla stessa famiglia nel propertyGraph.
		//PGS1 PGS2...PGSn devono essere mergiati a livello "base", ovvero, no verranno modificati solo i pattern che usano topProperties  ma anche quelli ai livelli inferiori.
		//specialParts_outputs contiene m cartelle, ogni cartella contiene dei PGS da mergiare. Alla fine della chiamata che segue, avremo quindi m PG.
		parallelProcessing(specialParts_outputs, merger);
	    
		//ora che non abbiamo più PG speciali, ma tutti omegenei, possiamo fare il merge degli HEADpatterns (pattern con topPropteries), e ottenere un UNICO PG.
	    merger.mergeHeadPatterns("datatype");
	}
	
}
