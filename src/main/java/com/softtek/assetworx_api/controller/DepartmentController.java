package com.softtek.assetworx_api.controller;

import static com.softtek.assetworx_api.util.Constants.DEPARTMENT_DELETED;
import static com.softtek.assetworx_api.util.Constants.DEPARTMENT_NOTDELETED;
import static com.softtek.assetworx_api.util.Constants.DEPARTMENT_NOTFOUND_ID;
import static com.softtek.assetworx_api.util.Constants.DEPARTMENT_NOTFOUND_NAME;
import static com.softtek.assetworx_api.util.Constants.DEPARTMENT_NOTSAVED;
import static com.softtek.assetworx_api.util.Constants.DEPARTMENT_NOTUPDATED;

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

import com.softtek.assetworx_api.entity.Department;
import com.softtek.assetworx_api.exception.GenericRestException;
import com.softtek.assetworx_api.service.DepartmentService;

@RestController
@RequestMapping("/department")
public class DepartmentController {

	@Autowired
	DepartmentService departmentService;


	@GetMapping("/{id}")
	private ResponseEntity<?> findById(@PathVariable("id") String id) {
		Department department = departmentService.findById(id);
		if(department!=null) {
			return new ResponseEntity<Department>(department, HttpStatus.OK);
		}
		throw new GenericRestException(DEPARTMENT_NOTFOUND_ID + id, HttpStatus.NOT_FOUND);	
	}

	@GetMapping("/findByName/{name}")
	private ResponseEntity<?> findByName(@PathVariable("name") String name) {
		Department department = departmentService.findByName(name);
		if(department!=null) {
			return new ResponseEntity<Department>(department, HttpStatus.OK);
		}
		throw new GenericRestException(DEPARTMENT_NOTFOUND_NAME + name, HttpStatus.NOT_FOUND);	
	}
	
	@PostMapping("/")
	private ResponseEntity<?> save(@RequestBody Department department) {
		System.out.println(department);
		Department createddepartment = departmentService.save(department);
		if(createddepartment != null) {
			return new ResponseEntity<Department>(createddepartment, HttpStatus.CREATED);
		}
		throw new GenericRestException(DEPARTMENT_NOTSAVED, HttpStatus.BAD_REQUEST);	
	} 
	
	@PutMapping("/")
	private ResponseEntity<?> update(@RequestBody Department department) {
		Department updateddepartment = departmentService.update(department);
		if(updateddepartment != null) {
			return new ResponseEntity<Department>(updateddepartment, HttpStatus.OK);
		}
		throw new GenericRestException(DEPARTMENT_NOTUPDATED, HttpStatus.BAD_REQUEST);	
	}
	
	@DeleteMapping("/{id}")
	private ResponseEntity<?> delete(@PathVariable("id") String id) {
		if(departmentService.delete(id)) {
			return new ResponseEntity<String>(DEPARTMENT_DELETED, HttpStatus.OK);
		}
		throw new GenericRestException(DEPARTMENT_NOTDELETED, HttpStatus.BAD_REQUEST);	
	}
	
}
