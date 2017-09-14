package com.controller;

import java.io.File;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.model.Ontology;
import com.service.OntologyService;

@Controller
@RequestMapping(value="ontology")
public class OntologyController {
	@Autowired
	OntologyService ontologyService;
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView listOntology() {
		ModelAndView  model = new ModelAndView("CRUD/listOntology");
		model.addObject("listOntology", ontologyService.listOntology());
		return model;
	}
	
	@RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
	public ModelAndView update(@PathVariable("id") String id) {
		ModelAndView model = new ModelAndView("CRUD/updateOntology");
		model.addObject("ontology", ontologyService.findOntologyById(id));
		return model;
	}
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(@ModelAttribute("ontology") Ontology ontology) {
		ontologyService.update(ontology);
		return "redirect:/ontology/list";	
	}
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
		public String delete(@PathVariable("id") String id) throws IOException {
			Ontology ontology = ontologyService.findOntologyById(id);
			ontologyService.delete(ontology);
			return "redirect:/ontology/list";
	}
	@RequestMapping(value = "/deleteDir/{id}", method = RequestMethod.GET)
	public String deleteDir(@PathVariable("id") String id) throws IOException {
		Ontology ontology = ontologyService.findOntologyById(id);
		ontologyService.delete(ontology);
		File ontFile = new File(ontology.getPath());
		ontFile.delete();
		return "redirect:/ontology/list";
	}
}
