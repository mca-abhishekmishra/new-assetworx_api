package com.softtek.assetworx_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.softtek.assetworx_api.entity.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String> {

	Optional<Employee> findByName(String name);

	Employee findFirstByEmailAndIdNotLike(String email, String id);
	
	Employee findFirstByEmployeeIdAndIdNotLike(String email, String id);
	
	Employee findFirstByIsidAndIdNotLike(String email, String id);

	Optional<Employee> findByIsid(String isid);

	Optional<Employee> findByEmail(String email);

}
