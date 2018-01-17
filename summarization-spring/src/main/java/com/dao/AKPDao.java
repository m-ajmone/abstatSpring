package com.dao;

import java.util.List;

import com.model.AKP;

public interface AKPDao {

	
	public void add(AKP AKP);
	
	public void update(AKP AKP);
	
	public void delete(AKP AKP);
	
	public List<AKP> list(String summary, String subj, String pred, String obj, Integer limit, Integer offset);
	
	public List<String> getSPOlist(String summary, String position);
}
