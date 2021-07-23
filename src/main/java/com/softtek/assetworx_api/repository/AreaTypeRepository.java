package com.softtek.assetworx_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.softtek.assetworx_api.entity.AreaType;

public interface AreaTypeRepository extends JpaRepository<AreaType, String> {

	Optional<AreaType> findByName(String name);

}
