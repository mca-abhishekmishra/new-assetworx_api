package com.softtek.assetworx_api.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softtek.assetworx_api.entity.AssetAssignmentType;
import com.softtek.assetworx_api.repository.AssetAssignmentTypeRepository;
import com.softtek.assetworx_api.service.AssetAssignmentTypeService;

@Service
public class AssetAssignmentTypeServiceImpl implements AssetAssignmentTypeService {
	
	@Autowired
	AssetAssignmentTypeRepository assetAssignmentTypeRepository;

	@Override
	public AssetAssignmentType findById(String id) {
		return assetAssignmentTypeRepository.findById(id).orElse(null);
	}

	@Override
	public AssetAssignmentType findByName(String name) {
		return assetAssignmentTypeRepository.findByName(name).orElse(null);
	}

}
