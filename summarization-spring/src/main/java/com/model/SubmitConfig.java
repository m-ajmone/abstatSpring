package com.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class SubmitConfig {

	@Id
	private String id;
	private String dsId;
	private List<String> listOntId;
	private String dsName;
	private String OntName;
	private String summaryPath;
	//options..
	private boolean tipoMinimo;
	private boolean inferences;
	private boolean cardinalita;
	private boolean propertyMinimaliz;
	
	
	public SubmitConfig() {
		super();
	}
	public SubmitConfig(String id, String dsId, ArrayList<String> listOntId) {
		super();
		this.id = id;
		this.dsId = dsId;
		this.listOntId = listOntId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDsId() {
		return dsId;
	}
	public void setDsId(String dsId) {
		this.dsId = dsId;
	}
	
	public void addOntId(String ontId) {
		listOntId.add(ontId);
	}
	public List<String> getListOntId() {
		return listOntId;
	}
	public void setListOntId(List<String> listOntId) {
		this.listOntId = listOntId;
	}
	public String getSummaryPath() {
		return summaryPath;
	}
	public void setSummaryPath(String summaryPath) {
		this.summaryPath = summaryPath;
	}
	public boolean isTipoMinimo() {
		return tipoMinimo;
	}
	public void setTipoMinimo(boolean tipoMinimo) {
		this.tipoMinimo = tipoMinimo;
	}
	public boolean isInferences() {
		return inferences;
	}
	public void setInferences(boolean inferences) {
		this.inferences = inferences;
	}
	public boolean isCardinalita() {
		return cardinalita;
	}
	public void setCardinalita(boolean cardinalita) {
		this.cardinalita = cardinalita;
	}
	public boolean isPropertyMinimaliz() {
		return propertyMinimaliz;
	}
	public void setPropertyMinimaliz(boolean propertyMinimaliz) {
		this.propertyMinimaliz = propertyMinimaliz;
	}
	public String getDsName() {
		return dsName;
	}
	public void setDsName(String dsName) {
		this.dsName = dsName;
	}
	public String getOntName() {
		return OntName;
	}
	public void setOntName(String ontName) {
		OntName = ontName;
	}
	
	
}
