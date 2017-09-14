package com.dao;

import java.util.List;

import com.model.Dataset;

public interface DatasetDao {
	
	public List<Dataset> listDataset();
	
	public void add(Dataset dataset);
	
	public void update(Dataset dataset);
	
	public void delete(Dataset dataset);
	
	public Dataset findDatasetById(String id);
	
}

