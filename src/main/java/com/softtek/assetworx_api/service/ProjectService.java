package com.softtek.assetworx_api.service;

import com.softtek.assetworx_api.entity.Project;

public interface ProjectService {

	Project findById(String id);

	Project findByName(String name);

	Project save(Project project);

	Project update(Project project);

	boolean delete(String id);

}
