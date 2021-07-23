package com.softtek.assetworx_api.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.softtek.assetworx_api.entity.Report;

public interface ReportService {

	Report save(Report report);
	
	Report save(Report report , String userName);

	Report findById(String id);

	<T> List<Object> getReportData(Report report);

	Report update(Report report);

	List<Report> findAll();
	
	Map<String , Set<Report>> findAllUsersReportsByUserName(String userName);
	
	int shareReportToUsers(String reportId, List<String> toUserName);

	int shareReportToRoles(String reportId, List<String> roles);

	int removeReportFromUsers(List<String> userNames, String string, String type);

	int removeReportFromRoles(List<String> roles, String string);

	Object getSeatallReport(String reportName, String reportParam);

}
