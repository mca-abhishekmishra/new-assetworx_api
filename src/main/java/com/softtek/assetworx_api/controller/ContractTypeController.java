package com.softtek.assetworx_api.controller;

import static com.softtek.assetworx_api.util.Constants.CONTRACT_TYPE_DELETED;
import static com.softtek.assetworx_api.util.Constants.CONTRACT_TYPE_NOTDELETED;
import static com.softtek.assetworx_api.util.Constants.CONTRACT_TYPE_NOTFOUND_ID;
import static com.softtek.assetworx_api.util.Constants.CONTRACT_TYPE_NOTFOUND_NAME;
import static com.softtek.assetworx_api.util.Constants.CONTRACT_TYPE_NOTSAVED;
import static com.softtek.assetworx_api.util.Constants.CONTRACT_TYPE_NOTUPDATED;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.softtek.assetworx_api.entity.ContractType;
import com.softtek.assetworx_api.exception.GenericRestException;
import com.softtek.assetworx_api.service.ContractTypeService;

@Controller
@RequestMapping("/contractType")
public class ContractTypeController {
	
	@Autowired
	ContractTypeService contractTypeService;	

	
	@GetMapping("/{id}")
	private ResponseEntity<?> findById(@PathVariable("id") String id) {
		ContractType contractType = contractTypeService.findById(id);
		if(contractType!=null) {
			return new ResponseEntity<ContractType>(contractType, HttpStatus.OK);
		}
		throw new GenericRestException(CONTRACT_TYPE_NOTFOUND_ID + id, HttpStatus.NOT_FOUND);	
	}

	@GetMapping("/findByName/{name}")
	private ResponseEntity<?> findByName(@PathVariable("name") String name) {
		ContractType contractType = contractTypeService.findByName(name);
		if(contractType!=null) {
			return new ResponseEntity<ContractType>(contractType, HttpStatus.OK);
		}
		throw new GenericRestException(CONTRACT_TYPE_NOTFOUND_NAME + name, HttpStatus.NOT_FOUND);	
	}
	
	@PostMapping("/")
	private ResponseEntity<?> save(@RequestBody ContractType contractType) {
		System.out.println(contractType);
		ContractType createdContractType = contractTypeService.save(contractType);
		if(createdContractType != null) {
			return new ResponseEntity<ContractType>(createdContractType, HttpStatus.CREATED);
		}
		throw new GenericRestException(CONTRACT_TYPE_NOTSAVED, HttpStatus.BAD_REQUEST);	
	} 
	
	@PutMapping("/")
	private ResponseEntity<?> update(@RequestBody ContractType contractType) {
		ContractType updatedContractType = contractTypeService.update(contractType);
		if(updatedContractType != null) {
			return new ResponseEntity<ContractType>(updatedContractType, HttpStatus.OK);
		}
		throw new GenericRestException(CONTRACT_TYPE_NOTUPDATED, HttpStatus.BAD_REQUEST);	
	}
	
	@DeleteMapping("/{id}")
	private ResponseEntity<?> delete(@PathVariable("id") String id) {
		if(contractTypeService.delete(id)) {
			return new ResponseEntity<String>(CONTRACT_TYPE_DELETED, HttpStatus.OK);
		}
		throw new GenericRestException(CONTRACT_TYPE_NOTDELETED, HttpStatus.BAD_REQUEST);	
	}

}
