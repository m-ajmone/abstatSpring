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
	
	
	public AKP getAKP(String subject, String predicate, String object, String summary) {
		return AKPDao.getAKP(subject, predicate, object, summary);
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
	
	
	public String list(String summary, String subj, String pred, String obj, Integer limit, Integer offset, Boolean enrichWithSPO) {
		List<AKP> akps = AKPDao.list(summary, subj, pred, obj, limit, offset);
		String out = ""; 
		ObjectMapper mapper = new ObjectMapper();
		ArrayNode array = mapper.createArrayNode();
		
		for(AKP akp : akps) { 
			JsonNode node = mapper.convertValue(akp, JsonNode.class);
			ObjectNode object = (ObjectNode) node;
			
			if(enrichWithSPO != null && enrichWithSPO == true) {
				Resource sResource = resService.getResourceFromSummary(akp.getSubject(), akp.getSummary_conf());
				Resource pResource = resService.getResourceFromSummary(akp.getPredicate(), akp.getSummary_conf());
				Resource oResource = resService.getResourceFromSummary(akp.getObject(), akp.getSummary_conf());
				
		        object.remove("patternType");
		        object.remove("url");
		        object.remove("subject");
		        object.remove("predicate");
		        object.remove("object");
		        
		        if(sResource!=null) object.putObject("subject").put("globalURL", akp.getSubject()).put("frequency", sResource.getFrequency());
		        else object.putObject("subject").put("globalURL", akp.getSubject());
		       
		        object.putObject("predicate").put("globalURL", akp.getPredicate()).put("frequency", pResource.getFrequency());
		        
		        if(oResource!=null) object.putObject("object").put("globalURL", akp.getObject()).put("frequency", oResource.getFrequency());
		        else object.putObject("object").put("globalURL", akp.getObject());
		        
		        array.add(object);
			}
			else {
		        object.remove("patternType");
		        object.remove("url");
		        array.add(object);
			}
			checkAndRemoveOptionalFields(object, akp);
		}
		
		try { out = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(array);}
		catch(Exception e) {e.printStackTrace(); }
		
		return "{ \"akps\": " + out + "}";
	}
	
	
	public void checkAndRemoveOptionalFields(ObjectNode node, AKP akp) {
		if(akp.getNumberOfInstances() == null)
			node.remove("numberOfInstances");
		if(akp.getCardinality1() == null)
			node.remove("cardinality1");
		if(akp.getCardinality2() == null)
			node.remove("cardinality2");
		if(akp.getCardinality3() == null)
			node.remove("cardinality3");
		if(akp.getCardinality4() == null)
			node.remove("cardinality4");
		if(akp.getCardinality5() == null)
			node.remove("cardinality5");
		if(akp.getCardinality6() == null)
			node.remove("cardinality6");
	}
}
