package com.softtek.assetworx_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.softtek.assetworx_api.entity.Asset;
import com.softtek.assetworx_api.entity.AssetAssignment;
import com.softtek.assetworx_api.entity.Employee;


public interface AssetAssignmentRepository extends JpaRepository<AssetAssignment, String> {

	AssetAssignment findByIdAndIsActive(String assetAssignmentId, boolean isActive);

	List<Asset> findAllByAssetIn(List<Asset> assets);

	List<AssetAssignment> findAllByEmployeeAndUnassignmentDateIsNotNull(Employee employee);

	List<AssetAssignment> findAllByEmployeeAndUnassignmentDateIsNull(Employee employee);

}
