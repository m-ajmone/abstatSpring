package com.repository;

import org.springframework.data.solr.repository.SolrCrudRepository;

import com.model.ResourceSolr;

public interface ResourceRepo extends SolrCrudRepository<ResourceSolr, Long>{


}
