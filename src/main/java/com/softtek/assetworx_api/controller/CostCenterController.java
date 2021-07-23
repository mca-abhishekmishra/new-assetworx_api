package com.softtek.assetworx_api.controller;

import static com.softtek.assetworx_api.util.Constants.COSTCENTER_DELETED;
import static com.softtek.assetworx_api.util.Constants.COSTCENTER_NOTDELETED;
import static com.softtek.assetworx_api.util.Constants.COSTCENTER_NOTFOUND_ID;
import static com.softtek.assetworx_api.util.Constants.COSTCENTER_NOTFOUND_NAME;
import static com.softtek.assetworx_api.util.Constants.COSTCENTER_NOTSAVED;
import static com.softtek.assetworx_api.util.Constants.COSTCENTER_NOTUPDATED;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.softtek.assetworx_api.entity.CostCenter;
import com.softtek.assetworx_api.exception.GenericRestException;
import com.softtek.assetworx_api.service.CostCenterService;

@RestController
@RequestMapping("/costCenter")
public class CostCenterController {

	@Autowired
	CostCenterService costCenterService;


	@GetMapping("/{id}")
	private ResponseEntity<?> findById(@PathVariable("id") String id) {
		CostCenter costCenter = costCenterService.findById(id);
		if(costCenter!=null) {
			return new ResponseEntity<CostCenter>(costCenter, HttpStatus.OK);
		}
		throw new GenericRestException(COSTCENTER_NOTFOUND_ID + id, HttpStatus.NOT_FOUND);	
	}

	@GetMapping("/findByName/{name}")
	private ResponseEntity<?> findByName(@PathVariable("name") String name) {
		CostCenter costCenter = costCenterService.findByName(name);
		if(costCenter!=null) {
			return new ResponseEntity<CostCenter>(costCenter, HttpStatus.OK);
		}
		throw new GenericRestException(COSTCENTER_NOTFOUND_NAME + name, HttpStatus.NOT_FOUND);	
	}
	
	@PostMapping("/")
	private ResponseEntity<?> save(@RequestBody CostCenter costCenter) {
		System.out.println(costCenter);
		CostCenter createdcostCenter = costCenterService.save(costCenter);
		if(createdcostCenter != null) {
			return new ResponseEntity<CostCenter>(createdcostCenter, HttpStatus.CREATED);
		}
		throw new GenericRestException(COSTCENTER_NOTSAVED, HttpStatus.BAD_REQUEST);	
	} 
	
	@PutMapping("/")
	private ResponseEntity<?> update(@RequestBody CostCenter costCenter) {
		CostCenter updatedcostCenter = costCenterService.update(costCenter);
		if(updatedcostCenter != null) {
			return new ResponseEntity<CostCenter>(updatedcostCenter, HttpStatus.OK);
		}
		throw new GenericRestException(COSTCENTER_NOTUPDATED, HttpStatus.BAD_REQUEST);	
	}
	
	@DeleteMapping("/{id}")
	private ResponseEntity<?> delete(@PathVariable("id") String id) {
		if(costCenterService.delete(id)) {
			return new ResponseEntity<String>(COSTCENTER_DELETED, HttpStatus.OK);
		}
		throw new GenericRestException(COSTCENTER_NOTDELETED, HttpStatus.BAD_REQUEST);	
	}
	
}
