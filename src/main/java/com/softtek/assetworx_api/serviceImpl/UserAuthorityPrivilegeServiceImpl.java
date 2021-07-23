package com.softtek.assetworx_api.serviceImpl;

import static com.softtek.assetworx_api.util.Constants.*;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.softtek.assetworx_api.entity.UserAuthorityPrivilege;
import com.softtek.assetworx_api.exception.GenericRestException;
import com.softtek.assetworx_api.exception.InvalidEntityException;
import com.softtek.assetworx_api.repository.UserAuthorityPrivilegeRepository;
import com.softtek.assetworx_api.service.UserAuthorityPrivilegeService;

@Service
public class UserAuthorityPrivilegeServiceImpl implements UserAuthorityPrivilegeService {

	@Autowired
	private UserAuthorityPrivilegeRepository privilegeRepository;

	@Autowired
	Validator validator;

	@Override
	public UserAuthorityPrivilege findByName(String privilegeName) {
		return privilegeRepository.findByName(privilegeName).orElse(null);
	}

	@Override
	public UserAuthorityPrivilege save(UserAuthorityPrivilege privilege) {
		privilege.setPrivilegeId("");
		privilege.setName(privilege.getName().toUpperCase());
		if (validate(privilege)) {
			return privilegeRepository.save(privilege);
		}
		return null;
	}

	@Override
	public UserAuthorityPrivilege update(UserAuthorityPrivilege privilege) {
		UserAuthorityPrivilege f = findById(privilege.getPrivilegeId());
		if (f == null) {
			throw new GenericRestException(PRIVILEGES_NOTFOUND + privilege.getName(), HttpStatus.NOT_FOUND);
		} else {
			f.setName(privilege.getName().toUpperCase());
			f.setDescription(privilege.getDescription());
			if (validate(f)) {
				return privilegeRepository.save(privilege);
			}
			return null;
		}
	}

	@Override
	public boolean delete(String id) {
		return true;
	}

	@Override
	public List<UserAuthorityPrivilege> findAll() {
		return privilegeRepository.findAll();
	}

	public boolean validate(UserAuthorityPrivilege privilege) {
		List<String> messages = validator.validate(privilege).stream()
				.map(e -> e.getPropertyPath() + ":" + e.getMessage()).collect(Collectors.toList());
		UserAuthorityPrivilege f = privilegeRepository.findByNameAndPrivilegeIdNotLike(privilege.getName(),privilege.getPrivilegeId());
		if (f != null) {
			messages.add(PRIVILEGE_EXISTS + privilege.getName());
		}
		if (!messages.isEmpty()) {
			throw new InvalidEntityException(PRIVILEGE_NOTSAVED, messages);
		}
		return true;

	}

	@Override
	public UserAuthorityPrivilege findById(String PrivilegeId) {
		return privilegeRepository.findById(PrivilegeId).orElse(null);
	}

}
