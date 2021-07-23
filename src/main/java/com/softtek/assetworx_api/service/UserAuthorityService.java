package com.softtek.assetworx_api.service;

import java.util.List;

import com.softtek.assetworx_api.entity.UserAuthority;

public interface UserAuthorityService {

	UserAuthority findByName(String authorityName);
	
	UserAuthority findById(String authorityId);

	UserAuthority save(UserAuthority authority);

	UserAuthority update(UserAuthority authority);

	boolean delete(String id);

	List<UserAuthority> findAll();

}
