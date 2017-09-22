package com.summarization.web;

import java.io.InputStream;

public class SolrAutocomplete implements Api{
	
	private Connector connector;
	private String suggestionService;

	public SolrAutocomplete(Connector connector, String service) {
		this.connector = connector;
		this.suggestionService = service;
	}

	@Override
	public InputStream get(RequestParameters request) throws Exception {
		QueryString queryString = new QueryString();
		
		String edgeMatching = "";
		if(request.get("edgeMatching")== null || request.get("edgeMatching").equals("false"))
			queryString.addParameter("q", "label_ngram", request.get("q"));
		else{
			edgeMatching = "-edge";
			queryString.addParameter("q", "label_edgeNgram", request.get("q"));
		}
		
		if(request.get("dataset")!= null)
			queryString.addParameter("fq", "dataset", request.get("dataset"));
		
		String rows = request.get("rows");
		if(rows != null)
			queryString.addParameter("rows", rows);
		
		String start = request.get("start");
		if(start != null)
			queryString.addParameter("start", start);
		
		return connector.query("/solr/indexing/" + suggestionService + edgeMatching, queryString);
	}
	
}
