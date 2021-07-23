package com.softtek.assetworx_api.service;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.softtek.assetworx_api.entity.AssetworxUser;

public interface UserService {

	AssetworxUser findByUserId(String userId);

	// User save(User User);

	// User update(User User);

	boolean delete(String id);

	AssetworxUser findByUserName(String userName);

	List<AssetworxUser> findByUserNameIn(List<String> toUserNames);

	AssetworxUser findByEmail(String email);

	AssetworxUser save(String id, Set<String> authorityIds);

	AssetworxUser update(String userName, Set<String> authorityIds);

	AssetworxUser save(AssetworxUser user);
	
	List<AssetworxUser> findUsersByRoleIn(List<String> roleIds);

	void save(List<AssetworxUser> users);

	List<AssetworxUser> findUserWithSharedDashBoardIdAndRoleIdIn(List<String> roleIdList, String dashBoardId);

	List<AssetworxUser> findUsersWithSharedDashBoardId(String dashBoardId);

	List<AssetworxUser> findUserWithSharedReportIdAndRoleIdIn(List<String> roles, String reportId);
	
	List<AssetworxUser> findUsersWithSharedReportId(@Param("reportId") String reportId);

	List<AssetworxUser> findAll();

	/* AssetworxUser update(AssetworxUser user); */

}
