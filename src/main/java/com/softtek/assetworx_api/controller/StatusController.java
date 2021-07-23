package com.softtek.assetworx_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.softtek.assetworx_api.service.StatusService;

@RestController
public class StatusController {
	
	@Autowired
	StatusService statusService; 

}
