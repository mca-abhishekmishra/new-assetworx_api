package com.softtek.assetworx_api.service;

import java.util.List;
import java.util.Map;

import com.softtek.assetworx_api.entity.Dashboard;
import com.softtek.assetworx_api.entity.Report;

public interface DashboardService {

	List<Dashboard> findAll();

	Dashboard findById(String id);

	Dashboard save(Dashboard a);

	Dashboard update(Dashboard a);

	// Dashboard delete(String userId, String dashboardId);

	Report addReport(String reportId, String dashboardId);

	Report removeReport(String string, String string2);

	Map<String, Object> findUsersDashboards(String userName);

	boolean setDefaultDashBoard(String dashboardId, String userName);

	Dashboard getDefaultDashBoard(String userName);

	boolean removeDefaultDashBoard(String userName);

	boolean shareDashBoardToUser(String dashboardId, String toUserName);

	int shareDashBoardToUsers(String dashboardId, List<String> toUserName);


	int shareDashBoardToRoles(String dashboardId, List<String> role);

	Dashboard save(Dashboard dashboard, String userName);

	int removeDashBoardFromRoles(List<String> roleIdList, String object);

	Dashboard removeDashBoardFromUser(String userId, String dashboardId, String type);

	int removeDashBoardFromUsers(List<String> userNames, String dashboardId, String typr);

}
