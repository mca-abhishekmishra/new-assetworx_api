package com.softtek.assetworx_api.controller;

import static com.softtek.assetworx_api.util.Constants.SUBCATEGORY_DELETED;
import static com.softtek.assetworx_api.util.Constants.SUBCATEGORY_NOTDELETED;
import static com.softtek.assetworx_api.util.Constants.SUBCATEGORY_NOTFOUND_ID;
import static com.softtek.assetworx_api.util.Constants.SUBCATEGORY_NOTFOUND_NAME;
import static com.softtek.assetworx_api.util.Constants.SUBCATEGORY_NOTSAVED;
import static com.softtek.assetworx_api.util.Constants.SUBCATEGORY_NOTUPDATED;

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

import com.softtek.assetworx_api.entity.SubCategory;
import com.softtek.assetworx_api.exception.GenericRestException;
import com.softtek.assetworx_api.service.SubCategoryService;

@RestController
@RequestMapping("/subCategory")
public class SubCategoryController {

	@Autowired
	SubCategoryService subCategoryService;


	@GetMapping("/{id}")
	private ResponseEntity<?> findById(@PathVariable("id") String id) {
		System.out.println("findById");
		SubCategory subCategory = subCategoryService.findById(id);
		if(subCategory!=null) {
			return new ResponseEntity<SubCategory>(subCategory, HttpStatus.OK);
		}
		throw new GenericRestException(SUBCATEGORY_NOTFOUND_ID + id, HttpStatus.NOT_FOUND);	
	}

	@GetMapping("/findByName/{name}")
	private ResponseEntity<?> findByName(@PathVariable("name") String name) {
		System.out.println("findByName");
		SubCategory subCategory = subCategoryService.findByName(name);
		if(subCategory!=null) {
			return new ResponseEntity<SubCategory>(subCategory, HttpStatus.OK);
		}
		throw new GenericRestException(SUBCATEGORY_NOTFOUND_NAME + name, HttpStatus.NOT_FOUND);	
	}

	@PostMapping("/")
	private ResponseEntity<?> save(@RequestBody SubCategory subCategory) {
		System.out.println("save");
		SubCategory createdsubCategory = subCategoryService.save(subCategory);
		if(createdsubCategory != null) {
			return new ResponseEntity<SubCategory>(createdsubCategory, HttpStatus.CREATED);
		}
		throw new GenericRestException(SUBCATEGORY_NOTSAVED, HttpStatus.BAD_REQUEST);	
	} 

	@PutMapping("/")
	private ResponseEntity<?> update(@RequestBody SubCategory subCategory) {
		System.out.println("update");
		SubCategory updatedsubCategory = subCategoryService.update(subCategory);
		if(updatedsubCategory != null) {
			return new ResponseEntity<SubCategory>(updatedsubCategory, HttpStatus.OK);
		}
		throw new GenericRestException(SUBCATEGORY_NOTUPDATED, HttpStatus.BAD_REQUEST);	
	}

	@DeleteMapping("/{id}")
	private ResponseEntity<?> delete(@PathVariable("id") String id) {
		System.out.println("delete");
		if(subCategoryService.delete(id)) {
			return new ResponseEntity<String>(SUBCATEGORY_DELETED, HttpStatus.OK);
		}
		throw new GenericRestException(SUBCATEGORY_NOTDELETED, HttpStatus.BAD_REQUEST);	
	}

}
