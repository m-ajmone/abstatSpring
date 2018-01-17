package com.dao;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.model.Resource;

@Repository
public class ResourceDaoImpl implements ResourceDao {

	@Autowired
	MongoTemplate mongoTemplate;
	
	private static final String COLLECTION_NAME = "resources";
	
	public void add(Resource resource) {
		if(!mongoTemplate.collectionExists(COLLECTION_NAME)) {
			mongoTemplate.createCollection(COLLECTION_NAME);
		}
		
		resource.setId(UUID.randomUUID().toString());
		mongoTemplate.insert(resource, COLLECTION_NAME);
		
	}


	public void update(Resource resource) {
		mongoTemplate.save(resource, COLLECTION_NAME);
		
	}

	
	public void delete(Resource resource) {
		mongoTemplate.remove(resource, COLLECTION_NAME);
		
	}
	
	public Resource getResourceFromSummary(String globalURI, String summary) {
		Query query = new Query();
		query.addCriteria(new Criteria().andOperator(Criteria.where("seeAlso").is(globalURI), Criteria.where("summary_conf").is(summary)));
		return mongoTemplate.findOne(query, Resource.class, COLLECTION_NAME);
	}
}
