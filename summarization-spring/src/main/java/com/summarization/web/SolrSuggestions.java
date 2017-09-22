package com.summarization.web;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashSet;

import org.apache.commons.io.IOUtils;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


public class SolrSuggestions implements Api {

	
	public InputStream get(RequestParameters request) throws Exception{
		String s = request.get("subjectType");
		String p = request.get("predicate");
		String o = request.get("objectType");
		String[] query  = request.get("query").split(",");
		String rows = request.get("rows");
		String start = request.get("start");
		String dataset = request.get("dataset");
		
		String output = getData(s, p, o, query, rows, start, dataset);
		return IOUtils.toInputStream(output);
	}

	
	private String getData(String s, String p, String o, String[] query, String rows, String start, String dataset) throws Exception{
		String response = "";
		String queryURL = "";
		if(query[0].length() < 3){
			if(query[1].equals("subj"))
				queryURL = "http://localhost/api/v1/autocomplete/concepts?q=" + query[0] + "&edgeMatching=true";
			else if(query[1].equals("pred"))
				queryURL = "http://localhost/api/v1/autocomplete/properties?q=" + query[0] + "&edgeMatching=true";
			else if(query[1].equals("obj"))
				queryURL = "http://localhost/api/v1/autocomplete/types?q=" + query[0] + "&edgeMatching=true";
		}
		else{
			if(query[1].equals("subj"))
				queryURL = "http://localhost/api/v1/autocomplete/concepts?q=" + query[0];
			else if(query[1].equals("pred"))
				queryURL = "http://localhost/api/v1/autocomplete/properties?q=" + query[0];
			else if(query[1].equals("obj"))
				queryURL = "http://localhost/api/v1/autocomplete/types?q=" + query[0];
		}
		
		if(rows != null)
			queryURL += "&rows=" + rows;
		if(start != null)
			queryURL += "&start=" + start;
		if(dataset != null)
			queryURL += "&dataset=" + dataset;
		
		response = suggestions(queryURL);
		return response;
	}
	
	
	private String suggestions(String URL) throws Exception {
		HashSet<String> suggestionsSet = new HashSet<String>();
		Object obj = new JSONParser().parse(readUrl(URL));
		JSONObject json = (JSONObject) obj;
		
	    JSONObject data = (JSONObject) json.get("response");
	    JSONArray docs = (JSONArray) data.get("docs");

    	for(int i=0; i<docs.size(); i++){
	    	JSONObject doc = (JSONObject) docs.get(i);
		    JSONArray URI = (JSONArray)doc.get("URI");
    		suggestionsSet.add(URI.get(0).toString());
    	}
	   
	    String suggestions = "[";
		for(String suggestion : suggestionsSet)
			suggestions += "\"" + suggestion + "\", ";
		
		suggestions = suggestions.substring(0, suggestions.length()-2) + "]";
		return suggestions.toString();
	}
		
	
	private String readUrl(String urlString) throws Exception {
	    BufferedReader reader = null;
	    try {
	        URL url = new URL(urlString);
	        reader = new BufferedReader(new InputStreamReader(url.openStream()));
	        StringBuffer buffer = new StringBuffer();
	        int read;
	        char[] chars = new char[2048];
	        while ((read = reader.read(chars)) != -1)
	            buffer.append(chars, 0, read); 

	        return buffer.toString();
	    } finally {
	        if (reader != null)
	            reader.close();
	    }
	}
	
}
