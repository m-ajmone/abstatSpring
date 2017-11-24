package com.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dao.ResourceDao;
import com.model.Resource;

@Service
public class ResourceServiceImpl implements ResourceService{


	@Autowired
	ResourceDao resourceDao;

	
	public void add(Resource resource) {
		resourceDao.add(resource);
	}

	
	public void update(Resource resource) {
		resourceDao.update(resource);
		
	}

	
	public void delete(Resource resource) {
		resourceDao.delete(resource);
		
	}
}
