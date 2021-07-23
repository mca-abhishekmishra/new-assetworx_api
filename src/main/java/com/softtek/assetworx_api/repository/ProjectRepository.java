package com.softtek.assetworx_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.softtek.assetworx_api.entity.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, String> {

	Optional<Project> findByName(String name);

	Project findFirstByNameAndIdNotLike(String name, String id);
	
	Project findFirstByProjectIdAndIdNotLike(String projectId, String id);

}
