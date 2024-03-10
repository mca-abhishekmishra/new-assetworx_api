package com.softtek.assetworx_api.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softtek.assetworx_api.entity.AssetLocation;
import com.softtek.assetworx_api.repository.AssetLocationRepository;
import com.softtek.assetworx_api.service.AssetLocationService;

@Service
public class AssetLocationServiceImpl implements AssetLocationService {
	
	@Autowired
	AssetLocationRepository assetLocationRepository;

	@Override
	public AssetLocation findById(String id) {
		return assetLocationRepository.findById(id).orElse(null);
	}

	@Override
	public AssetLocation findByName(String name) {
		return assetLocationRepository.findByName(name).orElse(null);
	}

}
