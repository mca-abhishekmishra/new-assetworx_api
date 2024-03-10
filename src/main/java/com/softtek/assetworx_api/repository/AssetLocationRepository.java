package com.softtek.assetworx_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.softtek.assetworx_api.entity.AssetLocation;

public interface AssetLocationRepository extends JpaRepository<AssetLocation, String> {

	Optional<AssetLocation> findByName(String name);

}
