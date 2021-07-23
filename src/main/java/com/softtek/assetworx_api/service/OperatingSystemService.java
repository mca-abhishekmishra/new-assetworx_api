package com.softtek.assetworx_api.service;

import com.softtek.assetworx_api.entity.OperatingSystem;

public interface OperatingSystemService {
	
	OperatingSystem findById(String id);
	
	OperatingSystem findByName(String name);
	
	OperatingSystem save(OperatingSystem os);

	OperatingSystem update(OperatingSystem os);

	boolean delete(String id);

}
