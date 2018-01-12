package com.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.service.AKPService;

@Controller
public class BrowseAPI {

	@Autowired
	AKPService AKPService;
	
	@RequestMapping(value="/api/v1/browse", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody String getitem(
			@RequestParam(value="subj", required=false) String s, 
			@RequestParam(value="pred", required=false) String p,
			@RequestParam(value="obj", required=false) String o,
			@RequestParam(value="dataset", required=false) String dataset) throws Exception {
			String results = AKPService.list(dataset, s, p, o);
			return results;
	}
	
}
