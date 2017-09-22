package com.summarization.web;

import com.summarization.export.Events;

public class WebApplication {

	public static void main(String[] args) throws Exception {
		try{
			new SummarizationBrowser().on(Integer.parseInt(args[0])).start();
		}
		catch(Exception e){
			Events.web().error("application didn't start", e);
			System.exit(1);
		}
	}
}
