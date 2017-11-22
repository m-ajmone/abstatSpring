package com.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.model.SubmitConfig;
import com.service.DatasetService;
import com.service.OntologyService;
import com.service.SubmitConfigService;

@Controller
@RequestMapping(value = "summary")
public class SubmitConfigListController {
	
	@Autowired
	SubmitConfigService submitConfigService;
	@Autowired
	DatasetService datasetService;
	@Autowired
	OntologyService ontologyService;
	
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ModelAndView listSubmitconfig() {
		ModelAndView  model = new ModelAndView("submitConfigList");
		List<SubmitConfig> submitConfigList = submitConfigService.listSubmitConfig();
		model.addObject("listSubmitConfig", submitConfigList);
		return model;
	}
}
