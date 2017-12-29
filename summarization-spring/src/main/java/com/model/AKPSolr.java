package com.model;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.SolrDocument;

@SolrDocument(solrCoreName = "AKPs")
public class AKPSolr {
	
	@Id
	@Field
	private String idDocument;
	
	@Field
	private String[] URI;
	
	@Field
	private String type;
	
	@Field
	private String dataset;
	
	@Field
	private String subtype;
	
//	@Field
//	private String text;
	
	
	@Field
	private String[] fullTextSearchField;
	
	@Field
	private String subject;
	
	@Field
	private String predicate;
	
	@Field
	private String object;
	
	@Field
	private String subject_ngram;
	
	@Field
	private String predicate_ngram;
	
	@Field
	private String object_ngram;
	
	@Field
	private String subject_edgeNgram;
	
	@Field
	private String  predicate_edgeNgram;
	
	@Field
	private String object_edgeNgram;
	
	@Field
	private long occurrence;
	
	@Field
	private long subjectFreq;
	
	@Field
	private long predicateFreq;
	
	@Field
	private long objectFreq;
	
	@Field
	private String subject_plus_dataset;
	
	@Field
	private String predicate_plus_dataset;
	
	@Field
	private String object_plus_dataset;

	
	public String getIdDocument() { return idDocument; } 
	public void setIdDocument(String idDocument) { this.idDocument = idDocument; } 

	public String[] getURI() { return URI; } 
	public void setURI(String[] URI) { this.URI = URI; } 

	public long getOccurence() { return occurrence; }
	public void setOCcurrence(long occurrence) { this.occurrence = occurrence; }
	
	public String getDataset() { return dataset; } 
	public void setDataset(String dataset) { this.dataset = dataset; } 

	public String getSubtype() { return subtype; } 
	public void setSubtype(String subtype) { this.subtype = subtype; } 

//	public String getText() { return text; } 
//	public void setText(String text) { this.text = text; } 

	public String getType() { return type; } 
	public void setType(String type) { this.type = type; } 

	public String[] getFullTextSearchField() { return fullTextSearchField; } 
	public void setFullTextSearchField(String[] fullTextSearchField) { this.fullTextSearchField = fullTextSearchField; } 

	public String getSubject() { return subject; } 
	public void setSubject(String subject) { this.subject = subject; } 

	public String getPredicate() { return predicate; } 
	public void setPredicate(String predicate) { this.predicate = predicate; } 

	public String getObject() { return object; } 
	public void setObject(String object) { this.object = object; } 

	public String getSubject_ngram() { return subject_ngram; } 
	public void setSubject_ngram(String subject_ngram) { this.subject_ngram = subject_ngram; } 

	public String getPredicate_ngram() { return predicate_ngram; } 
	public void setPredicate_ngram(String predicate_ngram) { this.predicate_ngram = predicate_ngram; } 

	public String getObject_ngram() { return object_ngram; } 
	public void setObject_ngram(String object_ngram) { this.object_ngram = object_ngram; } 

	public String getSubject_edgeNgram() { return subject_edgeNgram; } 
	public void setSubject_edgeNgram(String subject_edgeNgram) { this.subject_edgeNgram = subject_edgeNgram; } 

	public String getPredicate_edgeNgram() { return predicate_edgeNgram; } 
	public void setPredicate_edgeNgram(String predicate_edgeNgram) { this.predicate_edgeNgram = predicate_edgeNgram; } 

	public String getObject_edgeNgram() { return object_edgeNgram; } 
	public void setObject_edgeNgram(String object_edgeNgram) { this.object_edgeNgram = object_edgeNgram; } 

	public long getSubjectFreq() { return subjectFreq; } 
	public void setSubjectFreq(long subjectFreq) { this.subjectFreq = subjectFreq; } 

	public long getPredicateFreq() { return predicateFreq; } 
	public void setPredicateFreq(long predicateFreq) { this.predicateFreq = predicateFreq; } 

	public long getObjectFreq() { return objectFreq; } 
	public void setObjectFreq(long objectFreq) { this.objectFreq = objectFreq; } 

	public String getSubject_plus_dataset() { return subject_plus_dataset; } 
	public void setSubject_plus_dataset(String subject_plus_dataset) { this.subject_plus_dataset = subject_plus_dataset; } 

	public String getPredicate_plus_dataset() { return predicate_plus_dataset; } 
	public void setPredicate_plus_dataset(String predicate_plus_dataset) { this.predicate_plus_dataset = predicate_plus_dataset; } 

	public String getObject_plus_dataset() { return object_plus_dataset; } 
	public void setObject_plus_dataset(String object_plus_dataset) { this.object_plus_dataset = object_plus_dataset; } 
	
}
