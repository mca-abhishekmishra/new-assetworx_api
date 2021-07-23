package com.softtek.assetworx_api.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softtek.assetworx_api.entity.ProjectWbsId;
import com.softtek.assetworx_api.service.ProjectWbsIdService;

@Service
public class ProjectWbsIdServiceImpl implements ProjectWbsIdService {

	@Autowired
	ProjectWbsIdRepository projectWbsIdRepository; 
	
	@Override
	public ProjectWbsId findById(String id) {
		return projectWbsIdRepository.findById(id).orElse(null);
	}

}
