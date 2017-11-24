package com.dao;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import com.model.AKP;

@Repository
public class AKPDaoImpl implements AKPDao{

	@Autowired
	MongoTemplate mongoTemplate;
	
	private static final String COLLECTION_NAME = "AKPs";
	
	public void add(AKP AKP) {
		if(!mongoTemplate.collectionExists(COLLECTION_NAME)) {
			mongoTemplate.createCollection(COLLECTION_NAME);
		}
		
		AKP.setId(UUID.randomUUID().toString());
		mongoTemplate.insert(AKP, COLLECTION_NAME);
		
	}


	public void update(AKP AKP) {
		mongoTemplate.save(AKP, COLLECTION_NAME);
		
	}

	
	public void delete(AKP AKP) {
		mongoTemplate.remove(AKP, COLLECTION_NAME);
		
	}
}
