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
	
	private Long frequency;
	private Long cardinality1;
	private Long cardinality2;
	private Long cardinality3;
	private Long cardinality4;
	private Long cardinality5;
	private Long cardinality6;
	
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
	
	
	public Long getFrequency() { return frequency; }
	public void setFrequency(Long frequency) { this.frequency = frequency; }
	
	public Long getCardinality1() { return cardinality1; }
	public void setCardinality1(Long cardinality1) { this.cardinality1 = cardinality1; }
	
	public Long getCardinality2() { return cardinality2; }
	public void setCardinality2(Long cardinality2) { this.cardinality2 = cardinality2; }
	
	public Long getCardinality3() { return cardinality3; }
	public void setCardinality3(Long cardinality3) { this.cardinality3 = cardinality3; }
	
	public Long getCardinality4() { return cardinality4; }
	public void setCardinality4(Long cardinality4) { this.cardinality4 = cardinality4; }
	
	public Long getCardinality5() { return cardinality5; }
	public void setCardinality5(Long cardinality5) { this.cardinality5 = cardinality5; }
	
	public Long getCardinality6() { return cardinality6; }
	public void setCardinality6(Long cardinality6) { this.cardinality6 = cardinality6; }
}
