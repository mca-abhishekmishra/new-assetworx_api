package com.softtek.assetworx_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.softtek.assetworx_api.entity.OperatingSystem;

@Repository
public interface OperatingSystemRepository extends JpaRepository<OperatingSystem, String> {

	Optional<OperatingSystem> findByName(String name);

	Optional<OperatingSystem> findByIdAndName(String id, String name);
	
	OperatingSystem findFirstByNameAndIdNotLike(String name, String id);
	
}
