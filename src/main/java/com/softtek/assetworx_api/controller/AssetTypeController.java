package com.softtek.assetworx_api.controller;

import static com.softtek.assetworx_api.util.Constants.ASSET_TYPE_DELETED;
import static com.softtek.assetworx_api.util.Constants.ASSET_TYPE_NOTDELETED;
import static com.softtek.assetworx_api.util.Constants.ASSET_TYPE_NOTFOUND_ID;
import static com.softtek.assetworx_api.util.Constants.ASSET_TYPE_NOTFOUND_NAME;
import static com.softtek.assetworx_api.util.Constants.ASSET_TYPE_NOTSAVED;
import static com.softtek.assetworx_api.util.Constants.ASSET_TYPE_NOTUPDATED;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.softtek.assetworx_api.entity.AssetType;
import com.softtek.assetworx_api.exception.GenericRestException;
import com.softtek.assetworx_api.service.AssetTypeService;

@Controller
@RequestMapping("/assetType")
public class AssetTypeController {
	
	@Autowired
	AssetTypeService assetTypeService;	

	
	@GetMapping("/{id}")
	private ResponseEntity<?> findById(@PathVariable("id") String id) {
		AssetType assetType = assetTypeService.findById(id);
		if(assetType!=null) {
			return new ResponseEntity<AssetType>(assetType, HttpStatus.OK);
		}
		throw new GenericRestException(ASSET_TYPE_NOTFOUND_ID + id, HttpStatus.NOT_FOUND);	
	}

	@GetMapping("/findByName/{name}")
	private ResponseEntity<?> findByName(@PathVariable("name") String name) {
		AssetType assetType = assetTypeService.findByName(name);
		if(assetType!=null) {
			return new ResponseEntity<AssetType>(assetType, HttpStatus.OK);
		}
		throw new GenericRestException(ASSET_TYPE_NOTFOUND_NAME + name, HttpStatus.NOT_FOUND);	
	}
	
	@PostMapping("/")
	private ResponseEntity<?> save(@RequestBody AssetType assetType) {
		System.out.println(assetType);
		AssetType createdAssetType = assetTypeService.save(assetType);
		if(createdAssetType != null) {
			return new ResponseEntity<AssetType>(createdAssetType, HttpStatus.CREATED);
		}
		throw new GenericRestException(ASSET_TYPE_NOTSAVED, HttpStatus.BAD_REQUEST);	
	} 
	
	@PutMapping("/")
	private ResponseEntity<?> update(@RequestBody AssetType assetType) {
		AssetType updatedAssetType = assetTypeService.update(assetType);
		if(updatedAssetType != null) {
			return new ResponseEntity<AssetType>(updatedAssetType, HttpStatus.OK);
		}
		throw new GenericRestException(ASSET_TYPE_NOTUPDATED, HttpStatus.BAD_REQUEST);	
	}
	
	@DeleteMapping("/{id}")
	private ResponseEntity<?> delete(@PathVariable("id") String id) {
		if(assetTypeService.delete(id)) {
			return new ResponseEntity<String>(ASSET_TYPE_DELETED, HttpStatus.OK);
		}
		throw new GenericRestException(ASSET_TYPE_NOTDELETED, HttpStatus.BAD_REQUEST);	
	}

}
