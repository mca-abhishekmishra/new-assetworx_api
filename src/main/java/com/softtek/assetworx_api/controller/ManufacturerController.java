package com.softtek.assetworx_api.controller;

import static com.softtek.assetworx_api.util.Constants.*;

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

import com.softtek.assetworx_api.entity.Manufacturer;
import com.softtek.assetworx_api.exception.GenericRestException;
import com.softtek.assetworx_api.service.ManufacturerService;

@RestController
@RequestMapping("/manufacturer")
public class ManufacturerController {

	@Autowired
	ManufacturerService manufacturerService;

	@GetMapping("/{id}")
	private ResponseEntity<?> findById(@PathVariable("id") String id) {
		Manufacturer manufacturer = manufacturerService.findById(id);
		if (manufacturer != null) {
			return new ResponseEntity<Manufacturer>(manufacturer, HttpStatus.OK);
		}
		throw new GenericRestException(ASSET_MODEL_NOTFOUND_ID + id, HttpStatus.NOT_FOUND);
	}

	@GetMapping("/findByName/{name}")
	private ResponseEntity<?> findByName(@PathVariable("name") String name) {
		Manufacturer manufacturer = manufacturerService.findByName(name);
		if (manufacturer != null) {
			return new ResponseEntity<Manufacturer>(manufacturer, HttpStatus.OK);
		}
		throw new GenericRestException(ASSET_MODEL_NOTFOUND_NAME + name, HttpStatus.NOT_FOUND);
	}

	@PostMapping("/")
	private ResponseEntity<?> save(@RequestBody Manufacturer manufacturer) {
		System.out.println(manufacturer);
		Manufacturer createdManufacturer = manufacturerService.save(manufacturer);
		if (createdManufacturer != null) {
			return new ResponseEntity<Manufacturer>(createdManufacturer, HttpStatus.CREATED);
		}
		throw new GenericRestException(ASSET_MODEL_NOTSAVED, HttpStatus.BAD_REQUEST);
	}

	@PutMapping("/")
	private ResponseEntity<?> update(@RequestBody Manufacturer manufacturer) {
		Manufacturer updatedManufacturer = manufacturerService.update(manufacturer);
		if (updatedManufacturer != null) {
			return new ResponseEntity<Manufacturer>(updatedManufacturer, HttpStatus.OK);
		}
		throw new GenericRestException(ASSET_MODEL_NOTUPDATED, HttpStatus.BAD_REQUEST);
	}

	@DeleteMapping("/{id}")
	private ResponseEntity<?> delete(@PathVariable("id") String id) {
		if (manufacturerService.delete(id)) {
			return new ResponseEntity<String>(ASSET_MODEL_DELETED, HttpStatus.OK);
		}
		throw new GenericRestException(ASSET_MODEL_NOTDELETED, HttpStatus.BAD_REQUEST);
	}

}
