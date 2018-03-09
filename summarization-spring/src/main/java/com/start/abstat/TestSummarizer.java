package com.start.abstat;

import java.sql.Timestamp;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.model.SubmitConfig;
import com.service.SubmitConfigService;
import com.service.SummarizationService;

@Component
public class TestSummarizer {

	@Autowired
	SummarizationService summarizationServ;
	
	@Autowired
	SubmitConfigService submitConfigService;
	
	public void run() throws Exception{
		
		ArrayList<String> listOntId = new ArrayList<String>();
		listOntId.add("system-test_ontology");
		ArrayList<String> listOntNames = new ArrayList<String>();
		listOntNames.add("dbpedia_2014");
		
		SubmitConfig conf = new SubmitConfig();
		conf.setDsId("system-test_dataset");
		conf.setDsName("system-test");
		conf.setListOntId(listOntId);
		conf.setListOntNames(listOntNames);
		conf.setSummaryPath("../data/summaries/system-test_dbpedia_2014_MinTpPropMinCardInf/");
		conf.setTimestamp(new Timestamp(System.currentTimeMillis()).toString());
		conf.setTipoMinimo(true);
		
		summarizationServ.summarize(conf, null);
	}
	
}
