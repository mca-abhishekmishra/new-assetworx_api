package com.softtek.assetworx_api.controller;

import static com.softtek.assetworx_api.util.Constants.VENDOR_DELETED;
import static com.softtek.assetworx_api.util.Constants.VENDOR_NOTDELETED;
import static com.softtek.assetworx_api.util.Constants.VENDOR_NOTFOUND_ID;
import static com.softtek.assetworx_api.util.Constants.VENDOR_NOTSAVED;
import static com.softtek.assetworx_api.util.Constants.VENDOR_NOTUPDATED;

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

import com.softtek.assetworx_api.entity.Vendor;
import com.softtek.assetworx_api.exception.GenericRestException;
import com.softtek.assetworx_api.service.VendorService;

@RestController
@RequestMapping("/vendor")
public class VendorController {

	@Autowired
	VendorService vendorService;


	@GetMapping("/{id}")
	private ResponseEntity<?> findById(@PathVariable("id") String id) {
		Vendor vendor = vendorService.findById(id);
		if(vendor!=null) {
			return new ResponseEntity<Vendor>(vendor, HttpStatus.OK);
		}
		throw new GenericRestException(VENDOR_NOTFOUND_ID + id, HttpStatus.NOT_FOUND);	
	}

	@GetMapping("/findByVendorId/{vendorId}")
	private ResponseEntity<?> findByVendorId(@PathVariable("vendorId") String vendorId) {
		Vendor vendor = vendorService.findByVendorId(vendorId);
		if(vendor!=null) {
			return new ResponseEntity<Vendor>(vendor, HttpStatus.OK);
		}
		throw new GenericRestException(VENDOR_NOTFOUND_ID + vendorId, HttpStatus.NOT_FOUND);	
	}
	
	@PostMapping("/")
	private ResponseEntity<?> save(@RequestBody Vendor vendor) {
		System.out.println(vendor);
		Vendor createdvendor = vendorService.save(vendor);
		if(createdvendor != null) {
			return new ResponseEntity<Vendor>(createdvendor, HttpStatus.CREATED);
		}
		throw new GenericRestException(VENDOR_NOTSAVED, HttpStatus.BAD_REQUEST);	
	} 
	
	@PutMapping("/")
	private ResponseEntity<?> update(@RequestBody Vendor vendor) {
		Vendor updatedvendor = vendorService.update(vendor);
		if(updatedvendor != null) {
			return new ResponseEntity<Vendor>(updatedvendor, HttpStatus.OK);
		}
		throw new GenericRestException(VENDOR_NOTUPDATED, HttpStatus.BAD_REQUEST);	
	}
	
	@DeleteMapping("/{id}")
	private ResponseEntity<?> delete(@PathVariable("id") String id) {
		if(vendorService.delete(id)) {
			return new ResponseEntity<String>(VENDOR_DELETED, HttpStatus.OK);
		}
		throw new GenericRestException(VENDOR_NOTDELETED, HttpStatus.BAD_REQUEST);	
	}
	
}
