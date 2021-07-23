package com.softtek.assetworx_api.serviceImpl;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softtek.assetworx_api.entity.Status;
import com.softtek.assetworx_api.repository.StatusRepository;
import com.softtek.assetworx_api.service.StatusService;

@Service
public class StatusServiceImpl implements StatusService {
	
	@Autowired
	StatusRepository statusRepository;

	@Override
	public Status findById(String id) {
		return statusRepository.findById(id).orElse(null);
	}

	@Override
	public Status findByName(String name) {
		return statusRepository.findByName(name).orElse(null);
	}

	@Override
	public List<Status> findAllByNameIn(String[] statusArray) {
		return statusRepository.findAllByNameIn(Arrays.asList(statusArray));
	}

}
