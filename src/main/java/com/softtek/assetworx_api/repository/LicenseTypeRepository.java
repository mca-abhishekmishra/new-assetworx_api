package com.softtek.assetworx_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.softtek.assetworx_api.entity.LicenseType;

public interface LicenseTypeRepository extends JpaRepository<LicenseType, String> {

	Optional<LicenseType> findByName(String name);

}
