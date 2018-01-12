package com.dao;

import java.util.List;

import com.model.AKP;

public interface AKPDao {

	
	public void add(AKP AKP);
	
	public void update(AKP AKP);
	
	public void delete(AKP AKP);
	
	public List<AKP> list(String dataset, String subj, String pred, String obj);
	
}
