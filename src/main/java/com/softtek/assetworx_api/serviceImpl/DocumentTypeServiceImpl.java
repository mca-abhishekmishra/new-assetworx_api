package com.softtek.assetworx_api.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softtek.assetworx_api.entity.DocumentType;
import com.softtek.assetworx_api.repository.DocumentTypeRepository;
import com.softtek.assetworx_api.service.DocumentTypeService;

@Service
public class DocumentTypeServiceImpl implements DocumentTypeService {
	
	@Autowired
	DocumentTypeRepository documentTypeRepository;

	@Override
	public DocumentType findById(String id) {
		return documentTypeRepository.findById(id).orElse(null);
	}

	@Override
	public DocumentType findByName(String name) {
		return documentTypeRepository.findByName(name).orElse(null);
	}

}
