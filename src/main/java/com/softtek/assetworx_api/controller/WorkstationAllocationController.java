package com.softtek.assetworx_api.controller;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.softtek.assetworx_api.entity.AssetworxUser;
import com.softtek.assetworx_api.entity.WorkstationAllocation;
import com.softtek.assetworx_api.exception.GenericRestException;
import com.softtek.assetworx_api.service.UserService;
import com.softtek.assetworx_api.service.WorkStationService;
import com.softtek.assetworx_api.service.WorkstationAllocationService;

@RestController
@RequestMapping("/workstationAllocation")
public class WorkstationAllocationController {

	@Autowired
	WorkStationService workStationService;

	@Autowired
	WorkstationAllocationService allocationService;
	
	@Autowired
	UserService userService;

	@Autowired
	RestTemplate restTemplate;
	
	@Value("${seatingcatacity.url}")
	private String seatingcatacityUrl;
	
	/*
	 * params are ( employeeId , workstationId )
	 */
	@PostMapping("/allocate")
	private ResponseEntity<?> allocate(@RequestBody Map<String, String> data, @RequestHeader(value="employeeId") String employeeId) {
		System.out.println("data : " + data);
		
		AssetworxUser asUser = userService.findByUserName(employeeId);
		WorkstationAllocation createdWSAllocation = allocationService.allocate(data.get("employeeId"),
				data.get("workstationId"));
		if (createdWSAllocation != null) {
			updateGDCSeatingCapacityReport(createdWSAllocation, asUser.getEmail(), "allocate");
			return new ResponseEntity<WorkstationAllocation>(createdWSAllocation, HttpStatus.CREATED);
		}
		throw new GenericRestException("Allocation Failed!..", HttpStatus.BAD_REQUEST);
	}

	@PutMapping("/deallocate")
	private ResponseEntity<?> deallocate(@RequestBody Map<String, String> data, @RequestHeader(value="employeeId") String employeeId) {
		System.out.println("data : " + data);
		
		AssetworxUser asUser = userService.findByUserName(employeeId);
		WorkstationAllocation deallocatedWS = allocationService.deallocate(data.get("employeeId"),
				data.get("workstationId"));
		if (deallocatedWS != null) {
			updateGDCSeatingCapacityReport(deallocatedWS, asUser.getEmail(), "deallocate");
			return new ResponseEntity<WorkstationAllocation>(deallocatedWS, HttpStatus.OK);
		}
		throw new GenericRestException("De-Allocation Failed!..", HttpStatus.BAD_REQUEST);
	}
	
	private boolean updateGDCSeatingCapacityReport(WorkstationAllocation wsAllocation, String userEmail, String type) {
		boolean flag = false;
		Map<String, String> wr = mapObject(wsAllocation,type,userEmail);
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(seatingcatacityUrl) 
		.queryParam("api-version", "2016-06-01")
		.queryParam("sp", "/triggers/manual/run")
		.queryParam("sv", "1.0")
		.queryParam("sig", "IzM3da5Gt5fJjlYxvd_dzTnLOZd17baEoQnMg2e8zEA");
		try {
			ResponseEntity<String> response = restTemplate.postForEntity(builder.buildAndExpand().toUri(), wr,String.class);
			if (response.getStatusCode() == HttpStatus.ACCEPTED) {
				flag = true;
			}
		}catch(Exception ex) {
			flag = false;
		}
		
		return flag;
	}
	
	private Map<String, String> mapObject(WorkstationAllocation wsAllocation, String type, String userEmail) {
		Map<String, String> valueMap = new LinkedHashMap<>();
		valueMap.put("Floor", wsAllocation.getWorkstation().getArea().getFloor().getName());
		valueMap.put("Area", wsAllocation.getWorkstation().getArea().getParentArea().getName());
		valueMap.put("WSType", wsAllocation.getWorkstation().getArea().getAreaType().getName());
		valueMap.put("WS No.", wsAllocation.getWorkstation().getArea().getName());
		valueMap.put("UpdatedBy", userEmail);
		valueMap.put("UpdatedOn", new Date().toString());
		
		if(type.equals("allocate")) {
			valueMap.put("EmpID", wsAllocation.getEmployee().getEmployeeId());
			valueMap.put("EmpName", wsAllocation.getEmployee().getName());
			valueMap.put("EmpEmail", wsAllocation.getEmployee().getEmail());
			valueMap.put("ISID", wsAllocation.getEmployee().getIsid());
			valueMap.put("Shared", wsAllocation.getWorkstation().isShared()==true?"YES":"NO");
			valueMap.put("Department", wsAllocation.getEmployee().getDepartment().getDivision().getName());
			valueMap.put("Practice", wsAllocation.getEmployee().getDepartment().getName());
			valueMap.put("EmpType", wsAllocation.getEmployee().getEmploymentType());
		}else{
			valueMap.put("EmpID", "");
			valueMap.put("EmpName", "");
			valueMap.put("EmpEmail", "");
			valueMap.put("ISID", "");
			valueMap.put("Shared", "");
			valueMap.put("Department", "");
			valueMap.put("Practice", "");
			valueMap.put("EmpType", "");
		}
		return valueMap;
	}

}
