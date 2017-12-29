package com.repository;

import org.springframework.data.solr.repository.SolrCrudRepository;
import org.springframework.stereotype.Component;

import com.model.AKPSolr;


@Component
public interface AKPRepo extends SolrCrudRepository<AKPSolr, Long>{


}
