package com.model;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.SolrDocument;

@SolrDocument(solrCoreName = "indexing")
public class ResourceSolr {
	
	@Id
	@Field
	private String idDocument;
	
	@Field
	private long occurrence;
	
	@Field
	private String[] URI;
	
	@Field
	private String dataset;
	
	@Field
	private String subtype;
	
//	@Field
//	private String text;
	
	@Field
	private String type;
	
	@Field
	private String[] fullTextSearchField;
	 
	@Field
	private String[] label_ngram;
	
	@Field
	private String[] label_edgeNgram;
	

	public String getIdDocument() { return idDocument; }
	public void setIdDocument(String idDocument) { this.idDocument = idDocument; }

	public long getOccurrence() { return occurrence; }
	public void setOccurrence(long occurrence) { this.occurrence = occurrence; }

	public String[] getURI() { return URI; }
	public void setURI(String[] URI) { this.URI = URI; }

	public String getDataset() { return dataset; }
	public void setDataset(String dataset) { this.dataset = dataset; }

	public String getSubtype() { return subtype; }
	public void setSubtype(String subtype) { this.subtype = subtype; }

//	public String getText() { return text; }
//	public void setText(String text) { this.text = text; }

	public String getType() { return type; }
	public void setType(String type) { this.type = type; }

	public String[] fetFullTextSearchField() { return fullTextSearchField; }
	public void setFullTextSearchField(String[] fullTextSearchField) {this.fullTextSearchField = fullTextSearchField; }

	public String[] getLabel_ngram() { return label_ngram; }
	public void setLabel_ngram(String[] label_ngram) { this.label_ngram = label_ngram; }

	public String[] getLabel_edgeNgram() { return label_edgeNgram; }
	public void setLabel_edgeNgram(String[] label_edgeNgram) { this.label_edgeNgram = label_edgeNgram; }
	
	
	
}
