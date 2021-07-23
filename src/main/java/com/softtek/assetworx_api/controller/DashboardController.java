package com.softtek.assetworx_api.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.softtek.assetworx_api.entity.AssetworxUser;
import com.softtek.assetworx_api.entity.Dashboard;
import com.softtek.assetworx_api.entity.Report;
import com.softtek.assetworx_api.service.DashboardService;
import com.softtek.assetworx_api.service.UserService;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

	@Autowired
	DashboardService dashboardService;

	@Autowired
	UserService userService;

	@Autowired
	HttpServletRequest request;

	@GetMapping(value = { "/list" })
	public ResponseEntity<Map<String, Object>> getAll() {
		System.out.println("--------------------------------------");

		// dashboardService.shareDashBoardToUsers("9b6aaa35-83cf-4141-9225-d30ef2dd9436",
		// Arrays.asList("ASP01310", "ASP01333"));

//		dashboardService.shareDashBoardToUsers("f0d4febc-4e50-451f-92ac-084fee78ac49",
//				Arrays.asList("ASP01310", "ASP01333"));

		// sharng it to IT_USER and basic user
//		dashboardService.shareDashBoardToRoles("9b6aaa35-83cf-4141-9225-d30ef2dd9436",
//				Arrays.asList("219c3062-5ada-4cbc-8357-cb0970da9885", "422651cc-ed48-4ad8-bc01-0fa6d05cb605"));

//		System.out.println("DEFAULT Dashboards : "
//				+ dashboardService.getDefaultDashBoard(request.getHeader("employeeId").toString()));

//		dashboardService.removeDashBoardFromRole("d5fd72e1-3ea8-4134-80b4-10282bc763d2",
//				"9b6aaa35-83cf-4141-9225-d30ef2dd9436");

		System.out.println("===================================");

		/*
		 * change it later according to ui changes as ui is not handling map
		 */
		Map<String, Object> allDashbaords = dashboardService
				.findUsersDashboards(request.getHeader("employeeId").toString());
		// temp logic for testing
		/*
		 * List<Dashboard> collect = new ArrayList<>(); collect.add((Dashboard)
		 * allDashbaords.get("defaultDashboard")); collect.addAll((Set<Dashboard>)
		 * allDashbaords.get("sharedReports")); collect.addAll((Set<Dashboard>)
		 * allDashbaords.get("userReport"));
		 */

		/* List<Dashboard> dashboardList = dashboardService.findAll(); */
		return new ResponseEntity<Map<String, Object>>(allDashbaords, HttpStatus.OK);
	}

	@GetMapping("/userDashboards/{userId}")
	public ResponseEntity<?> findUserDashboards(@PathVariable("userId") String userId) {
		Map<String, Object> dashboards = dashboardService.findUsersDashboards(userId);
		if (dashboards == null || dashboards.size() == 0) {
			throw new ResourceNotFoundException("Dashboard could not be found for the given id: " + userId);
		}
		return new ResponseEntity<Map<String, Object>>(dashboards, HttpStatus.OK);
	}

	@GetMapping("/getUsersBySharedDasboardId/{dashBoardId}")
	private ResponseEntity<List<AssetworxUser>> getUsersBySharedDasboardId(
			@PathVariable(required = true) String dashBoardId) {
		List<AssetworxUser> data = userService.findUsersWithSharedDashBoardId(dashBoardId);
		return new ResponseEntity<List<AssetworxUser>>(data, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Map<String, Object>> findById(@PathVariable("id") String id) {
		Dashboard dashboard = dashboardService.findById(id);
		Map<String, Object> map = new HashMap<String, Object>();
		if (dashboard == null) {
			throw new ResourceNotFoundException("Dashboard could not be found for the given id: " + id);
		}
		AssetworxUser user = userService.findByUserName(request.getHeader("employeeId").toString());
		map.put("isEditable", dashboard.getCreatedBy().startsWith(request.getHeader("employeeId").toString()));
		map.put("isDefault", dashboardService.getDefaultDashBoard(request.getHeader("employeeId").toString()).getId().equals(dashboard.getId()));
		map.put("dashboard", dashboard);
		return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
	}

	@PostMapping("/save")
	public ResponseEntity<Dashboard> save(@RequestBody Dashboard dashboard) {
		String userName = request.getHeader("employeeId").toString();
		dashboard = dashboardService.save(dashboard, userName);
		if (dashboard == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} else {
			return new ResponseEntity<Dashboard>(dashboard, HttpStatus.CREATED);
		}
	}

	@PutMapping("/setDefaultDashboard/{dashboardId}")
	public ResponseEntity<?> setDefaultDashBoard(
			@PathVariable(value = "dashboardId", required = true) String dashboardId) {
		String userName = request.getHeader("employeeId").toString();
		boolean flag = dashboardService.setDefaultDashBoard(dashboardId, userName);
		if (!flag) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} else {
			return new ResponseEntity<>(HttpStatus.OK);
		}
	}

	@PutMapping("/shareDashboardToUsers")
	public ResponseEntity<?> shareDashBoardToUser(@RequestBody Map<String, Object> data) {
		int updatedCount = dashboardService.shareDashBoardToUsers((String) data.get("dashboardId"), (List<String>) data.get("userNames"));
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PutMapping("/shareDashboardToRoles")
	public ResponseEntity<?> shareDashBoardToRoles(
			@RequestParam(value = "dashboardId", required = true) String dashboardId,
			@RequestParam(value = "roles", required = true) List<String> roles) {
		int updatedCount = dashboardService.shareDashBoardToRoles(dashboardId, roles);
		if (updatedCount <= 0) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} else {
			return new ResponseEntity<>(HttpStatus.CREATED);
		}
	}

	@GetMapping("/defaultDashboard")
	public ResponseEntity<?> getDefaultDashBoard() {
		String userName = request.getHeader("employeeId").toString();
		Dashboard defaultDashboard = dashboardService.getDefaultDashBoard(userName);
		if (defaultDashboard == null) {
			throw new ResourceNotFoundException(
					"Default Dashboard could not be found for the given user id: " + userName);
		} else {
			return new ResponseEntity<>(defaultDashboard, HttpStatus.OK);
		}
	}

	@PutMapping("/removeDefaultDashboard")
	public ResponseEntity<?> removeDefaultDashBoard() {
		String userName = request.getHeader("employeeId").toString();
		boolean flag = dashboardService.removeDefaultDashBoard(userName);
		if (!flag) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} else {
			return new ResponseEntity<>(HttpStatus.CREATED);
		}
	}

	@PutMapping("/update")
	public ResponseEntity<Dashboard> update(@RequestBody Dashboard a) {
		Dashboard dashboard = dashboardService.update(a);
		if (dashboard == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} else {
			return new ResponseEntity<Dashboard>(dashboard, HttpStatus.OK);
		}
	}

	/*
	 * send >> 1. userNames 2. dashboardId 3. type {look in Constants.java}
	 */
	@DeleteMapping("/removeDashboardFromUser")
	public ResponseEntity<Dashboard> removeDashBoardFromUser(@RequestBody Map<String, Object> data) {
		System.out.println("data:" + data);
		int updatedCount = dashboardService.removeDashBoardFromUsers((List<String>) data.get("userNames"),
				data.get("dashboardId").toString(), data.get("type").toString());
		if (updatedCount <= 0) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} else {
			return new ResponseEntity<Dashboard>(HttpStatus.CREATED);
		}
	}

	/*
	 * send >> 1. roles 2. dashboardId
	 */
	@DeleteMapping("/removeDashboardFromRole")
	public ResponseEntity<Dashboard> removeDashBoardFromRoles(@RequestBody Map<String, Object> data) {
		System.out.println("data:" + data);
		int count = dashboardService.removeDashBoardFromRoles((List<String>) data.get("roles"),
				data.get("dashboardId").toString());
		if (count <= 0) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} else {
			return new ResponseEntity<Dashboard>(HttpStatus.CREATED);
		}
	}

	@PostMapping("/addReport")
	public ResponseEntity<Report> addReport(@RequestBody Map<String, String> data) {
		Report report = dashboardService.addReport(data.get("reportId"), data.get("dashboardId"));
		if (report == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} else {
			return new ResponseEntity<Report>(report, HttpStatus.CREATED);
		}
	}

	@DeleteMapping("/removeReport")
	public ResponseEntity<Report> removeReport(@RequestBody Map<String, String> data) {
		System.out.println("data:" + data);
		Report report = dashboardService.removeReport(data.get("reportId"), data.get("dashboardId"));
		if (report == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} else {
			return new ResponseEntity<Report>(report, HttpStatus.CREATED);
		}
	}
}
