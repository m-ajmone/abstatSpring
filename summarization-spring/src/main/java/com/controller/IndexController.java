package com.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.model.IndexSummary;
import com.model.SubmitConfig;
import com.service.DatasetService;
import com.service.OntologyService;
import com.service.SubmitConfigService;

@Controller
public class IndexController {
	
	@Autowired
	DatasetService datasetService;
	@Autowired
	OntologyService ontologyService;
	@Autowired
	SubmitConfigService submitConfigService;


	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public ModelAndView home() {
		ModelAndView model = new ModelAndView("home");
		model.addObject("listSummaries", submitConfigService.listSubmitConfig());
		model.addObject("listDatasets", datasetService.listDataset());
		model.addObject("listOntologies", ontologyService.listOntology());
		return model;
	}
	
		
	@RequestMapping(value = "/summarize", method = RequestMethod.GET)
	public ModelAndView summarization() {
		ModelAndView model = new ModelAndView("summarize");
		model.addObject("listDataset", datasetService.listDataset());
		model.addObject("listOntology", ontologyService.listOntology());
		model.addObject("submitConfig", new SubmitConfig());
		return model;
	}
	
	
	@RequestMapping(value = "/submit", method = RequestMethod.POST)
	public ModelAndView submitCfg(@ModelAttribute("submitConfig") SubmitConfig submitConfig) throws Exception {
		String summary_dir = ""; String datasetName; String ontName = ""; String inf = ""; String minTp = ""; String propMin = ""; String card = "";
		
		datasetName =  datasetService.findDatasetById(submitConfig.getDsId()).getName();
		if(submitConfig.isTipoMinimo())        minTp = "MinTp";
		if(submitConfig.isCardinalita())       card = "Card";
		if(submitConfig.isInferences())        inf = "Inf";
		if(submitConfig.isPropertyMinimaliz()) propMin = "PropMin";
		if(!submitConfig.isTipoMinimo() || submitConfig.getListOntId().isEmpty()) {
			ontName = "emptyOnt";
		}
		else {
			String ontId = submitConfig.getListOntId().get(0);
			ontName = ontologyService.findOntologyById(ontId).getName();
		}
		summary_dir = "../data/summaries/" + datasetName + "_" + ontName + "_" + minTp + propMin + card + inf +"/";
		
		ArrayList<String> ontlogiesListName = new ArrayList<String>();
		for(String id : submitConfig.getListOntId())
			ontlogiesListName.add(ontologyService.findOntologyById(id).getName());
		
		submitConfig.setDsName(datasetName);
		submitConfig.setListOntNames(ontlogiesListName);
		submitConfig.setSummaryPath(summary_dir);
		submitConfig.setTimestamp(new Timestamp(System.currentTimeMillis()).toString());
		submitConfigService.add(submitConfig);
		
		ModelAndView model = new ModelAndView("recapConfig");
		model.addObject("submitConfig", submitConfig);
				
		return model;
	}
	
	
	@RequestMapping(value = "/browse", method = RequestMethod.GET)
	public ModelAndView browse() {
		ModelAndView model = new ModelAndView("browse");
		model.addObject("listSummaries", submitConfigService.listSubmitConfig(true, null));
		return model;
	}
	
	
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ModelAndView search() {
		ModelAndView model = new ModelAndView("search");
		return model;
	}
	
	
	@RequestMapping(value = "/apis", method = RequestMethod.GET)
	public ModelAndView apis() {
		ModelAndView model = new ModelAndView("apis");
		return model;
	}
	
	
	@RequestMapping(value = "/dataLoading", method = RequestMethod.GET)
	public ModelAndView dataLoading() {
		ModelAndView model = new ModelAndView("dataLoading");
		model.addObject("listSummaries", submitConfigService.listSubmitConfig());
		model.addObject("indexingReq", new IndexSummary());
		return model;
	}
	
}
