package com.model;

public class IndexSummary {

	private String idSummary;
	private boolean indexMongoDB;
	private boolean indexSolr;
	
	public String getIdSummary() { return idSummary; }
	public void setIdSummary(String idSummary) { this.idSummary = idSummary; }
	
	public boolean getIndexMongoDB() { return indexMongoDB; }
	public void setIndexMongoDB(boolean indexMongoDB) { this.indexMongoDB = indexMongoDB; }
	
	public boolean getIndexSolr() { return indexSolr; }
	public void setIndexSolr(boolean indexSolr) { this.indexSolr = indexSolr; }	
}
