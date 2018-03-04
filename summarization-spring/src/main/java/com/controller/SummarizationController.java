package com.controller;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.model.SubmitConfig;
import com.service.DatasetService;
import com.service.OntologyService;
import com.service.SummarizationService;


@Controller
@RequestMapping(value = "summarize")
@SessionAttributes("submitConfig")
public class SummarizationController {

	
	@Autowired
	DatasetService datasetService;
	@Autowired
	OntologyService ontologyService;
	@Autowired
	SummarizationService summarizationServ;
	
	
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ModelAndView summarization() {
		ModelAndView model = new ModelAndView("summarize");
		model.addObject("listDataset", datasetService.listDataset());
		model.addObject("listOntology", ontologyService.listOntology());
		model.addObject("submitConfig", new SubmitConfig());
		return model;
	}
	
	
	@RequestMapping(value = "/recap", method = RequestMethod.POST)
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
		
		Date dNow = new Date( );
        SimpleDateFormat ft = new SimpleDateFormat ("dd/MM/yyyy hh:mm:ss");
		
		submitConfig.setDsName(datasetName);
		submitConfig.setListOntNames(ontlogiesListName);
		submitConfig.setSummaryPath(summary_dir);
		submitConfig.setTimestamp(ft.format(dNow));
		
		ModelAndView model = new ModelAndView("recapConfig");
		model.addObject("submitConfig", submitConfig);
				
		return model;
	}
	
	
	@RequestMapping(value = "/run" , method = RequestMethod.POST)
	public Callable<String> runSummarization(@ModelAttribute("submitConfig") SubmitConfig subCfg, @RequestParam("email") String email, RedirectAttributes redirectAttributes)  throws Exception  {
		
		Callable<String> callable = new Callable<String>() {
			@Override
            public String call () throws Exception {
				summarizationServ.summarizeAsyncWrapper(subCfg, email);
				redirectAttributes.addFlashAttribute("message", "You successfully submit the summarization request");
				return "redirect:status";
			}
		};
		return callable;
	}	
	
	
	@RequestMapping(value = "/status", method = RequestMethod.GET)
    public String uploadStatus() {
        return "operationStatus";
    }
	
}
