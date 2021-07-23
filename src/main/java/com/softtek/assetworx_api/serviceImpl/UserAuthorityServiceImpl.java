package com.softtek.assetworx_api.serviceImpl;

import static com.softtek.assetworx_api.util.Constants.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.softtek.assetworx_api.entity.UserAuthority;
import com.softtek.assetworx_api.entity.UserAuthorityPrivilege;
import com.softtek.assetworx_api.exception.GenericRestException;
import com.softtek.assetworx_api.exception.InvalidEntityException;
import com.softtek.assetworx_api.repository.UserAuthorityRepository;
import com.softtek.assetworx_api.service.UserAuthorityService;

@Service
public class UserAuthorityServiceImpl implements UserAuthorityService {

	@Autowired
	Validator validator;

	@Autowired
	private UserAuthorityRepository authorityRepository;

	@Autowired
	private UserAuthorityPrivilegeServiceImpl privilgesSerivce;

	@Override
	public UserAuthority findByName(String authorityName) {
		return authorityRepository.findByName(authorityName).orElse(null);
	}

	@Override
	public UserAuthority save(UserAuthority authority) {
		System.out.println(authority);
		if (validate(authority)) {
			Set<UserAuthorityPrivilege> privileges = authority.getPrivileges().stream()
					.map(p -> privilgesSerivce.findById(p.getPrivilegeId())).filter(p -> !Objects.isNull(p))
					.collect(Collectors.toSet());
			authority.setPrivileges(privileges);
			return authorityRepository.save(authority);
		}
		return null;
	}

	private boolean validate(UserAuthority authority) {
		List<String> messages = validator.validate(authority).stream()
				.map(e -> e.getPropertyPath() + ":" + e.getMessage()).collect(Collectors.toList());
		UserAuthority f = findByName(authority.getName());
		if (f != null) {
			messages.add(AUTHORITY_NOTSAVED + authority.getName());
		}
		if (!messages.isEmpty()) {
			throw new InvalidEntityException(AUTHORITY_NOTSAVED, messages);
		}
		return true;
	}

	@Override
	public UserAuthority update(UserAuthority authority) {
		System.out.println(authority);
		UserAuthority f = findById(authority.getAuthorityId());
		if (f == null) {
			throw new GenericRestException(AUTHORITY_NOTFOUND_NAME + authority.getName(), HttpStatus.NOT_FOUND);
		} else {

			f.setName(authority.getName());
			f.setDescription(authority.getDescription());
			Set<UserAuthorityPrivilege> privileges = authority.getPrivileges().stream()
					.map(p -> privilgesSerivce.findById(p.getPrivilegeId())).filter(p -> !Objects.isNull(p))
					.collect(Collectors.toSet());
			f.setPrivileges(privileges);

			return authorityRepository.save(f);
		}
	}

	@Override
	public boolean delete(String id) {
		Optional<UserAuthority> opt = authorityRepository.findById(id);
		if (opt.isPresent()) {
			authorityRepository.delete(opt.get());
		} else {
			throw new GenericRestException(AUTHORITY_NOTFOUND, HttpStatus.NOT_FOUND);
		}
		return true;
	}

	@Override
	public UserAuthority findById(String authorityId) {
		return authorityRepository.findById(authorityId).orElse(null);
	}

	@Override
	public List<UserAuthority> findAll() {
		return authorityRepository.findAll();
	}

}
