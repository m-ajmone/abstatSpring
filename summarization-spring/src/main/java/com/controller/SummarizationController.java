package com.controller;

import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.model.SubmitConfig;
import com.service.SummarizationService;


@Controller
@SessionAttributes("submitConfig")
public class SummarizationController {

	@Autowired
	SummarizationService summarizationServ;
	
	
	@RequestMapping(value = "summarization" , method = RequestMethod.POST)
	public Callable<String> runSummarization(@ModelAttribute("submitConfig") SubmitConfig subCfg)  throws Exception  {
		
		Callable<String> callable = new Callable<String>() {
			@Override
            public String call () throws Exception {
				summarizationServ.summarizza(subCfg);
				return "redirect:home";
			}
		};
		return callable;
	}	
	
}
