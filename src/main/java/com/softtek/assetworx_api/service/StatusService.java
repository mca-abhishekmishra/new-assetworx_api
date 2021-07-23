package com.softtek.assetworx_api.service;

import java.util.List;

import com.softtek.assetworx_api.entity.Status;

public interface StatusService {
	
	Status findById(String id);
	
	Status findByName(String name);

	List<Status> findAllByNameIn(String[] strings);

}
