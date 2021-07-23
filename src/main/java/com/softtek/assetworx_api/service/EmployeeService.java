package com.softtek.assetworx_api.service;

import com.softtek.assetworx_api.entity.Employee;

public interface EmployeeService {

	Employee findById(String id);

	Employee findByName(String name);

	Employee save(Employee employee);

	Employee update(Employee employee);

	boolean delete(String id);

	Employee findByIsid(String isid);

	Employee removeEmployee(Employee employee);

	Employee findByEmail(String email);

}
