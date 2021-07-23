package com.softtek.assetworx_api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.softtek.assetworx_api.entity.Workstation;
import com.softtek.assetworx_api.exception.GenericRestException;
import com.softtek.assetworx_api.service.WorkStationService;

@RestController
@RequestMapping("/workstation")
public class WorkstationController {
	
	@Autowired
	WorkStationService workStationService;
	
	
	@GetMapping("/list")
	private List<Workstation> list() {
		return workStationService.findAllByActive(true);
	}

	@GetMapping("/{id}")
	private ResponseEntity<?> findById(@PathVariable("id") String id) {
		Workstation workstation = workStationService.findById(id);
		if(workstation!=null) {
			return new ResponseEntity<Workstation>(workstation, HttpStatus.OK);
		}
		throw new GenericRestException("Details could not be fetched.", HttpStatus.NOT_FOUND);	
	}
}
