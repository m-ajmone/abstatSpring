package com.model;

public class IndexSummary {

	private String idSummary;
	private boolean loadOnMongoDB;
	private boolean indexOnSolr;
	
	public String getIdSummary() { return idSummary; }
	public void setIdSummary(String idSummary) { this.idSummary = idSummary; }
	
	public boolean getLoadOnMongoDB() { return loadOnMongoDB; }
	public void setLoadOnMongoDB(boolean indexMongoDB) { this.loadOnMongoDB = indexMongoDB; }
	
	public boolean getIndexOnSolr() { return indexOnSolr; }
	public void setIndexOnSolr(boolean indexSolr) { this.indexOnSolr = indexSolr; }	
}
