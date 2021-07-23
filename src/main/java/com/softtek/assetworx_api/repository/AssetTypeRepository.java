package com.softtek.assetworx_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.softtek.assetworx_api.entity.AssetType;

@Repository
public interface AssetTypeRepository extends JpaRepository<AssetType, String> {

	Optional<AssetType> findByName(String name);

	Optional<AssetType> findByIdAndName(String id, String name);

	AssetType findFirstByNameAndIdNotLike(String name, String id);
}
