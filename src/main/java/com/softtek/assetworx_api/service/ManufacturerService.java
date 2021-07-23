package com.softtek.assetworx_api.service;

import com.softtek.assetworx_api.entity.Manufacturer;

public interface ManufacturerService {
	
	Manufacturer findById(String id);
	
	Manufacturer findByName(String name);
	
	Manufacturer save(Manufacturer floor);

	Manufacturer update(Manufacturer floor);

	boolean delete(String id);

}
