package com.softtek.assetworx_api.controller;

import static com.softtek.assetworx_api.util.Constants.USER_DELETED;
import static com.softtek.assetworx_api.util.Constants.USER_NOTDELETED;
import static com.softtek.assetworx_api.util.Constants.USER_NOTFOUND_ID;
import static com.softtek.assetworx_api.util.Constants.USER_NOTSAVED;
import static com.softtek.assetworx_api.util.Constants.USER_NOTUPDATED;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import org.springframework.web.bind.annotation.RestController;

import com.softtek.assetworx_api.entity.AssetworxUser;
import com.softtek.assetworx_api.exception.GenericRestException;
import com.softtek.assetworx_api.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	UserService userService;

	@GetMapping("/{id}")
	private ResponseEntity<?> findById(@PathVariable("id") String userId) {
		System.out.println("findById");
		AssetworxUser user = userService.findByUserId(userId);
		if (user != null) {
			return new ResponseEntity<AssetworxUser>(user, HttpStatus.OK);
		}
		throw new GenericRestException(USER_NOTFOUND_ID + userId, HttpStatus.NOT_FOUND);
	}

	@GetMapping("/list")
	private ResponseEntity<?> findAll() {
		System.out.println("findAll");
		return new ResponseEntity<List<AssetworxUser>>( userService.findAll(), HttpStatus.OK);
	}

	@GetMapping("/findByUserName/{userName}")
	private ResponseEntity<?> findByName(@PathVariable("userName") String userName) {
		System.out.println("findByUserName");
		AssetworxUser user = userService.findByUserName(userName);
		if (user != null) {
			return new ResponseEntity<AssetworxUser>(user, HttpStatus.OK);
		}
		return new ResponseEntity<AssetworxUser>(HttpStatus.NOT_FOUND);
	}

	@GetMapping("/findByEmail/{email}")
	private ResponseEntity<?> findByEmail(@PathVariable("email") String email) {
		System.out.println("email");
		AssetworxUser user = userService.findByEmail(email);
		if (user != null) {
			return new ResponseEntity<AssetworxUser>(user, HttpStatus.OK);
		}
		return new ResponseEntity<AssetworxUser>(HttpStatus.NOT_FOUND);
	}

	@PostMapping("/")
	private ResponseEntity<?> save(@RequestBody Map<String, Object> data) {
		Set<String> authorityIds = new HashSet<String>();
		authorityIds.addAll((List<String>) data.get("authorityIds"));
		AssetworxUser createdUser = userService.save((String) data.get("id"), authorityIds);
		if (createdUser != null) {
			return new ResponseEntity<AssetworxUser>(createdUser, HttpStatus.CREATED);
		}
		throw new GenericRestException(USER_NOTSAVED, HttpStatus.BAD_REQUEST);
	}

	@PutMapping("/")
	private ResponseEntity<?> update(@RequestBody Map<String, Object> data) {
		System.out.println("update");
		Set<String> authorityIds = new HashSet<String>();
		authorityIds.addAll((List<String>) data.get("authorityIds"));
		AssetworxUser updatedUser = userService.update((String) data.get("id"), authorityIds);
		if (updatedUser != null) {
			return new ResponseEntity<AssetworxUser>(updatedUser, HttpStatus.OK);
		}
		throw new GenericRestException(USER_NOTUPDATED, HttpStatus.BAD_REQUEST);
	}

	@DeleteMapping("/{id}")
	private ResponseEntity<?> delete(@PathVariable("id") String id) {
		System.out.println("delete");
		if (userService.delete(id)) {
			return new ResponseEntity<String>(USER_DELETED, HttpStatus.OK);
		}
		throw new GenericRestException(USER_NOTDELETED, HttpStatus.BAD_REQUEST);
	}

}
