package com.service;

import java.util.List;

import com.model.Ontology;

public interface OntologyService {

	public List<Ontology> listOntology();
	
	public void add(Ontology ontology);
	
	public void update(Ontology ontology);
	
	public void delete(Ontology ontology);
	
	public Ontology findOntologyById(String id);
	
}
