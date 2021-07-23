package com.softtek.assetworx_api.service;

import com.softtek.assetworx_api.entity.Department;

public interface DepartmentService {

	Department findById(String id);

	Department findByName(String name);

	Department save(Department department);

	Department update(Department department);

	boolean delete(String id);

}
