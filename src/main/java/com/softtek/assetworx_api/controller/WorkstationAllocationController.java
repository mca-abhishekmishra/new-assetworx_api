package com.softtek.assetworx_api.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.softtek.assetworx_api.entity.WorkstationAllocation;
import com.softtek.assetworx_api.exception.GenericRestException;
import com.softtek.assetworx_api.service.WorkStationService;
import com.softtek.assetworx_api.service.WorkstationAllocationService;

@RestController
@RequestMapping("/workstationAllocation")
public class WorkstationAllocationController {

	@Autowired
	WorkStationService workStationService;

	@Autowired
	WorkstationAllocationService allocationService;

	/*
	 * params are ( employeeId , workstationId )
	 */
	@PostMapping("/allocate")
	private ResponseEntity<?> allocate(@RequestBody Map<String, String> data) {
		System.out.println("data : " + data);
		WorkstationAllocation createdWSAllocation = allocationService.allocate(data.get("employeeId"),
				data.get("workstationId"));
		if (createdWSAllocation != null) {
			return new ResponseEntity<WorkstationAllocation>(createdWSAllocation, HttpStatus.CREATED);
		}
		throw new GenericRestException("Allocation Failed!..", HttpStatus.BAD_REQUEST);
	}

	@PutMapping("/deallocate")
	private ResponseEntity<?> deallocate(@RequestBody Map<String, String> data) {
		System.out.println("data : " + data);
		WorkstationAllocation deallocatedWS = allocationService.deallocate(data.get("employeeId"),
				data.get("workstationId"));
		if (deallocatedWS != null) {
			return new ResponseEntity<WorkstationAllocation>(deallocatedWS, HttpStatus.OK);
		}
		throw new GenericRestException("De-Allocation Failed!..", HttpStatus.BAD_REQUEST);
	}


}
