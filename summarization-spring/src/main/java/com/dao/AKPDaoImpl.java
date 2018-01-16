package com.dao;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
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
	
	public List<AKP> list(String dataset, String subj, String pred, String obj) {
		String[][] array = new String[4][2];
		array[0][0] = "";
		array[1][0] = "" ;
		array[2][0] = "";
		array[3][0] = "" ;
		
		if(dataset!=null) {
			array[0][0] = "datasetOfOrigin";
			array[0][1] = dataset;
		}
		if(subj!=null) {
			array[1][0] = "subject" ;
			array[1][1] = subj;
		}
		if(pred!=null) {
			array[2][0] = "predicate";
			array[2][1] = pred;
		}
		if(obj!=null) {
			array[3][0] = "object" ;
			array[3][1] = obj;
		}
		
		Query query = new Query();
		query.addCriteria(new Criteria().andOperator(Criteria.where(array[0][0]).is(array[0][1]),
													 Criteria.where(array[1][0]).is(array[1][1]),
													 Criteria.where(array[2][0]).is(array[2][1]),
													 Criteria.where(array[3][0]).is(array[3][1])));
		query.with(new Sort(Sort.Direction.DESC, "frequency"));
		return mongoTemplate.find(query, AKP.class, COLLECTION_NAME);
	}
	
	
	public List<String> getSPOlist(String dataset, String position){
		Query query = new Query();	
		if(dataset!=null)
			query.addCriteria(Criteria.where("datasetOfOrigin").is(dataset));
		
		List<String> coll = mongoTemplate.getCollection(COLLECTION_NAME).distinct(position, query.getQueryObject());
		return coll;	
	}
}
