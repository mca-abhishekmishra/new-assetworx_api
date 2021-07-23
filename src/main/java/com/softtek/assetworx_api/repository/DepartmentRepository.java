package com.softtek.assetworx_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.softtek.assetworx_api.entity.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, String> {

	Optional<Department> findByName(String name);

	Department findFirstByNameAndIdNotLike(String name, String id);

}
