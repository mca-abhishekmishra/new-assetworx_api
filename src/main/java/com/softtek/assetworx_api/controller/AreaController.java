package com.softtek.assetworx_api.controller;

import static com.softtek.assetworx_api.util.Constants.AREA_DELETED;
import static com.softtek.assetworx_api.util.Constants.AREA_NOTDELETED;
import static com.softtek.assetworx_api.util.Constants.AREA_NOTFOUND_ID;
import static com.softtek.assetworx_api.util.Constants.AREA_NOTFOUND_NAME;
import static com.softtek.assetworx_api.util.Constants.AREA_NOTSAVED;
import static com.softtek.assetworx_api.util.Constants.AREA_NOTUPDATED;

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

import com.softtek.assetworx_api.entity.Area;
import com.softtek.assetworx_api.exception.GenericRestException;
import com.softtek.assetworx_api.service.AreaService;

@RestController
@RequestMapping("/area")
public class AreaController {

	@Autowired
	AreaService areaService;

	@GetMapping("/{id}")
	private ResponseEntity<?> findById(@PathVariable("id") String id) {
		System.out.println("findById");
		Area area = areaService.findById(id);
		if(area!=null) {
			return new ResponseEntity<Area>(area, HttpStatus.OK);
		}
		throw new GenericRestException(AREA_NOTFOUND_ID + id, HttpStatus.NOT_FOUND);	
	}

	@GetMapping("/findByName/{name}")
	private ResponseEntity<?> findByName(@PathVariable("name") String name) {
		System.out.println("findByName");
		Area area = areaService.findByName(name);
		if(area!=null) {
			return new ResponseEntity<Area>(area, HttpStatus.OK);
		}
		throw new GenericRestException(AREA_NOTFOUND_NAME + name, HttpStatus.NOT_FOUND);	
	}

	@PostMapping("/")
	private ResponseEntity<?> save(@RequestBody Area area) {
		System.out.println("save");
		Area createdarea = areaService.save(area);
		if(createdarea != null) {
			return new ResponseEntity<Area>(createdarea, HttpStatus.CREATED);
		}
		throw new GenericRestException(AREA_NOTSAVED, HttpStatus.BAD_REQUEST);	
	} 

	@PutMapping("/")
	private ResponseEntity<?> update(@RequestBody Area area) {
		System.out.println("update");
		Area updatedarea = areaService.update(area);
		if(updatedarea != null) {
			return new ResponseEntity<Area>(updatedarea, HttpStatus.OK);
		}
		throw new GenericRestException(AREA_NOTUPDATED, HttpStatus.BAD_REQUEST);	
	}

	@DeleteMapping("/{id}")
	private ResponseEntity<?> delete(@PathVariable("id") String id) {
		System.out.println("delete");
		if(areaService.delete(id)) {
			return new ResponseEntity<String>(AREA_DELETED, HttpStatus.OK);
		}
		throw new GenericRestException(AREA_NOTDELETED, HttpStatus.BAD_REQUEST);	
	}

}
