package com.softtek.assetworx_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.softtek.assetworx_api.entity.AssetAssignmentType;

public interface AssetAssignmentTypeRepository extends JpaRepository<AssetAssignmentType, String> {

	Optional<AssetAssignmentType> findByName(String name);

}
