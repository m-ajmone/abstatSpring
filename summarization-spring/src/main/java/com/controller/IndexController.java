package com.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
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
	public ModelAndView list() {
		ModelAndView model = new ModelAndView("index");
		model.addObject("listDataset", datasetService.listDataset());
		model.addObject("listOntology", ontologyService.listOntology());
		model.addObject("submitConfig", new SubmitConfig());
		return model;
	}
	
	@RequestMapping(value = "/submit", method = RequestMethod.POST)
	public ModelAndView submitCfg(@ModelAttribute("submitConfig") SubmitConfig submitConfig) {
		submitConfig.setDsName(datasetService.findDatasetById(submitConfig.getDsId()).getName());
		if(!submitConfig.getListOntId().isEmpty())
			submitConfig.setOntName(ontologyService.findOntologyById(submitConfig.getListOntId().get(0)).getName());
		else
			submitConfig.setOntName("");
		submitConfigService.add(submitConfig);
		
		ModelAndView model = new ModelAndView("processing2");
		model.addObject("submitConfig", submitConfig);
		/*if(submitConfig.getDsId()!=null)
			model.addObject("datasetName", datasetService.findDatasetById(submitConfig.getDsId()).getName());
		List<String> ontNames = new ArrayList<String>();
		for(String id : submitConfig.getListOntId())
			ontNames.add(ontologyService.findOntologyById(id).getName());
		model.addObject("ontologyNames", ontNames);*/
			
		
		return model;
	}
	
	
}
