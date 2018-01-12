package com.service;

import java.util.List;

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
	
	public String listSubmitConfig(Boolean loaded, Boolean indexed) {
		List<SubmitConfig> results =  submitConfigDao.listSubmitConfig(loaded, indexed);
		
		String output = "[";
		for(SubmitConfig conf : results)
			output += "\"" + conf.getId() + "\", ";
		
		if(results.size()>0)
			output = output.substring(0,output.length()-2) + "]";
		else
			output += "]";
		
		return output;
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