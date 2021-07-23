package com.softtek.assetworx_api.controller;

import static com.softtek.assetworx_api.util.Constants.IN_PROGRESS;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.softtek.assetworx_api.entity.Process;
import com.softtek.assetworx_api.service.ProcessService;

@RestController
@RequestMapping("/process")
public class ProcessController {

	@Autowired
	ProcessService processService;

	@GetMapping("/{id}")
	private ResponseEntity<Process> findById(@PathVariable("id") String id) {
		Process process = processService.findById(id);
		return new ResponseEntity<Process>(process, HttpStatus.OK);	
	}
	
	@GetMapping("/findByRelativeId/{relativeId}")
	private ResponseEntity<Process> findByRelativeId(@PathVariable("relativeId") String relativeId) {
		Process process = processService.findByRelativeId(relativeId, IN_PROGRESS);
		return new ResponseEntity<Process>(process, HttpStatus.OK);
	}
}
