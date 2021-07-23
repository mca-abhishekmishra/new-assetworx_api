package com.softtek.assetworx_api.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softtek.assetworx_api.entity.LicenseType;
import com.softtek.assetworx_api.repository.LicenseTypeRepository;
import com.softtek.assetworx_api.service.LicenseTypeService;

@Service
public class LicenseTypeServiceImpl implements LicenseTypeService {
	
	@Autowired
	LicenseTypeRepository licenseTypeRepository;

	@Override
	public LicenseType findById(String id) {
		return licenseTypeRepository.findById(id).orElse(null);
	}

	@Override
	public LicenseType findByName(String name) {
		return licenseTypeRepository.findByName(name).orElse(null);
	}

}
