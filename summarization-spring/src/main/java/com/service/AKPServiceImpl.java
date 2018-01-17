package com.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dao.AKPDao;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.model.AKP;
import com.model.Resource;

@Service
public class AKPServiceImpl implements AKPService{

	@Autowired
	AKPDao AKPDao;

	@Autowired
	ResourceService resService;
	
	public void add(AKP AKP) {
		AKPDao.add(AKP);
	}

	
	public void update(AKP AKP) {
		AKPDao.update(AKP);	
	}

	
	public void delete(AKP AKP) {
		AKPDao.delete(AKP);
	}
	
	
	public String list(String summary, String subj, String pred, String obj, Integer limit, Integer offset, Boolean enrichWithSPO) {
		List<AKP> list = AKPDao.list(summary, subj, pred, obj, limit, offset);
		String out = ""; 
		ObjectMapper mapper = new ObjectMapper();
		ArrayNode array = mapper.createArrayNode();
		
		if(enrichWithSPO != null && enrichWithSPO == true) {
			for(AKP el : list) { 
				Resource subjResource = resService.getResourceFromSummary(el.getSubject(), el.getSummary_conf());
				Resource predResource = resService.getResourceFromSummary(el.getPredicate(), el.getSummary_conf());
				Resource objResource = resService.getResourceFromSummary(el.getObject(), el.getSummary_conf());
				
				JsonNode node = mapper.convertValue(el, JsonNode.class);
		        ObjectNode object = (ObjectNode) node;
		        object.remove("patternType");
		        object.remove("url");
		        object.remove("subject");
		        object.remove("predicate");
		        object.remove("object");
		        
		        if(subjResource!=null)
		        	object.putObject("subject").put("globalURL", el.getSubject()).put("frequency", subjResource.getFrequency());
		        else
		        	object.putObject("subject").put("globalURL", el.getSubject());
		        object.putObject("predicate").put("globalURL", el.getPredicate()).put("frequency", predResource.getFrequency());
		        if(objResource!=null)
		        	object.putObject("object").put("globalURL", el.getObject()).put("frequency", objResource.getFrequency());
		        else
		        	object.putObject("object").put("globalURL", el.getObject());
		        
		        array.add(object);
			}
			try { out = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(array);}
			catch(Exception e) {e.printStackTrace(); }
		}
		else {
			for(AKP el : list) { 
				JsonNode node = mapper.convertValue(el, JsonNode.class);
		        ObjectNode object = (ObjectNode) node;
		        object.remove("patternType");
		        object.remove("url");
		        array.add(object);
			}
			try { out = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(array);}
			catch(Exception e) {e.printStackTrace(); }
		}
		
		
		return "{ \"akps\": " + out + "}";
	}
	
	
	public String getSPOlist(String summary, String position){
		List<String> list = AKPDao.getSPOlist(summary, position);
		String out = "{ \"results\": [";
		for(String el : list) 
			out += "{\"" + position + "\":\""+ el+"\"},";
		
		if(list.size()>0)
			out = out.substring(0,out.length()-1) + "]}";
		else
			out += "]}";
		
		return out;
	}
}
