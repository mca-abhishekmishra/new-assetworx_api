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

import com.softtek.assetworx_api.entity.AssetModel;
import com.softtek.assetworx_api.exception.GenericRestException;
import com.softtek.assetworx_api.service.AssetModelService;

@RestController
@RequestMapping("/assetModel")
public class AssetModelController {

	@Autowired
	AssetModelService assetModelService;

	@GetMapping("/{id}")
	private ResponseEntity<?> findById(@PathVariable("id") String id) {
		AssetModel assetModel = assetModelService.findById(id);
		if (assetModel != null) {
			return new ResponseEntity<AssetModel>(assetModel, HttpStatus.OK);
		}
		throw new GenericRestException(ASSET_MODEL_NOTFOUND_ID + id, HttpStatus.NOT_FOUND);
	}

	@GetMapping("/findByName/{name}")
	private ResponseEntity<?> findByName(@PathVariable("name") String name) {
		AssetModel assetModel = assetModelService.findByName(name);
		if (assetModel != null) {
			return new ResponseEntity<AssetModel>(assetModel, HttpStatus.OK);
		}
		throw new GenericRestException(ASSET_MODEL_NOTFOUND_NAME + name, HttpStatus.NOT_FOUND);
	}

	@PostMapping("/")
	private ResponseEntity<?> save(@RequestBody AssetModel assetModel) {
		System.out.println(assetModel);
		AssetModel createdModel = assetModelService.save(assetModel);
		if (createdModel != null) {
			return new ResponseEntity<AssetModel>(createdModel, HttpStatus.CREATED);
		}
		throw new GenericRestException(ASSET_MODEL_NOTSAVED, HttpStatus.BAD_REQUEST);
	}

	@PutMapping("/")
	private ResponseEntity<?> update(@RequestBody AssetModel assetModel) {
		AssetModel updatedModel = assetModelService.update(assetModel);
		if (updatedModel != null) {
			return new ResponseEntity<AssetModel>(updatedModel, HttpStatus.OK);
		}
		throw new GenericRestException(ASSET_MODEL_NOTUPDATED, HttpStatus.BAD_REQUEST);
	}

	@DeleteMapping("/{id}")
	private ResponseEntity<?> delete(@PathVariable("id") String id) {
		if (assetModelService.delete(id)) {
			return new ResponseEntity<String>(ASSET_MODEL_DELETED, HttpStatus.OK);
		}
		throw new GenericRestException(ASSET_MODEL_NOTDELETED, HttpStatus.BAD_REQUEST);
	}

}
