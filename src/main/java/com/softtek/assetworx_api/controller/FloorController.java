package com.softtek.assetworx_api.controller;

import static com.softtek.assetworx_api.util.Constants.FLOOR_DELETED;
import static com.softtek.assetworx_api.util.Constants.FLOOR_NOTDELETED;
import static com.softtek.assetworx_api.util.Constants.FLOOR_NOTFOUND_ID;
import static com.softtek.assetworx_api.util.Constants.FLOOR_NOTFOUND_NAME;
import static com.softtek.assetworx_api.util.Constants.FLOOR_NOTSAVED;
import static com.softtek.assetworx_api.util.Constants.FLOOR_NOTUPDATED;

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

import com.softtek.assetworx_api.entity.Floor;
import com.softtek.assetworx_api.exception.GenericRestException;
import com.softtek.assetworx_api.service.FloorService;

@RestController
@RequestMapping("/floor")
public class FloorController {

	@Autowired
	FloorService floorService;


	@GetMapping("/{id}")
	private ResponseEntity<?> findById(@PathVariable("id") String id) {
		Floor floor = floorService.findById(id);
		if(floor!=null) {
			return new ResponseEntity<Floor>(floor, HttpStatus.OK);
		}
		throw new GenericRestException(FLOOR_NOTFOUND_ID + id, HttpStatus.NOT_FOUND);	
	}

	@GetMapping("/findByName/{name}")
	private ResponseEntity<?> findByName(@PathVariable("name") String name) {
		Floor floor = floorService.findByName(name);
		if(floor!=null) {
			return new ResponseEntity<Floor>(floor, HttpStatus.OK);
		}
		throw new GenericRestException(FLOOR_NOTFOUND_NAME + name, HttpStatus.NOT_FOUND);	
	}
	
	@PostMapping("/")
	private ResponseEntity<?> save(@RequestBody Floor floor) {
		System.out.println(floor);
		Floor createdfloor = floorService.save(floor);
		if(createdfloor != null) {
			return new ResponseEntity<Floor>(createdfloor, HttpStatus.CREATED);
		}
		throw new GenericRestException(FLOOR_NOTSAVED, HttpStatus.BAD_REQUEST);	
	} 
	
	@PutMapping("/")
	private ResponseEntity<?> update(@RequestBody Floor floor) {
		Floor updatedfloor = floorService.update(floor);
		if(updatedfloor != null) {
			return new ResponseEntity<Floor>(updatedfloor, HttpStatus.OK);
		}
		throw new GenericRestException(FLOOR_NOTUPDATED, HttpStatus.BAD_REQUEST);	
	}
	
	@DeleteMapping("/{id}")
	private ResponseEntity<?> delete(@PathVariable("id") String id) {
		if(floorService.delete(id)) {
			return new ResponseEntity<String>(FLOOR_DELETED, HttpStatus.OK);
		}
		throw new GenericRestException(FLOOR_NOTDELETED, HttpStatus.BAD_REQUEST);	
	}
	
}
