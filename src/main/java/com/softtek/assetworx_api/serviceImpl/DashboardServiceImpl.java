package com.softtek.assetworx_api.serviceImpl;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.collections4.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.softtek.assetworx_api.entity.AssetworxUser;
import com.softtek.assetworx_api.entity.Dashboard;
import com.softtek.assetworx_api.entity.Report;
import com.softtek.assetworx_api.exception.GenericRestException;
import com.softtek.assetworx_api.repository.DashboardRepository;
import com.softtek.assetworx_api.service.DashboardService;
import com.softtek.assetworx_api.service.ReportService;
import com.softtek.assetworx_api.service.UserService;
import com.softtek.assetworx_api.util.Constants;

@Service
public class DashboardServiceImpl implements DashboardService {

	@Autowired
	DashboardRepository dashboardRepository;

	@Autowired
	UserService userService;

	@Autowired
	ReportService reportService;

	@Override
	public List<Dashboard> findAll() {
		return dashboardRepository.findAll();
	}

	@Override
	public Dashboard findById(String id) {
		Optional<Dashboard> d = dashboardRepository.findById(id); 
		if (d.isPresent()) {
			return d.get();
		}
		return null;
	}

	@Override
	public Dashboard save(Dashboard a) {
		return dashboardRepository.save(a);
	}

	@Override
	public Dashboard update(Dashboard a) {
		Dashboard d = findById(a.getId());
		if (d == null) {
			throw new GenericRestException("Dashboard could not be updated.", HttpStatus.BAD_REQUEST);
		}
		d.setName(a.getName());
		d.setDescription(a.getDescription());
		return dashboardRepository.save(d);
	}

	@Override
	public Report addReport(String reportId, String dashboardId) {
		Dashboard dashboard = findById(dashboardId);
		Report report = reportService.findById(reportId);
		if (report == null || dashboard == null) {
			throw new GenericRestException("Report could not be added.", HttpStatus.BAD_REQUEST);
		}
		dashboard.addReport(report);
		dashboardRepository.save(dashboard);
		return report;
	}

	@Override
	public Report removeReport(String reportId, String dashboardId) {
		Dashboard dashboard = findById(dashboardId);
		Report report = reportService.findById(reportId);
		// List<Report> reportList = dashboard.getReportList().stream().filter(r ->
		// r.getId() != report.getId()).collect(Collectors.toList());
		// dashboard.setReportList(reportList);
		for (Report r : dashboard.getReportList()) {
			if (r.getId().equals(reportId)) {
				dashboard.getReportList().remove(r);
				break;
			}
		}
		dashboard.setReportList(dashboard.getReportList());
		dashboardRepository.save(dashboard);
		return report;
	}

	@Override
	public Map<String, Object> findUsersDashboards(String userName) {
		System.out.println("user ID ----- > " + userName);
		AssetworxUser user = userService.findByUserName(userName);
		// System.out.println("user ------> "+ user);
		if (user == null) {
			throw new GenericRestException("User Dashboard could not be Found.", HttpStatus.BAD_REQUEST);
		}
		Map<String, Object> data = new HashedMap<>();
		// will remove the duplicates
		user.getSharedDashBoards().removeAll(user.getUserDashBoards());
		data.put("sharedDashboards", user.getSharedDashBoards());
		data.put("myDashboard", user.getUserDashBoards());

		// change it later after disscussion with the team
		data.put("defaultDashboard", getDefaultDashBoard(userName));
		return data;
	}

	@Override
	public boolean setDefaultDashBoard(String dashboardId, String userName) {
		AssetworxUser user = userService.findByUserName(userName);
		Dashboard dashboard = findById(dashboardId);
		if (user == null || dashboard == null) {
			throw new GenericRestException("User Dashboard could not be Found.", HttpStatus.BAD_REQUEST);
		}
		user.setDefaultDashBoard(dashboard);
		return userService.save(user) != null ? true : false;
	}

	@Override
	public boolean removeDefaultDashBoard(String userName) {
		AssetworxUser user = userService.findByUserName(userName);
		if (user == null) {
			throw new GenericRestException("User Dashboard could not be Found.", HttpStatus.BAD_REQUEST);
		}
		user.setDefaultDashBoard(null);
		return userService.save(user) != null ? true : false;
	}

	@Override
	public Dashboard getDefaultDashBoard(String userName) {
		// find the user default dash board in not available provide with a default one
		// based on role of the user
		AssetworxUser user = userService.findByUserName(userName);
		if (user == null) {
			throw new GenericRestException("User could not be Found.", HttpStatus.BAD_REQUEST);
		}
		if (user.getDefaultDashBoard() == null) {
			return findById("main-dashboard");
			/*
			 * // set a default one based on the role if no role then ? if
			 * (user.getAuthorties().size() <= 0) { return findById("defaultDashBoard"); }
			 * else { // based on authority add dashboard // find the authority with max
			 * authority TreeSet<UserAuthority> authsTree = new TreeSet<>( (a1, a2) ->
			 * a1.getPrivileges().size() < a2.getPrivileges().size() ? 1 : -1);
			 * authsTree.addAll(user.getAuthorties()); return
			 * findById(authsTree.pollFirst().getName()); }
			 */
		}
		return user.getDefaultDashBoard();
	}

