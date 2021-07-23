package com.softtek.assetworx_api.service;

import com.softtek.assetworx_api.entity.AssetType;

public interface AssetTypeService {
	
	AssetType findById(String id);
	
	AssetType findByName(String name);
	
	AssetType save(AssetType assetType);

	AssetType update(AssetType assetType);

	boolean delete(String id);

}
