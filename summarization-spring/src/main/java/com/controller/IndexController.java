package com.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.model.IndexSummary;
import com.service.DatasetService;
import com.service.OntologyService;
import com.service.SubmitConfigService;

@Controller
@SessionAttributes("submitConfig")
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
	
	
	@RequestMapping(value = "/browse", method = RequestMethod.GET)
	public ModelAndView browse() {
		ModelAndView model = new ModelAndView("browse");
		model.addObject("listSummaries", submitConfigService.listSubmitConfig(true, null));
		return model;
	}
	
	
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ModelAndView search() {
		ModelAndView model = new ModelAndView("search");
		model.addObject("listDataset", submitConfigService.datasetsUsed( null, true));
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
	
	
	@RequestMapping(value = "/management", method = RequestMethod.GET)
	public ModelAndView CRUD() {
		ModelAndView model = new ModelAndView("management");
		model.addObject("listSummaries", submitConfigService.listSubmitConfig());
		model.addObject("listOntologies", ontologyService.listOntology());
		model.addObject("listDatasets", datasetService.listDataset());
		return model;
	}
}
