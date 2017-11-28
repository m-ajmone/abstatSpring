package com.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Resource {

	@Id
	private String Id;
	private String localURL;
	private String seeAlso;
	private String type;
	private String subType;
	private String subConceptOf;
	
	private String datasetOfOrigin;
	private List<String> ontologiesOfOrigin;
	private String summary_conf;
	
	private long frequency;
	private long cardinality1;
	private long cardinality2;
	private long cardinality3;
	private long cardinality4;
	private long cardinality5;
	private long cardinality6;
	
	public String getId() { return Id; }
	public void setId(String Id) { this.Id = Id; }
	
	public String getLocalURL() { return localURL; }
	public void setLocalUrl(String localURL) { this.localURL = localURL; }
	
	public String getSeeAlso() { return seeAlso; }
	public void setSeeAlso(String seeAlso) { this.seeAlso = seeAlso; }
	
	public String getType() { return type; }
	public void setType(String type) { this.type = type; }
	
	public String getSubType() { return subType; }
	public void setSubType(String subType) { this.subType = subType; }
	
	public String getSubConceptOf() { return subConceptOf; }
	public void setSubConceptOf(String subConceptOf) { this.subConceptOf = subConceptOf; }
	
	public String getDatasetOfOrigin() { return datasetOfOrigin; }
	public void setDatasetOfOrigin(String datasetOfOrigin) { this.datasetOfOrigin = datasetOfOrigin; }
	
	public List<String> getOntologiesOfOrigind() { return ontologiesOfOrigin; }
	public void setOntologiesOfOrigin(List<String> ontologiesOfOrigin) { this.ontologiesOfOrigin = ontologiesOfOrigin; }
	
	public String getSummary_conf() { return summary_conf; }
	public void setSummary_conf(String summary_conf) { this.summary_conf = summary_conf; }
	
	
	public long getFrequency() { return frequency; }
	public void setFrequency(long frequency) { this.frequency = frequency; }
	
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
