package com.softtek.assetworx_api.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softtek.assetworx_api.entity.Division;
import com.softtek.assetworx_api.repository.DivisionRepository;
import com.softtek.assetworx_api.service.DivisionService;

@Service
public class DivisionServiceImpl implements DivisionService {
	
	@Autowired
	DivisionRepository divisionRepository;

	@Override
	public Division findById(String id) {
		return divisionRepository.findById(id).orElse(null);
	}

}
