package com.softtek.assetworx_api.serviceImpl;

import static com.softtek.assetworx_api.util.Constants.EMPLOYEE_NOTFOUND_ID;
import static com.softtek.assetworx_api.util.Constants.USER_NOTFOUND_ID;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.softtek.assetworx_api.entity.AssetworxUser;
import com.softtek.assetworx_api.entity.Employee;
import com.softtek.assetworx_api.entity.UserAuthority;
import com.softtek.assetworx_api.exception.GenericRestException;
import com.softtek.assetworx_api.exception.InvalidEntityException;
import com.softtek.assetworx_api.repository.UserRepository;
import com.softtek.assetworx_api.service.EmployeeService;
import com.softtek.assetworx_api.service.UserAuthorityService;
import com.softtek.assetworx_api.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	UserAuthorityService authorityService;

	@Autowired
	EmployeeService employeeService;

	@Autowired
	Validator validator;

	@Override
	public AssetworxUser findByUserId(String userId) {
		if (userId == null) {
			return null;
		}
		return userRepository.findById(userId).orElse(null);
	}

	@Override
	public AssetworxUser save(AssetworxUser user) {
		return userRepository.save(user);
	}

	@Override
	public AssetworxUser update(String id, Set<String> authorityIds) {
		AssetworxUser userFromDb = findByUserId(id);
		if (userFromDb == null) {
			throw new GenericRestException(USER_NOTFOUND_ID + " " + id, HttpStatus.NOT_FOUND);
		}
		Set<UserAuthority> authorties = authorityIds.stream().map(authorityId -> authorityService.findById(authorityId))
				.filter(p -> !Objects.isNull(p)).collect(Collectors.toSet());

		userFromDb.setAuthorties(authorties);
		return userRepository.save(userFromDb);
	}

	@Override
	public boolean delete(String id) {
		AssetworxUser user = findByUserId(id);
		if (user == null) {
			throw new GenericRestException(USER_NOTFOUND_ID + id, HttpStatus.NOT_FOUND);
		}
		user.setActive(false);
		user.setFullName(user.getFullName() + "~" + System.nanoTime());
		user.setUserName(user.getUserName() + "~" + System.nanoTime());
		user.setEmail(user.getEmail() + "~" + System.nanoTime());
		if (userRepository.save(user) != null) {
			return true;
		}
		return false;
	}

	public boolean validate(AssetworxUser user) {
		List<String> messages = validator.validate(user).stream().map(e -> e.getPropertyPath() + ":" + e.getMessage())
				.collect(Collectors.toList());
		AssetworxUser f = userRepository.findFirstByUserNameAndUserIdNotLike(user.getUserName(), user.getUserId());
		if (f != null) {
			messages.add("name:User already exists for the  given username - " + user.getUserName());
		}
		if (!messages.isEmpty()) {
			throw new InvalidEntityException("User could not be saved.", messages);
		}
		return true;

	}

	@Override
	public AssetworxUser findByUserName(String userName) {
		return userRepository.findByUserName(userName);
	}

	@Override
	public AssetworxUser save(String id, Set<String> authorityIds) {
		Employee employee = employeeService.findById(id);
		if (employee == null) {
			throw new GenericRestException(EMPLOYEE_NOTFOUND_ID + " " + id, HttpStatus.NOT_FOUND);
		}
		AssetworxUser newUser = new AssetworxUser();
		newUser.setUserId("");
		newUser.setActive(true);
		newUser.setEmail(employee.getEmail());
		newUser.setUserName(employee.getEmployeeId());
		newUser.setFullName(employee.getName());
		Set<UserAuthority> authorties = authorityIds.stream().map(authorityId -> {
			System.out.println(authorityId);
			System.out.println(authorityService.findById(authorityId));
			return authorityService.findById(authorityId);
		}).filter(p -> !Objects.isNull(p)).collect(Collectors.toSet());
		System.out.println(authorties);
		newUser.setAuthorties(authorties);
		if (validate(newUser)) {
			return userRepository.save(newUser);
		}
		return null;
	}

	@Override
	public AssetworxUser findByEmail(String email) {
		return userRepository.findByEmailAndIsActive(email, true);
	}

	@Override
	public void save(List<AssetworxUser> users) {
		userRepository.saveAll(users);
	}

	@Override
	public List<AssetworxUser> findByUserNameIn(List<String> toUserNames) {
		return userRepository.findByUserNameIn(toUserNames);
	}

	@Override
	public List<AssetworxUser> findUsersByRoleIn(List<String> roleIds) {
		return userRepository.findUsersByRoleIn(roleIds);
	}

	@Override
	public List<AssetworxUser> findUserWithSharedDashBoardIdAndRoleIdIn(List<String> roleIdList, String dashBoardId) {
		return userRepository.findUserWithSharedDashBoardIdAndRoleIdIn(roleIdList, dashBoardId);
	}

	@Override
	public List<AssetworxUser> findUserWithSharedReportIdAndRoleIdIn(List<String> roles, String reportId) {
		return userRepository.findUserWithSharedReportIdAndRoleIdIn(roles, reportId);
	}

	@Override
	public List<AssetworxUser> findUsersWithSharedDashBoardId(String dashBoardId) {
		return userRepository.findUsersWithSharedDashBoardId(dashBoardId);
	}

	@Override
	public List<AssetworxUser> findUsersWithSharedReportId(String reportId) {
		return userRepository.findUsersWithSharedReportId(reportId);
	}

	@Override
	public List<AssetworxUser> findAll() {
		// TODO Auto-generated method stub
		return userRepository.findAll().stream().map(u -> {
			u.setAuthorties(null);
			return u;
		}).collect(Collectors.toList()); 
	}

}
