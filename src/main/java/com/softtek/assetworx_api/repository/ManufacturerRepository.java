package com.softtek.assetworx_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.softtek.assetworx_api.entity.Manufacturer;

@Repository
public interface ManufacturerRepository extends JpaRepository<Manufacturer, String> {

	Optional<Manufacturer> findByName(String name);

	Optional<Manufacturer> findByIdAndName(String id, String name);

	Manufacturer findFirstByNameAndIdNotLike(String name, String id);

}
