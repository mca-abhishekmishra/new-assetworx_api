package com.softtek.assetworx_api.controller;

import static com.softtek.assetworx_api.util.Constants.REPORT_NOTSAVED;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.softtek.assetworx_api.entity.AssetworxUser;
import com.softtek.assetworx_api.entity.Dashboard;
import com.softtek.assetworx_api.entity.Report;
import com.softtek.assetworx_api.exception.GenericRestException;
import com.softtek.assetworx_api.service.DashboardService;
import com.softtek.assetworx_api.service.ReportService;
import com.softtek.assetworx_api.service.UserService;
import com.softtek.assetworx_api.util.Constants;

@RestController
@RequestMapping("/report")
public class ReportController {

	@Autowired
	ReportService reportService;

	@Autowired
	DashboardService dashboardService;

	@Autowired
	UserService userService;

	@Autowired
	HttpServletRequest request;

	@GetMapping("/{id}")
	private ResponseEntity<?> findById(@PathVariable("id") String id) {
		Report report = reportService.findById(id);
		if (report != null) {
			return new ResponseEntity<Report>(report, HttpStatus.OK);
		}
		throw new GenericRestException("Report not found", HttpStatus.NOT_FOUND);
	}

	@GetMapping("/getUsersBySharedReportId/{reportId}")
	private ResponseEntity<List<AssetworxUser>> getUsersBySharedReportId(@PathVariable(required = true) String reportId) {
		List<AssetworxUser> data = userService.findUsersWithSharedReportId(reportId);
		if (data.size() <= 0) {
			throw new GenericRestException(Constants.USERS_NOTFOUND, HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<List<AssetworxUser>>(data, HttpStatus.OK);
	}

	@GetMapping("/getReports")
	private ResponseEntity<Map<String, Set<Report>>> findAll() {
		Map<String, Set<Report>> data = reportService
				.findAllUsersReportsByUserName(request.getHeader("employeeId").toString());
		System.out.println(data);
		List<Report> collect = new ArrayList<>();
		collect.addAll((Set<Report>) data.get("sharedReports"));
		collect.addAll((Set<Report>) data.get("userReport"));
		return new ResponseEntity<Map<String, Set<Report>>>(data, HttpStatus.OK);
	}

	@PostMapping("/")
	private ResponseEntity<?> save(@RequestBody Report report) {
		System.out.println("Save");
		Report createdReport = reportService.save(report, request.getHeader("employeeId").toString());
		if (createdReport != null) {
			return new ResponseEntity<Report>(createdReport, HttpStatus.CREATED);
		}
		throw new GenericRestException(REPORT_NOTSAVED, HttpStatus.BAD_REQUEST);
	}

	@PutMapping("/")
	private ResponseEntity<?> update(@RequestBody Report report) {
		Report createdReport = reportService.update(report);
		if (createdReport != null) {
			return new ResponseEntity<Report>(createdReport, HttpStatus.OK);
		}
		throw new GenericRestException(REPORT_NOTSAVED, HttpStatus.BAD_REQUEST);
	}

	@PostMapping("/getReportData")
	private ResponseEntity<?> getReportData(@RequestBody Report report) {
		List<Object> data = reportService.getReportData(report);
		if (data != null) {
			return new ResponseEntity<List<Object>>(data, HttpStatus.OK);
		}
		throw new GenericRestException("Report data could not be loaded", HttpStatus.BAD_REQUEST);
	}

	@PutMapping("/shareReportToUsers")
	public ResponseEntity<?> shareReportToUsers(@RequestParam(value = "reportId", required = true) String reportId,
			@RequestParam(value = "userNames", required = true) List<String> toUserName) {
		int updatedCount = reportService.shareReportToUsers(reportId, toUserName);
		if (updatedCount <= 0) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} else {
			return new ResponseEntity<>(HttpStatus.CREATED);
		}
	}

	@PutMapping("/shareReportToRoles")
	public ResponseEntity<?> shareReportToRoles(@RequestParam(value = "reportId", required = true) String reportId,
			@RequestParam(value = "role", required = true) List<String> role) {
		int updatedCount = reportService.shareReportToRoles(reportId, role);
		if (updatedCount <= 0) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} else {
			return new ResponseEntity<>(HttpStatus.CREATED);
		}
	}

	/*
	 * send >> 1. userNames 2. reportId 3. type {look in Constants.java}
	 */
	@DeleteMapping("/removeReportFromUser")
	public ResponseEntity<Dashboard> removeDashBoardFromUser(@RequestBody Map<String, Object> data) {
		System.out.println("data:" + data);
		int updatedCount = reportService.removeReportFromUsers((List<String>) data.get("userNames"),
				data.get("reportId").toString(), data.get("type").toString());
		if (updatedCount <= 0) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} else {
			return new ResponseEntity<Dashboard>(HttpStatus.CREATED);
		}
	}

	/*
	 * send >> 1. roles 2. reportId
	 */
	@DeleteMapping("/removeReportFromRole")
	public ResponseEntity<Dashboard> removeDashBoardFromRoles(@RequestBody Map<String, Object> data) {
		System.out.println("data:" + data);
		int count = reportService.removeReportFromRoles((List<String>) data.get("roles"),
				data.get("reportId").toString());
		if (count <= 0) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} else {
			return new ResponseEntity<Dashboard>(HttpStatus.CREATED);
		}
	}
	
	@GetMapping("/getSeatallReport")
	private ResponseEntity<?> getSeatallReport(@RequestParam("reportName") String reportName, @RequestParam(value="reportParam", required = false) String reportParam) {
		Object data = reportService.getSeatallReport(reportName, reportParam);
		if (data != null) {
			return new ResponseEntity<Object>(data, HttpStatus.OK);
		}
		throw new GenericRestException("Report data could not be loaded", HttpStatus.BAD_REQUEST);
	}

}
