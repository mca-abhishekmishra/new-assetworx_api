package com.softtek.assetworx_api.controller;

import static com.softtek.assetworx_api.util.Constants.EMPLOYEE_DELETED;
import static com.softtek.assetworx_api.util.Constants.EMPLOYEE_NOTDELETED;
import static com.softtek.assetworx_api.util.Constants.EMPLOYEE_NOTFOUND_ID;
import static com.softtek.assetworx_api.util.Constants.EMPLOYEE_NOTFOUND_NAME;
import static com.softtek.assetworx_api.util.Constants.EMPLOYEE_NOTSAVED;
import static com.softtek.assetworx_api.util.Constants.EMPLOYEE_NOTUPDATED;

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

import com.softtek.assetworx_api.entity.Employee;
import com.softtek.assetworx_api.exception.GenericRestException;
import com.softtek.assetworx_api.service.EmployeeService;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

	@Autowired
	EmployeeService employeeService;


	@GetMapping("/{id}")
	private ResponseEntity<?> findById(@PathVariable("id") String id) {
		Employee employee = employeeService.findById(id);
		if(employee!=null) {
			return new ResponseEntity<Employee>(employee, HttpStatus.OK);
		}
		throw new GenericRestException(EMPLOYEE_NOTFOUND_ID + id, HttpStatus.NOT_FOUND);	
	}

	@GetMapping("/findByName/{name}")
	private ResponseEntity<?> findByName(@PathVariable("name") String name) {
		Employee employee = employeeService.findByName(name);
		if(employee!=null) {
			return new ResponseEntity<Employee>(employee, HttpStatus.OK);
		}
		throw new GenericRestException(EMPLOYEE_NOTFOUND_NAME + name, HttpStatus.NOT_FOUND);	
	}
	
	@GetMapping("/getByEmail/{email}")
	private ResponseEntity<?> findByEmail(@PathVariable("email") String email) {
		Employee employee = employeeService.findByEmail(email);
		if(employee!=null) {
			return new ResponseEntity<Employee>(employee, HttpStatus.OK);
		}
		throw new GenericRestException("Employee could not be found for the given email id: " + email, HttpStatus.NOT_FOUND);	
	}
	
	@PostMapping("/")
	private ResponseEntity<?> save(@RequestBody Employee employee) {
		System.out.println("emp :"+ employee);
		System.out.println("mgr :" +employee.getReportingTo());
		System.out.println("project :" +employee.getProject());
		Employee createdemployee = employeeService.save(employee);
		if(createdemployee != null) {
			return new ResponseEntity<Employee>(createdemployee, HttpStatus.CREATED);
		}
		throw new GenericRestException(EMPLOYEE_NOTSAVED, HttpStatus.BAD_REQUEST);	
	} 
	
	@PutMapping("/")
	private ResponseEntity<?> update(@RequestBody Employee employee) {
		Employee updatedemployee = employeeService.update(employee);
		if(updatedemployee != null) {
			return new ResponseEntity<Employee>(updatedemployee, HttpStatus.OK);
		}
		throw new GenericRestException(EMPLOYEE_NOTUPDATED, HttpStatus.BAD_REQUEST);	
	}
	
	@PutMapping("/removeEmployee")
	private ResponseEntity<?> removeEmployee(@RequestBody Employee employee) {
		Employee removeEmployee = employeeService.removeEmployee(employee);
		if(removeEmployee != null) {
			return new ResponseEntity<Employee>(removeEmployee, HttpStatus.OK);
		}
		throw new GenericRestException("Employee Could not be removed", HttpStatus.BAD_REQUEST);	
	}
	
	@DeleteMapping("/{id}")
	private ResponseEntity<?> delete(@PathVariable("id") String id) {
		if(employeeService.delete(id)) {
			return new ResponseEntity<String>(EMPLOYEE_DELETED, HttpStatus.OK);
		}
		throw new GenericRestException(EMPLOYEE_NOTDELETED, HttpStatus.BAD_REQUEST);	
	}
	
}
