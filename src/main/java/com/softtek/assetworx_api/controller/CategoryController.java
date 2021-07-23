package com.softtek.assetworx_api.controller;

import static com.softtek.assetworx_api.util.Constants.CATEGORY_DELETED;
import static com.softtek.assetworx_api.util.Constants.CATEGORY_NOTDELETED;
import static com.softtek.assetworx_api.util.Constants.CATEGORY_NOTFOUND_ID;
import static com.softtek.assetworx_api.util.Constants.CATEGORY_NOTFOUND_NAME;
import static com.softtek.assetworx_api.util.Constants.CATEGORY_NOTSAVED;
import static com.softtek.assetworx_api.util.Constants.CATEGORY_NOTUPDATED;

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

import com.softtek.assetworx_api.entity.Category;
import com.softtek.assetworx_api.exception.GenericRestException;
import com.softtek.assetworx_api.service.CategoryService;

@RestController
@RequestMapping("/category")
public class CategoryController {

	@Autowired
	CategoryService categoryService;


	@GetMapping("/{id}")
	private ResponseEntity<?> findById(@PathVariable("id") String id) {
		Category category = categoryService.findById(id);
		if(category!=null) {
			return new ResponseEntity<Category>(category, HttpStatus.OK);
		}
		throw new GenericRestException(CATEGORY_NOTFOUND_ID + id, HttpStatus.NOT_FOUND);	
	}

	@GetMapping("/findByName/{name}")
	private ResponseEntity<?> findByName(@PathVariable("name") String name) {
		Category category = categoryService.findByName(name);
		if(category!=null) {
			return new ResponseEntity<Category>(category, HttpStatus.OK);
		}
		throw new GenericRestException(CATEGORY_NOTFOUND_NAME + name, HttpStatus.NOT_FOUND);	
	}
	
	@PostMapping("/")
	private ResponseEntity<?> save(@RequestBody Category category) {
		System.out.println(category);
		Category createdCategory = categoryService.save(category);
		if(createdCategory != null) {
			return new ResponseEntity<Category>(createdCategory, HttpStatus.CREATED);
		}
		throw new GenericRestException(CATEGORY_NOTSAVED, HttpStatus.BAD_REQUEST);	
	} 
	
	@PutMapping("/")
	private ResponseEntity<?> update(@RequestBody Category category) {
		Category updatedCategory = categoryService.update(category);
		if(updatedCategory != null) {
			return new ResponseEntity<Category>(updatedCategory, HttpStatus.OK);
		}
		throw new GenericRestException(CATEGORY_NOTUPDATED, HttpStatus.BAD_REQUEST);	
	}
	
	@DeleteMapping("/{id}")
	private ResponseEntity<?> delete(@PathVariable("id") String id) {
		if(categoryService.delete(id)) {
			return new ResponseEntity<String>(CATEGORY_DELETED, HttpStatus.OK);
		}
		throw new GenericRestException(CATEGORY_NOTDELETED, HttpStatus.BAD_REQUEST);	
	}
	
}
