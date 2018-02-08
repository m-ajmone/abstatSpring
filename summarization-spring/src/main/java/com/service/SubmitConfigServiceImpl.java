package com.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dao.SubmitConfigDao;
import com.model.SubmitConfig;

@Service
public class SubmitConfigServiceImpl implements SubmitConfigService {

	@Autowired
	SubmitConfigDao submitConfigDao;

	public List<SubmitConfig> listSubmitConfig() {
		return submitConfigDao.listSubmitConfig();
	}
	
	
	public List<SubmitConfig> listSubmitConfig(Boolean loaded, Boolean indexed) {
		 List<SubmitConfig> list = submitConfigDao.listSubmitConfig();
		 List<SubmitConfig> output = new ArrayList<SubmitConfig>();
		 
		 for(SubmitConfig el : list) {
			 if(loaded!=null && indexed!=null) {
				 if(el.isLoadedMongoDB() == loaded  &&  el.isIndexedSolr() == indexed)
					 output.add(el);
			 }
			 else if(loaded!=null) {
				 if(el.isLoadedMongoDB() == loaded)
					 output.add(el);
			 }
			 else if(indexed!=null) {
				 if(el.isIndexedSolr() == indexed)
					 output.add(el);
			 }
			 else {
				 output.add(el);
			 }
		 }

		 return output;
	}
	
	
	public String listSubmitConfigJSON(Boolean loaded, Boolean indexed) {
		List<SubmitConfig> results =  this.listSubmitConfig(loaded, indexed);
		
		String output = "{\"summaries\":[";
		for(SubmitConfig conf : results)
			output += "{\"id\":\"" + conf.getId() + "\"},";
		
		if(results.size()>0)
			output = output.substring(0,output.length()-1) + "]}";
		else
			output += "]}";
		
		return output;
	}

	
 /* returns the dataset list of the summaries which satisfy the constraints*/
	public Set<String> datasetsUsed(Boolean loaded, Boolean indexed){
		List<SubmitConfig> results =  this.listSubmitConfig(loaded, indexed);
		Set<String> datasets = new HashSet<String>();
		
		for(SubmitConfig result : results) 
			datasets.add(result.getDsName());
		
		return datasets;
	}
	
	
	public void add(SubmitConfig submitConfig) {
		submitConfigDao.add(submitConfig);
		
	}

	
	public void delete(SubmitConfig submitConfig) {
		submitConfigDao.delete(submitConfig);
		
	}
	

	public SubmitConfig findSubmitConfigById(String id) {
		return submitConfigDao.findSubmitConfigById(id);
	}

	
	public void update(SubmitConfig submitConfig) {
		submitConfigDao.update(submitConfig);
	}
	
}