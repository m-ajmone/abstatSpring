package com.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dao.DatasetDao;
import com.model.Dataset;

@Service
public class DatasetServiceImpl implements DatasetService{
	
	@Autowired
	DatasetDao datasetDao;

	
	public List<Dataset> listDataset() {
		return datasetDao.listDataset();
	}

	
	public void add(Dataset dataset) {
		datasetDao.add(dataset);
	}

	
	public void update(Dataset dataset) {
		datasetDao.update(dataset);
		
	}

	
	public void delete(Dataset dataset) {
		datasetDao.delete(dataset);
		
	}

	
	public Dataset findDatasetById(String id) {
		
		return datasetDao.findDatasetById(id);
	}
	
	

}
