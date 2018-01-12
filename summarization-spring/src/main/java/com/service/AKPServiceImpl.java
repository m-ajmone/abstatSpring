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

@Service
public class AKPServiceImpl implements AKPService{

	@Autowired
	AKPDao AKPDao;

	
	public void add(AKP AKP) {
		AKPDao.add(AKP);
	}

	
	public void update(AKP AKP) {
		AKPDao.update(AKP);	
	}

	
	public void delete(AKP AKP) {
		AKPDao.delete(AKP);
	}
	
	
	public String list(String dataset, String subj, String pred, String obj) {
		List<AKP> list = AKPDao.list(dataset, subj, pred, obj);
	
		String out = ""; 
		ObjectMapper mapper = new ObjectMapper();
		ArrayNode array = mapper.createArrayNode();
		
		for(AKP el : list) { 
			JsonNode node = mapper.convertValue(el, JsonNode.class);
	        ObjectNode object = (ObjectNode) node;
	        object.remove("patternType");
	        object.remove("url");
	        array.add(object);
		}
		try { out = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(array);}
		catch(Exception e) {e.printStackTrace(); }
		
		return out;
	}
}
