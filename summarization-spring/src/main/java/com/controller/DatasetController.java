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

import com.model.Dataset;
import com.service.DatasetService;

@Controller
@RequestMapping(value = "dataset")
public class DatasetController {
	
	@Autowired
	DatasetService datasetService;
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView listDataset() {
		ModelAndView  model = new ModelAndView("CRUD/listDataset");
		model.addObject("listDataset", datasetService.listDataset());
		return model;
	}
	
	@RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
	public ModelAndView update(@PathVariable("id") String id) {
		ModelAndView model = new ModelAndView("CRUD/updateDataset");
		model.addObject("dataset", datasetService.findDatasetById(id));
		return model;
	}
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(@ModelAttribute("dataset") Dataset dataset) {
		datasetService.update(dataset);
		return "redirect:/dataset/list";	
	}
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
		public String delete(@PathVariable("id") String id) throws IOException {
			Dataset dataset = datasetService.findDatasetById(id);
			datasetService.delete(dataset);
			return "redirect:/dataset/list";
	}
	@RequestMapping(value = "/deleteDir/{id}", method = RequestMethod.GET)
	public String deleteDir(@PathVariable("id") String id) throws IOException {
		Dataset dataset = datasetService.findDatasetById(id);
		datasetService.delete(dataset);
		FileUtils.deleteDirectory(new File(StringUtils.substringBeforeLast(dataset.getPath(), "/")));
		return "redirect:/dataset/list";
	}
	
}
