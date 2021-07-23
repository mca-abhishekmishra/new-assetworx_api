package com.softtek.assetworx_api.controller;

import static com.softtek.assetworx_api.util.Constants.*;

import java.util.List;

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

import com.softtek.assetworx_api.entity.UserAuthority;
import com.softtek.assetworx_api.exception.GenericRestException;
import com.softtek.assetworx_api.service.UserAuthorityService;

@RestController
@RequestMapping("/authority")
public class UserAuthorityController {

	@Autowired
	UserAuthorityService authorityService;

	@GetMapping("/{id}")
	private ResponseEntity<?> findById(@PathVariable("id") String userId) {
		System.out.println("findById");
		UserAuthority userAuthority = authorityService.findById(userId);
		if (userAuthority != null) {
			return new ResponseEntity<UserAuthority>(userAuthority, HttpStatus.OK);
		}
		throw new GenericRestException(AUTHORITY_NOTFOUND_ID + userId, HttpStatus.NOT_FOUND);
	}
	
	@GetMapping("/findAll")
	private ResponseEntity<?> findAll() {
		System.out.println("all");
		List<UserAuthority> privilege = authorityService.findAll();
		if (privilege != null && privilege.size() > 0) {
			return new ResponseEntity<List<UserAuthority>>(privilege, HttpStatus.OK);
		}
		throw new GenericRestException(PRIVILEGES_NOTFOUND , HttpStatus.NOT_FOUND);
	}

	@PostMapping("/")
	private ResponseEntity<?> save(@RequestBody UserAuthority userAuthority) {
		System.out.println("save");
		UserAuthority createduserAuthority = authorityService.save(userAuthority);
		if (createduserAuthority != null) {
			return new ResponseEntity<UserAuthority>(createduserAuthority, HttpStatus.CREATED);
		}
		throw new GenericRestException(AUTHORITY_NOTSAVED, HttpStatus.BAD_REQUEST);
	}

	@PutMapping("/")
	private ResponseEntity<?> update(@RequestBody UserAuthority userAuthority) {
		System.out.println("update");
		UserAuthority updateduserAuthority = authorityService.update(userAuthority);
		if (updateduserAuthority != null) {
			return new ResponseEntity<UserAuthority>(updateduserAuthority, HttpStatus.OK);
		}
		throw new GenericRestException(AUTHORITY_NOTUPDATED, HttpStatus.BAD_REQUEST);
	}

	@DeleteMapping("/{id}")
	private ResponseEntity<?> delete(@PathVariable("id") String id) {
		System.out.println("delete");
		if (authorityService.delete(id)) {
			return new ResponseEntity<String>(AUTHORITY_DELETED, HttpStatus.OK);
		}
		throw new GenericRestException(AUTHORITY_NOTDELETED, HttpStatus.BAD_REQUEST);
	}
}
