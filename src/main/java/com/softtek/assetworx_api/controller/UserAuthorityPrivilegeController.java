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

import com.softtek.assetworx_api.entity.UserAuthorityPrivilege;
import com.softtek.assetworx_api.exception.GenericRestException;
import com.softtek.assetworx_api.service.UserAuthorityPrivilegeService;

@RestController
@RequestMapping("/privilege")
public class UserAuthorityPrivilegeController {

	@Autowired
	UserAuthorityPrivilegeService privilegeService;

	@GetMapping("/{id}")
	private ResponseEntity<?> findById(@PathVariable("id") String userId) {
		System.out.println("findById");
		UserAuthorityPrivilege privilege = privilegeService.findById(userId);
		if (privilege != null) {
			return new ResponseEntity<UserAuthorityPrivilege>(privilege, HttpStatus.OK);
		}
		throw new GenericRestException(PRIVILEGE_NOTFOUND_ID + userId, HttpStatus.NOT_FOUND);
	}
	
	@GetMapping("/findAll")
	private ResponseEntity<?> findAll() {
		System.out.println("all");
		List<UserAuthorityPrivilege> privilege = privilegeService.findAll();
		if (privilege != null && privilege.size() > 0) {
			return new ResponseEntity<List<UserAuthorityPrivilege>>(privilege, HttpStatus.OK);
		}
		throw new GenericRestException(PRIVILEGES_NOTFOUND , HttpStatus.NOT_FOUND);
	}

	@PostMapping("/")
	private ResponseEntity<?> save(@RequestBody UserAuthorityPrivilege privilege) {
		UserAuthorityPrivilege createdPrivilege = privilegeService.save(privilege);
		System.out.println("-----------------+" + createdPrivilege);
		if (createdPrivilege != null) {
			return new ResponseEntity<UserAuthorityPrivilege>(createdPrivilege, HttpStatus.CREATED);
		}
		throw new GenericRestException(PRIVILEGE_NOTSAVED, HttpStatus.BAD_REQUEST);
	}

	@PutMapping("/")
	private ResponseEntity<?> update(@RequestBody UserAuthorityPrivilege privilege) {
		UserAuthorityPrivilege updatedPrivilege = privilegeService.update(privilege);
		if (updatedPrivilege != null) {
			return new ResponseEntity<UserAuthorityPrivilege>(updatedPrivilege, HttpStatus.OK);
		}
		throw new GenericRestException(PRIVILEGE_NOTUPDATED, HttpStatus.BAD_REQUEST);
	}

	@DeleteMapping("/{id}")
	private ResponseEntity<?> delete(@PathVariable("id") String id) {
		if (privilegeService.delete(id)) {
			return new ResponseEntity<String>(PRIVILEGE_DELETED, HttpStatus.OK);
		}
		throw new GenericRestException(PRIVILEGE_NOTDELETED, HttpStatus.BAD_REQUEST);
	}

}
