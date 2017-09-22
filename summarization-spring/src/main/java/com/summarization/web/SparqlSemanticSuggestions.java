package com.summarization.web;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.RDFNode;

import com.summarization.experiments.SparqlEndpoint;

public class SparqlSemanticSuggestions implements Api{
	
	public InputStream get(RequestParameters request) throws Exception{
		String s = request.get("subjectType");
		String p = request.get("predicate");
		String o = request.get("objectType");
		String[] query  = request.get("query").split(",");
		String format = request.get("format");
		String dataset = request.get("dataset");
		String rows = request.get("rows");
		
		
		String queryString = buildQuery(s, p, o, query, dataset, rows);
		SparqlEndpoint localEndpoint = SparqlEndpoint.local();
		ResultSet results = localEndpoint.execute(queryString);	
	
		
		if(format!=null && format.equals("json")){
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ResultSetFormatter.outputAsJSON(out, results);
			byte[] data = out.toByteArray();
			ByteArrayInputStream istream = new ByteArrayInputStream(data);
			return istream;
		}
		else{
			String output = "";
			while(results.hasNext()){
				RDFNode node = results.next().get("suggestion");
				output += "\"" + node.toString() + "\", ";
			}
			output = "[" + output.substring(0, output.length()-2) + "]";
			return IOUtils.toInputStream(output);
		}
	}

	
	
	
	private String buildQuery(String s, String p, String o, String[] query, String dataset, String rows) throws Exception{
		String position = "";
		if(query[1].equals("subj"))
			position = "subject";
		else if(query[1].equals("pred"))
			position = "predicate";
		
		String fromBegin = "";
		if(query[0].length()<=2)
			fromBegin = "^";
		
		String queryString = "";
		if(query[1].equals("obj")){
			queryString =  "\nSELECT  DISTINCT ?suggestion ";
			if(dataset!= null)
			queryString += "FROM <http://ld-summaries.org/" + dataset + "> ";
			queryString	+= "\nWHERE { ";
			if(s!=null)
			queryString += "\n	?input_subj <http://www.w3.org/2000/01/rdf-schema#seeAlso> <" + s +"> ."
		            	+  "\n	?akp <http://www.w3.org/1999/02/22-rdf-syntax-ns#subject>  ?input_subj .";
			if(p!=null)
			queryString += "\n	?input_pred <http://www.w3.org/2000/01/rdf-schema#seeAlso> <" + p +"> ."
		            	+  "\n	?akp <http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate>  ?input_pred .";
		
			queryString += "\n   ?akp <http://www.w3.org/1999/02/22-rdf-syntax-ns#object>  ?o ."
						+  "\n   ?o <http://www.w3.org/2000/01/rdf-schema#seeAlso> ?suggestion ."
						+  "\n   BIND(REPLACE(STR(?suggestion),\"[A-Z,a-z,0-9,/,\\\\.,\\\\:]*?[/]\",\"\") AS ?rep1)."
						+  "\n   BIND(REPLACE(STR(?rep1),\"[A-Z,a-z,0-9,/,\\\\.,\\\\:,\\\\-]*?[\\\\#]\",\"\") AS ?rep2)"
						+  "\n   FILTER(regex(str(?rep2), \"" + fromBegin + query[0] + "\", \"i\")) "
						+  "\n}";
		}
		else{
			queryString =  "\nSELECT  DISTINCT ?suggestion ";
			if(dataset!= null)
			queryString += "FROM <http://ld-summaries.org/" + dataset + "> ";
			queryString	+= "\nWHERE { ";
			if(s!=null)
			queryString += "\n	?input_subj <http://www.w3.org/2000/01/rdf-schema#seeAlso> <" + s +"> ."
			            +  "\n	?akp <http://www.w3.org/1999/02/22-rdf-syntax-ns#subject>  ?input_subj .";
			if(p!=null)
			queryString += "\n	?input_pred <http://www.w3.org/2000/01/rdf-schema#seeAlso> <" + p +"> ."
			            +  "\n	?akp <http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate>  ?input_pred .";
			if(o!=null)
			queryString += "\n	?input_obj <http://www.w3.org/2000/01/rdf-schema#seeAlso> <" + o +"> ."
			            +  "\n	?akp <http://www.w3.org/1999/02/22-rdf-syntax-ns#object>  ?input_obj .";
			
			queryString += "\n   ?akp <http://www.w3.org/1999/02/22-rdf-syntax-ns#" + position + ">  ?o . "
						+  "\n   ?o <http://www.w3.org/2000/01/rdf-schema#seeAlso> ?suggestion ."
						+  "\n   BIND(REPLACE(STR(?suggestion),\"[A-Z,a-z,0-9,/,\\\\.,\\\\:]*?[/]\",\"\") AS ?rep1)."
						+  "\n   FILTER(regex(str(?rep1), \"" + fromBegin + query[0] + "\", \"i\")) "
						+  "\n}";
		}
		
		//output limit
		if(rows!=null){
			int lim = Integer.parseInt(rows);
			if(lim >=0)
				queryString += "LIMIT "+lim;
		}
		
		return queryString;
	}
	
	
	
	
	
	
}
