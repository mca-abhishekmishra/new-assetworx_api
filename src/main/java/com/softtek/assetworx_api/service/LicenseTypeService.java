package com.softtek.assetworx_api.service;

import com.softtek.assetworx_api.entity.LicenseType;

public interface LicenseTypeService {
	
	LicenseType findById(String id);
	
	LicenseType findByName(String name);

}
