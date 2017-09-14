package com.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dao.OntologyDao;
import com.model.Ontology;

@Service
public class OntologyServiceImpl implements OntologyService {

	@Autowired
	OntologyDao ontologyDao;

	
	public List<Ontology> listOntology() {
		return ontologyDao.listOntology();
	}

	
	public void add(Ontology ontology) {
		ontologyDao.add(ontology);
		
	}

	
	public void update(Ontology ontology) {
		ontologyDao.update(ontology);
	}

	
	public void delete(Ontology ontology) {
		ontologyDao.delete(ontology);
	}

	
	public Ontology findOntologyById(String id) {
		return ontologyDao.findOntologyById(id);
	}
	
	
}
