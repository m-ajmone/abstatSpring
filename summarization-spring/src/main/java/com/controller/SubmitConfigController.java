package com.controller;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.model.SubmitConfig;
import com.service.SubmitConfigService;

@Controller
@RequestMapping(value="submitconfig")
public class SubmitConfigController {
	
	@Autowired
	SubmitConfigService submitConfigService;
	

	@RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
	public ModelAndView update(@PathVariable("id") String id) {
		ModelAndView model = new ModelAndView("CRUD/updateSubmitConfig");
		model.addObject("submitConfig", submitConfigService.findSubmitConfigById(id));
		return model;
	}
	
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(@ModelAttribute("submitConfig") SubmitConfig submitConfig) {
		submitConfigService.update(submitConfig);
		return "redirect:/management";	
	}
	
	
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
		public String delete(@PathVariable("id") String id) throws IOException {
			SubmitConfig submitConfig = submitConfigService.findSubmitConfigById(id);
			submitConfigService.delete(submitConfig);
			return "redirect:/management";
	}
	
	
	@RequestMapping(value = "/deleteDir/{id}", method = RequestMethod.GET)
	public String deleteDir(@PathVariable("id") String id) throws IOException {
		SubmitConfig submitConfig = submitConfigService.findSubmitConfigById(id);
		submitConfigService.delete(submitConfig);
		FileUtils.deleteDirectory(new File(StringUtils.substringBeforeLast(submitConfig.getSummaryPath(), "/")));
		return "redirect:/management";
	}
}
