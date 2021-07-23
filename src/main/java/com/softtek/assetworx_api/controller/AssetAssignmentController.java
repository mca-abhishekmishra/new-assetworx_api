package com.softtek.assetworx_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.softtek.assetworx_api.entity.AssetAssignment;
import com.softtek.assetworx_api.exception.GenericRestException;
import com.softtek.assetworx_api.service.AssetAssignmentService;
import com.softtek.assetworx_api.service.AssetService;

@RestController
@RequestMapping("/assetAssignment")
public class AssetAssignmentController {

	@Autowired
	AssetAssignmentService assetAssignmentService;
	
	@Autowired
	AssetService assetService;
	
	@PostMapping("/assign")
	private ResponseEntity<?> assign(@RequestBody AssetAssignment a) {
		AssetAssignment assetAssignment = assetAssignmentService.assign(a);
		if(assetAssignment != null) {
			return new ResponseEntity<AssetAssignment>(assetAssignment, HttpStatus.CREATED);
		}
		throw new GenericRestException("Asset could not be assigned.", HttpStatus.BAD_REQUEST);	
	}
	
	@PutMapping("/unassign/")
	private ResponseEntity<?> unassign(@RequestBody AssetAssignment a) {
		AssetAssignment assetAssignment = assetAssignmentService.unassign(a);
		if(assetAssignment != null) {
			return new ResponseEntity<AssetAssignment>(assetAssignment, HttpStatus.OK);
		}
		throw new GenericRestException("Asset could not be unassigned.", HttpStatus.BAD_REQUEST);	
	}
	
	@PostMapping("/reassign")
	private ResponseEntity<?> reassign(@RequestBody AssetAssignment a) {
		AssetAssignment assetAssignment = assetAssignmentService.reassign(a);
		if(assetAssignment != null) {
			return new ResponseEntity<AssetAssignment>(assetAssignment, HttpStatus.CREATED);
		}
		throw new GenericRestException("Asset could not be re-assigned.", HttpStatus.BAD_REQUEST);	
	}
	
	@PutMapping("/")
	private ResponseEntity<?> update(@RequestBody AssetAssignment assetAssignment) {
		AssetAssignment updatedAssetAssignment = assetAssignmentService.update(assetAssignment);
		if(updatedAssetAssignment != null) {
			return new ResponseEntity<AssetAssignment>(updatedAssetAssignment, HttpStatus.OK);
		}
		throw new GenericRestException("Asset Assignment could not be updated.", HttpStatus.BAD_REQUEST);	
	}
}
