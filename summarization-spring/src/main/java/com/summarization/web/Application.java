package com.summarization.web;

import com.summarization.export.Events;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

public class Application extends AbstractHandler{
	
	@Override
	public void handle(String path, Request base, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		base.setQueryEncoding("utf-8");
		response.setContentType("text/html");
		response.setCharacterEncoding("utf-8");
		request.getSession();
		
		try{
			Routing routes = new Routing();
			
			serviceAPI(routes);
			mainUI(routes);
			API(routes);
			experiment(routes);
			experimentalFeatures(routes);
			
			routes.routeTo(path).sendTo(base, response, new HttpParameters(request));
			
		}catch(Exception e){
			Events.web().error("processing request: " + path, e);
			response.setStatus(500);
		}
	}

	private void experimentalFeatures(Routing routes) {
		routes.mapFile("/property-similarity", "property-similarity.html");
	}

	private void API(Routing routes) {
		routes
			.mapJson("/api/v1/autocomplete/concepts", new SolrAutocomplete(new SolrConnector(), "concept-suggest"))    //Solr
			.mapJson("/api/v1/autocomplete/properties", new SolrAutocomplete(new SolrConnector(), "property-suggest")) //Solr
			.mapJson("/api/v1/autocomplete/datatypes", new SolrAutocomplete(new SolrConnector(), "datatype-suggest"))  //Solr
			.mapJson("/api/v1/autocomplete/types", new SolrAutocomplete(new SolrConnector(), "type-suggest"))          //Solr
			.mapJson("/api/v1/datasets", new Datasets(new File("../data/summaries")))                                  //Solr
			.mapJson("/api/v1/sample", new Sample())
			.mapMultiple("/api/v1/queryWithParams", new QueryWithParams())
			.mapJson("/api/v1/AKPsCardinality", new AKPsCardinality())
			.mapJson("/api/v1/resourceOccurrence", new ResourceOccurrence())
			.mapJson("/api/v1/suggestions", new Suggestions())             //SPARQL
			.mapJson("/api/v1/SparqlSemanticSuggestions", new SparqlSemanticSuggestions())    //SPARQL
			.mapJson("/api/v1/SolrSuggestions", new SolrSuggestions());    //Solr
			
	}

	private void experiment(Routing routes) {
		routes
			.mapFile("/experiment/browse", "experiment-browse.html")
			.mapFile("/experiment/search", "experiment-search.html")
			.mapFile("/experiment/query", "experiment-sparql.html")
			.mapFile("/experiment/browse/dbp-2016-10-infobox-clean", "experiment-browse_dbp-2016-10-infobox-clean.html");
	}

	private void mainUI(Routing routes) {
		routes
			.mapFile("/", "index.html")
			.mapFile("/browse", "browse.html")
			.mapFile("/search", "search.html")
			.mapFile("/about", "about.html")
			.mapFile("/apis", "apis.html");
		    
	}

	private void serviceAPI(Routing routes) throws Exception {
		DeployedVersion version = new DeployedVersion(new File(".."));
		String currentVersion = version.branch() + "-" + version.commit();
		routes.mapText("/alive", "OK")
			  .mapText("/version", currentVersion);
	}
}
