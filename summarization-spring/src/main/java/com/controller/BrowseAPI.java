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
	public @ResponseBody String browse(
			@RequestParam(value="subj", required=false) String s, 
			@RequestParam(value="pred", required=false) String p,
			@RequestParam(value="obj", required=false) String o,
			@RequestParam(value="summary", required=false) String summary) {
		
	
			// to avoid errors when an empty value is passed
			if(s!= null && s.equals(""))
				s = null;
			if(p!=null && p.equals(""))
				p = null;
			if(o!=null && o.equals(""))
				o = null;
			
			String results = AKPService.list(summary, s, p, o);
			return results;
	}
	
	
	@RequestMapping(value="/api/v1/SPO", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody String SPO(
			@RequestParam(value="summary", required=false) String summary,
			@RequestParam(value="position", required=true) String position) {
			String results = AKPService.getSPOlist(summary, position);
			return results;
	}
}
