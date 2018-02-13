package com.controller;

import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.summarization.web.QueryString;

@Controller
public class AutocompleteAPI {

	@RequestMapping(value="/api/v1/SolrSuggestions", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody String getitem(
			@RequestParam(value="subj", required=false) String s, 
			@RequestParam(value="pred", required=false) String p,
			@RequestParam(value="obj", required=false) String o,
			@RequestParam(value="qString") String qString,
			@RequestParam(value="qPosition") String qPosition,
			@RequestParam(value="dataset", required=false) String dataset,
			@RequestParam(value="group", required=false) String group,
			@RequestParam(value="rows", required=false) String rows,
			@RequestParam(value="start", required=false) String start) throws Exception{ 
		
		int qLength = qString.length();
        QueryString queryString = new QueryString();
		String solrCore = "indexing";
		String suggestionService = "";
        
     // setting core
		if(s != null || p != null || o!= null)
			solrCore = "AKPs";
		
     // setting s,p,o costraints
		if(s != null){
			s = "\"" + s + "\"";
			queryString.addParameter("fq", "subject", s);
		}
		if(p != null){
			p = "\"" + p + "\"";
			queryString.addParameter("fq", "predicate", p);
		}
		if(o != null){
			o = "\"" + o + "\"";
			queryString.addParameter("fq", "object", o);
		}

	 // setting suggested service
        if(qPosition.equals("subj"))
			suggestionService = "concept-suggest";
		else if(qPosition.equals("pred"))
			suggestionService = "property-suggest";
		else if(qPosition.equals("obj"))
			suggestionService = "type-suggest";
        if(qLength < 3)
        	suggestionService += "-edge";

     // setting query parameter
        queryString.addParameter("q", qString); 
		
        //setting dataset and paging parameters
		if(dataset!= null)
			queryString.addParameter("fq", "dataset", dataset);
		if(rows != null)
			queryString.addParameter("rows", rows);
		if(start != null)
			queryString.addParameter("start", start);
		
		
     // setting sort & group parameter
		if(solrCore.equals("indexing"))
			queryString.addParameter("sort", "occurrence desc");
        
		if(group != null && group.equals("true")){
		    queryString.addParameter("group", "true");
	        if(qPosition.equals("subj")){
	        	queryString.addParameter("sort", "subjectFreq desc");
	            queryString.addParameter("group.field", "subject_plus_dataset");
	        }
			else if(qPosition.equals("pred")){
	        	queryString.addParameter("sort", "predicateFreq desc");
				queryString.addParameter("group.field", "predicate_plus_dataset");
			}
			else if(qPosition.equals("obj")){
	        	queryString.addParameter("sort", "objectFreq desc");
				queryString.addParameter("group.field", "object_plus_dataset");
			}
		}
		
		
		InputStream solrOutput = new URL("http://localhost:8983/solr/"+solrCore+"/" + suggestionService + queryString.build()).openStream();
			
		String response;
		if(group != null && group.equals("true"))
			response = extractSuggestionsAKPs(solrOutput, solrCore, qPosition);
		else 
			response = extractSuggestions(solrOutput, solrCore, qPosition);
		
		return response;
	}
	
	
	// Extracts suggestions and dataset  and return a String in JSON format
	private String extractSuggestions(InputStream inputStream, String core, String position) throws Exception {
	    //for output
	    JSONObject out = new JSONObject();
	    JSONArray suggestionList = new JSONArray();
		
	    //input parsing
		Object obj = new JSONParser().parse(IOUtils.toString(inputStream, StandardCharsets.UTF_8));
		JSONObject json = (JSONObject) obj;
	    JSONObject data = (JSONObject) json.get("response");
	    JSONArray docs = (JSONArray) data.get("docs");
	    
    	for(int i=0; i<docs.size(); i++){
	    	JSONObject doc = (JSONObject) docs.get(i);
    		JSONArray URI = (JSONArray)doc.get("URI");	
    		
    		JSONObject suggestion = new JSONObject();
	    	if(core.equals("indexing")){
			      suggestion.put("suggestion", URI.get(0).toString());
			      suggestion.put("occurrence", doc.get("occurrence"));
	    	}
	    	else{
	    		if(position.equals("subj")){
				    suggestion.put("suggestion", URI.get(0).toString());
				    suggestion.put("occurrence", doc.get("subjectFreq"));
	    		}
	    		else if(position.equals("pred")){
				    suggestion.put("suggestion", URI.get(1).toString());
				    suggestion.put("occurrence", doc.get("predicateFreq"));
	    		}
	    		else {
				    suggestion.put("suggestion", URI.get(2).toString());
				    suggestion.put("occurrence", doc.get("objectFreq"));
				    }
	    	}
		      suggestion.put("dataset", doc.get("dataset"));
		      suggestionList.add(suggestion);
    	}
    	
    	out.put("suggestions", suggestionList);
    	return out.toJSONString();
	}
		
		
	// Extracts suggestions and dataset  and return a String in JSON format
	private String extractSuggestionsAKPs(InputStream inputStream, String core, String position) throws Exception {
	    //for output
	    JSONObject out = new JSONObject();
	    JSONArray suggestionList = new JSONArray();
	    
	    //input parsing
		Object obj = new JSONParser().parse(IOUtils.toString(inputStream, StandardCharsets.UTF_8));
		JSONObject json = (JSONObject) obj;
		JSONObject plus_dataset;
	    JSONObject grouped = (JSONObject) json.get("grouped");
	    if(position.equals("subj"))
	    	plus_dataset = (JSONObject) grouped.get("subject_plus_dataset");
	    else if(position.equals("pred"))
	    	plus_dataset = (JSONObject) grouped.get("predicate_plus_dataset");
	    else
	    	plus_dataset = (JSONObject) grouped.get("object_plus_dataset");

	    JSONArray groups = (JSONArray) plus_dataset.get("groups");
	    
    	for(int i=0; i<groups.size(); i++){
	    	JSONObject group = (JSONObject) groups.get(i);
	    	JSONObject doclist = (JSONObject) group.get("doclist");
	    	JSONArray docs = (JSONArray) doclist.get("docs");
	    	JSONObject doc = (JSONObject) docs.get(0);
    		JSONArray URI = (JSONArray)doc.get("URI");	
    		
    		JSONObject suggestion = new JSONObject();
    		if(position.equals("subj")){
			    suggestion.put("suggestion", URI.get(0).toString());
			    suggestion.put("occurrence", doc.get("subjectFreq"));
    		}
    		else if(position.equals("pred")){
			    suggestion.put("suggestion", URI.get(1).toString());
			    suggestion.put("occurrence", doc.get("predicateFreq"));
    		}
    		else {
			    suggestion.put("suggestion", URI.get(2).toString());
			    suggestion.put("occurrence", doc.get("objectFreq"));
			}
		      suggestion.put("dataset", doc.get("dataset"));
		      suggestionList.add(suggestion);
    	}
    	
    	out.put("suggestions", suggestionList);
    	return out.toJSONString();
	}
}
