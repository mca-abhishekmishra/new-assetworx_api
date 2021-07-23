package com.softtek.assetworx_api.controller;

import static com.softtek.assetworx_api.util.Constants.PROJECT_DELETED;
import static com.softtek.assetworx_api.util.Constants.PROJECT_NOTDELETED;
import static com.softtek.assetworx_api.util.Constants.PROJECT_NOTFOUND_ID;
import static com.softtek.assetworx_api.util.Constants.PROJECT_NOTFOUND_NAME;
import static com.softtek.assetworx_api.util.Constants.PROJECT_NOTSAVED;
import static com.softtek.assetworx_api.util.Constants.PROJECT_NOTUPDATED;

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

import com.softtek.assetworx_api.entity.Project;
import com.softtek.assetworx_api.exception.GenericRestException;
import com.softtek.assetworx_api.service.ProjectService;

@RestController
@RequestMapping("/project")
public class ProjectController {

	@Autowired
	ProjectService projectService;


	@GetMapping("/{id}")
	private ResponseEntity<?> findById(@PathVariable("id") String id) {
		Project project = projectService.findById(id);
		if(project!=null) {
			return new ResponseEntity<Project>(project, HttpStatus.OK);
		}
		throw new GenericRestException(PROJECT_NOTFOUND_ID + id, HttpStatus.NOT_FOUND);	
	}

	@GetMapping("/findByName/{name}")
	private ResponseEntity<?> findByName(@PathVariable("name") String name) {
		Project project = projectService.findByName(name);
		if(project!=null) {
			return new ResponseEntity<Project>(project, HttpStatus.OK);
		}
		throw new GenericRestException(PROJECT_NOTFOUND_NAME + name, HttpStatus.NOT_FOUND);	
	}
	
	@PostMapping("/")
	private ResponseEntity<?> save(@RequestBody Project project) {
		System.out.println(project);
		Project createdproject = projectService.save(project);
		if(createdproject != null) {
			return new ResponseEntity<Project>(createdproject, HttpStatus.CREATED);
		}
		throw new GenericRestException(PROJECT_NOTSAVED, HttpStatus.BAD_REQUEST);	
	} 
	
	@PutMapping("/")
	private ResponseEntity<?> update(@RequestBody Project project) {
		Project updatedproject = projectService.update(project);
		if(updatedproject != null) {
			return new ResponseEntity<Project>(updatedproject, HttpStatus.OK);
		}
		throw new GenericRestException(PROJECT_NOTUPDATED, HttpStatus.BAD_REQUEST);	
	}
	
	@DeleteMapping("/{id}")
	private ResponseEntity<?> delete(@PathVariable("id") String id) {
		if(projectService.delete(id)) {
			return new ResponseEntity<String>(PROJECT_DELETED, HttpStatus.OK);
		}
		throw new GenericRestException(PROJECT_NOTDELETED, HttpStatus.BAD_REQUEST);	
	}
	
}
