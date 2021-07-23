package com.softtek.assetworx_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.softtek.assetworx_api.entity.AssetModel;

@Repository
public interface AssetModelRepository extends JpaRepository<AssetModel, String> {

	Optional<AssetModel> findByName(String name);

	Optional<AssetModel> findByIdAndName(String id, String name);
	
	AssetModel findFirstByNameAndIdNotLike(String name, String id);
}
