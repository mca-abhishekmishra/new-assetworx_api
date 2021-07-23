package com.softtek.assetworx_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.softtek.assetworx_api.entity.CostCenter;

public interface CostCenterRepository extends JpaRepository<CostCenter, String> {

	CostCenter findFirstByNameAndIdNotLike(String name, String id);

	Optional<CostCenter> findByName(String name);

	CostCenter findFirstByNumberAndIdNotLike(String number, String id);

}
