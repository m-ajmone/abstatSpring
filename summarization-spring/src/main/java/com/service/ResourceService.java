package com.service;

import com.model.Resource;

public interface ResourceService {
	
	public void add(Resource resource);
	
	public void update(Resource resource);
	
	public void delete(Resource resource);
	
	public Resource getResourceFromSummary(String globalURI, String summary);
}
