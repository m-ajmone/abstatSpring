package com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.service.SubmitConfigService;

@Controller
public class SummariesListAPI {

	@Autowired
	SubmitConfigService submitConfigService;
	
	@RequestMapping(value="/api/v1/summaries", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody String getitem( @RequestParam(value="loaded", required=false) Boolean loaded,
										 @RequestParam(value="indexed", required=false) Boolean indexed) {
			String result = submitConfigService.listSubmitConfig(loaded, indexed);
			return result;
	}
}
