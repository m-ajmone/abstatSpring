package com.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class AKP {
	
	@Id
	private String Id;
	private String URL;
	private String subject;
	private String predicate;
	private String object;
	private String patternType;
	private String type;
	private String subType;
	
	private String datasetOfOrigin;
	private List<String> ontologiesOfOrigin;
	private String summary_conf;
	
	private long frequency;
	private long numberOfInstances;
	private long cardinality1;
	private long cardinality2;
	private long cardinality3;
	private long cardinality4;
	private long cardinality5;
	private long cardinality6;
	
	public String getId() { return Id; }
	public void setId(String Id) { this.Id = Id; }
	
	public String getURL() { return URL; }
	public void setURL(String URL) { this.URL = URL; }
	
	public String getSubject() { return subject; }
	public void setSubject(String subject) { this.subject = subject; }
	
	public String getPredicate() { return predicate; }
	public void setPredicate(String predicate) { this.predicate = predicate; }
	
	public String getObject() { return object; }
	public void setObject(String object) { this.object = object; }
	
	public String getPatternType() { return patternType; }
	public void setPatternType(String patternType) { this.patternType = patternType; }
	
	public String getType() { return type; }
	public void setType(String type) { this.type = type; }
	
	public String getSubType() { return subType; }
	public void setSubType(String subType) { this.subType = subType; }
	
	public String getDatasetOfOrigin() { return datasetOfOrigin; }
	public void setDatasetOfOrigin(String datasetOfOrigin) { this.datasetOfOrigin = datasetOfOrigin; }
	
	public List<String> getOntologiesOfOrigind() { return ontologiesOfOrigin; }
	public void setOntologiesOfOrigin(List<String> ontologiesOfOrigin) { this.ontologiesOfOrigin = ontologiesOfOrigin; }
	
	public String getSummary_conf() { return summary_conf; }
	public void setSummary_conf(String summary_conf) { this.summary_conf = summary_conf; }	
	
	
	public long getFrequency() { return frequency; }
	public void setFrequency(long frequency) { this.frequency = frequency; }
	
	public long getNumberOfInstances() { return numberOfInstances; }
	public void setNumberOfInstances(long numberOfInstances) { this.numberOfInstances = numberOfInstances; }
	
	public long getCardinality1() { return cardinality1; }
	public void setCardinality1(long cardinality1) { this.cardinality1 = cardinality1; }
	
	public long getCardinality2() { return cardinality2; }
	public void setCardinality2(long cardinality2) { this.cardinality2 = cardinality2; }
	
	public long getCardinality3() { return cardinality3; }
	public void setCardinality3(long cardinality3) { this.cardinality3 = cardinality3; }
	
	public long getCardinality4() { return cardinality4; }
	public void setCardinality4(long cardinality4) { this.cardinality4 = cardinality4; }
	
	public long getCardinality5() { return cardinality5; }
	public void setCardinality5(long cardinality5) { this.cardinality5 = cardinality5; }
	
	public long getCardinality6() { return cardinality6; }
	public void setCardinality6(long cardinality6) { this.cardinality6 = cardinality6; }

}

