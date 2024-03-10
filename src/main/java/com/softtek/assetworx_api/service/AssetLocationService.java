package com.softtek.assetworx_api.service;

import com.softtek.assetworx_api.entity.AssetLocation;

public interface AssetLocationService {
	
	AssetLocation findById(String id);
	
	AssetLocation findByName(String name);

}
