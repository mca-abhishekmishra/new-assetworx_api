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

import com.softtek.assetworx_api.entity.OperatingSystem;
import com.softtek.assetworx_api.exception.GenericRestException;
import com.softtek.assetworx_api.service.OperatingSystemService;

@RestController
@RequestMapping("/operatingSystem")
public class OperatingSystemController {

	@Autowired
	OperatingSystemService operatingSystemService;

	@GetMapping("/{id}")
	private ResponseEntity<?> findById(@PathVariable("id") String id) {
		OperatingSystem operatingSystem = operatingSystemService.findById(id);
		if (operatingSystem != null) {
			return new ResponseEntity<OperatingSystem>(operatingSystem, HttpStatus.OK);
		}
		throw new GenericRestException(OS_NOTFOUND_ID + id, HttpStatus.NOT_FOUND);
	}

	@GetMapping("/findByName/{name}")
	private ResponseEntity<?> findByName(@PathVariable("name") String name) {
		OperatingSystem operatingSystem = operatingSystemService.findByName(name);
		if (operatingSystem != null) {
			return new ResponseEntity<OperatingSystem>(operatingSystem, HttpStatus.OK);
		}
		throw new GenericRestException(OS_NOTFOUND_NAME + name, HttpStatus.NOT_FOUND);
	}

	@PostMapping("/")
	private ResponseEntity<?> save(@RequestBody OperatingSystem operatingSystem) {
		System.out.println(operatingSystem);
		OperatingSystem createdOperatingSystem = operatingSystemService.save(operatingSystem);
		if (createdOperatingSystem != null) {
			return new ResponseEntity<OperatingSystem>(createdOperatingSystem, HttpStatus.CREATED);
		}
		throw new GenericRestException(OS_NOTSAVED, HttpStatus.BAD_REQUEST);
	}

	@PutMapping("/")
	private ResponseEntity<?> update(@RequestBody OperatingSystem operatingSystem) {
		OperatingSystem updatedOperatingSystem = operatingSystemService.update(operatingSystem);
		if (updatedOperatingSystem != null) {
			return new ResponseEntity<OperatingSystem>(updatedOperatingSystem, HttpStatus.OK);
		}
		throw new GenericRestException(OS_NOTUPDATED, HttpStatus.BAD_REQUEST);
	}

	@DeleteMapping("/{id}")
	private ResponseEntity<?> delete(@PathVariable("id") String id) {
		if (operatingSystemService.delete(id)) {
			return new ResponseEntity<String>(OS_DELETED, HttpStatus.OK);
		}
		throw new GenericRestException(OS_NOTDELETED, HttpStatus.BAD_REQUEST);
	}

}
