package com.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dao.SubmitConfigDao;
import com.model.SubmitConfig;

@Service
public class SubmitConfigServiceImpl implements SubmitConfigService {

	@Autowired
	SubmitConfigDao submitConfigDao;

	public List<SubmitConfig> listSubmitConfig() {
		return submitConfigDao.listSubmitConfig();
	}

	public void add(SubmitConfig submitConfig) {
		submitConfigDao.add(submitConfig);
		
	}

	public void delete(SubmitConfig submitConfig) {
		submitConfigDao.delete(submitConfig);
		
	}

	public SubmitConfig findSubmitConfigById(String id) {
		return submitConfigDao.findSubmitConfigById(id);
	}

	public void update(SubmitConfig submitConfig) {
		submitConfigDao.update(submitConfig);
	}
}