	@Override
	public boolean shareDashBoardToUser(String dashboardId, String toUserName) {
		AssetworxUser user = userService.findByUserName(toUserName);
		Dashboard dashboard = findById(dashboardId);
		if (user == null || dashboard == null) {
			throw new GenericRestException("User Dashboard could not be Shared.", HttpStatus.BAD_REQUEST);
		}
		if (user.getSharedDashBoards().add(dashboard)) {
			userService.save(user);
			return true;
		}

		throw new GenericRestException("User already have access to the Dashboard.", HttpStatus.BAD_REQUEST);
	}

	@Override
	public int shareDashBoardToUsers(String dashboardId, List<String> toUserNames) {
		List<AssetworxUser> users = userService.findByUserNameIn(toUserNames);
		Dashboard dashboard = findById(dashboardId);
		if (dashboard == null) {
			throw new GenericRestException("User Dashboard could not be Shared.", HttpStatus.BAD_REQUEST);
		}
		int updatedUsersCount = 0;
		List<AssetworxUser> removedUsers = dashboard.getUsers().stream().filter(u -> !users.contains(u))
				.collect(Collectors.toList());
		System.out.println(removedUsers);
		for (AssetworxUser user : removedUsers) {
			if(user.getDefaultDashBoard() != null && user.getDefaultDashBoard().getId().equals(dashboard.getId())) {
				user.setDefaultDashBoard(null);
			}
		}

		dashboard.setUsers(new HashSet<AssetworxUser>(users));
		userService.save(users);
		return dashboard.getUsers().size();
	}

	@Override
	@Transactional(rollbackOn = Exception.class)
	public int shareDashBoardToRoles(String dashboardId, List<String> roleIds) {
		int updatedUsersCount = 0;
		List<AssetworxUser> allusers = userService.findUsersByRoleIn(roleIds);
		Dashboard sharedDashbaord = findById(dashboardId);
		for (AssetworxUser user : allusers) {
			if (user.getSharedDashBoards().add(sharedDashbaord)) {
				System.out.println("sharing to : " + user.getUserName());
				updatedUsersCount++;
				userService.save(user);
			}
		}
		return updatedUsersCount;
	}

	@Override
	public Dashboard save(Dashboard dashboard, String userId) {
		AssetworxUser user = userService.findByUserName(userId);
		user.getUserDashBoards().add(dashboard);

		dashboard = save(dashboard);
		userService.save(user);

		return dashboard;
	}

	@Override
	public Dashboard removeDashBoardFromUser(String userId, String dashboardId, String type) {
		AssetworxUser user = userService.findByUserName(userId);
		Dashboard dashboard = findById(dashboardId);

		if (user == null || dashboard == null) {
			throw new GenericRestException("User Dashboard could not be removed.", HttpStatus.BAD_REQUEST);
		}

		if (type.equals(Constants.DASHBORED_USER)) {
			if (user.getUserDashBoards().remove(findById(dashboardId))) {
				userService.save(user);
				return dashboard;
			}
		} else if (type.equals(Constants.DASHBORED_SHARED)) {
			if (user.getSharedDashBoards().remove(findById(dashboardId))) {
				userService.save(user);
				return dashboard;
			}
		} else if (type.equals(Constants.DASHBORED_DEFAULT)) {
			user.setDefaultDashBoard(null);
			userService.save(user);
			return dashboard;
		}
		return null;

	}

	@Override
	@Transactional(rollbackOn = Exception.class)
	public int removeDashBoardFromRoles(List<String> roleIdList, String dashBoardId) {
		int updatedCount = 0;
		List<AssetworxUser> users = userService.findUserWithSharedDashBoardIdAndRoleIdIn(roleIdList, dashBoardId);

		Dashboard dashboard = findById(dashBoardId.toString());

		if (users.size() <= 0 || dashboard == null) {
			throw new GenericRestException("User Dashboard could not be removed.", HttpStatus.BAD_REQUEST);
		}
		for (AssetworxUser user : users) {
			System.out.println("user : +++++  >  " + user);
			updatedCount++;
			// user.getSharedDashBoards().remove(dashboard);
		}
		// userService.save(users);
		return updatedCount;

	}

	@Override
	public int removeDashBoardFromUsers(List<String> userNames, String dashboardId, String type) {
		List<AssetworxUser> users = userService.findByUserNameIn(userNames);
		Dashboard dashboard = findById(dashboardId);
		int updatedCount = 0;
		if (users.size() <= 0 || dashboard == null) {
			throw new GenericRestException("User Dashboard could not be removed.", HttpStatus.BAD_REQUEST);
		}

		for (AssetworxUser user : users) {
			if (type.equals(Constants.DASHBORED_USER)) {
				if (user.getUserDashBoards().remove(dashboard)) {
					updatedCount++;
					userService.save(users);
				}
			} else if (type.equals(Constants.DASHBORED_SHARED)) {
				if (user.getSharedDashBoards().remove(dashboard)) {
					updatedCount++;
					userService.save(users);
				}
			} else if (type.equals(Constants.DASHBORED_DEFAULT)) {
				user.setDefaultDashBoard(null);
				updatedCount++;
				userService.save(users);
			}
		}
		return updatedCount;
	}
}
