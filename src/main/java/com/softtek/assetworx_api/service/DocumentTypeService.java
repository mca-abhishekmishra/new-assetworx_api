package com.softtek.assetworx_api.service;

import com.softtek.assetworx_api.entity.DocumentType;

public interface DocumentTypeService {
	
	DocumentType findById(String id);
	
	DocumentType findByName(String name);

}
