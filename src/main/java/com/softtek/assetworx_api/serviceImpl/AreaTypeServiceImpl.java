package com.softtek.assetworx_api.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softtek.assetworx_api.entity.AreaType;
import com.softtek.assetworx_api.repository.AreaTypeRepository;
import com.softtek.assetworx_api.service.AreaTypeService;

@Service
public class AreaTypeServiceImpl implements AreaTypeService {
	
	@Autowired
	AreaTypeRepository areaTypeRepository;

	@Override
	public AreaType findById(String id) {
		return areaTypeRepository.findById(id).orElse(null);
	}

	@Override
	public AreaType findByName(String name) {
		return areaTypeRepository.findByName(name).orElse(null);
	}

}
