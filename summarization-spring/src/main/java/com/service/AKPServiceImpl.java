package com.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dao.AKPDao;
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
}
