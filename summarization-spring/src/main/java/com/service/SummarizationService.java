package com.service;

import com.model.SubmitConfig;

public interface SummarizationService {

	public void summarizeAsyncWrapper(SubmitConfig subCfg, String email) throws Exception;
	public void summarize(SubmitConfig subCfg, String email) throws Exception ;
	
}
