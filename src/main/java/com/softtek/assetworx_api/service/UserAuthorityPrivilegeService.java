package com.softtek.assetworx_api.service;

import java.util.List;

import com.softtek.assetworx_api.entity.UserAuthorityPrivilege;

public interface UserAuthorityPrivilegeService {

	UserAuthorityPrivilege findByName(String PrivilegeName);
	
	UserAuthorityPrivilege findById(String PrivilegeId);

	UserAuthorityPrivilege save(UserAuthorityPrivilege Privilege);

	UserAuthorityPrivilege update(UserAuthorityPrivilege Privilege);

	boolean delete(String id);

	List<UserAuthorityPrivilege> findAll();

}
