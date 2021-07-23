package com.softtek.assetworx_api.service;

import com.softtek.assetworx_api.entity.AreaType;

public interface AreaTypeService {
	
	AreaType findById(String id);
	
	AreaType findByName(String name);

}
