package com.softtek.assetworx_api.service;

import com.softtek.assetworx_api.entity.ContractType;

public interface ContractTypeService {
	
	ContractType findById(String id);
	
	ContractType findByName(String name);
	
	ContractType save(ContractType contractType);

	ContractType update(ContractType contractType);

	boolean delete(String id);

}
