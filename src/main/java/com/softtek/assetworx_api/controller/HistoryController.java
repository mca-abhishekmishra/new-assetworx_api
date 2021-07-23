package com.softtek.assetworx_api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.softtek.assetworx_api.entity.History;
import com.softtek.assetworx_api.service.HistoryService;

@RestController
@RequestMapping("/history")
public class HistoryController {
	
	@Autowired
	HistoryService historyService;
	
	@GetMapping("/findAllByRelativeId/{relativeId}")
	public ResponseEntity<List<History>> findAllByRelativeId(@PathVariable String relativeId){
		return new ResponseEntity<List<History>>(historyService.findAllByRelativeIdOrderByLastUpdated(relativeId), HttpStatus.OK);
	}

}
