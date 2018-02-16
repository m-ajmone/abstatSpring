package com.service;

import com.model.SubmitConfig;

public interface SummarizationService {

	public String summarize(SubmitConfig subCfg, String email) throws Exception ;
	
}
