package com.summarization.export;

import com.summarization.dataset.FileSystemConnector;
import com.summarization.dataset.TextInput;
import com.summarization.web.SolrConnector;

import java.io.File;


public class RunAKPIndexing
{
	public static void main(String[] args) throws Exception
	{
		Events.summarization();
		
		try{
			String file = args[0];
			String dataset = args[1];
			String type = args[2];
			String domain = args[3];
			
			IndexAKP indexing = new IndexAKP(new SolrConnector(), dataset, type, domain);
			indexing.process(new TextInput(new FileSystemConnector(new File(file))));
			indexing.endProcessing();
		}
		catch(Exception e){
			Events.summarization().error("", e);
		}
	}
}