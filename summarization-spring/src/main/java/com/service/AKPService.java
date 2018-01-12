package com.service;

import com.model.AKP;

public interface AKPService {
	
	public void add(AKP AKP);
	
	public void update(AKP AKP);
	
	public void delete(AKP AKP);
	
	public String list(String dataset,String subj, String pred, String obj);
	
}
