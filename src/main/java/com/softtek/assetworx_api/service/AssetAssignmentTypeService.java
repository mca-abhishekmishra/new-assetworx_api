package com.softtek.assetworx_api.service;

import com.softtek.assetworx_api.entity.AssetAssignmentType;

public interface AssetAssignmentTypeService {
	
	AssetAssignmentType findById(String id);
	
	AssetAssignmentType findByName(String name);

}
