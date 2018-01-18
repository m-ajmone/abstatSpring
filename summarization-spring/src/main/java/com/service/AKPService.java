package com.service;

import com.model.AKP;

public interface AKPService {
	
	public void add(AKP AKP);
	
	public void update(AKP AKP);
	
	public void delete(AKP AKP);
	
	public String list(String summary,String subj, String pred, String obj, Integer limit, Integer offset, Boolean enrichWithSPO);
	
	public String getSPOlist(String summary, String position);
	
	public AKP getAKP(String subject, String predicate, String object, String summary);
	
}
