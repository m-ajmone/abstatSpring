package com.service;

import java.util.List;

import com.model.SubmitConfig;

public interface SubmitConfigService {
	
	public List<SubmitConfig> listSubmitConfig();
	
	public String listSubmitConfig(Boolean loaded, Boolean indexed);
	
	public void add(SubmitConfig submitConfig);
	
	public void delete(SubmitConfig submitConfig);
	
	public void update(SubmitConfig submitConfig);
	
	public SubmitConfig findSubmitConfigById(String id);
	
	
}
