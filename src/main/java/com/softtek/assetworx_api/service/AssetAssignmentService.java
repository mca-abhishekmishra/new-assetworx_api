package com.softtek.assetworx_api.service;

import java.util.List;

import com.softtek.assetworx_api.entity.AssetAssignment;
import com.softtek.assetworx_api.entity.Employee;

public interface AssetAssignmentService {

	AssetAssignment assign(AssetAssignment a);

	AssetAssignment unassign(AssetAssignment a);

	AssetAssignment reassign(AssetAssignment a);

	AssetAssignment update(AssetAssignment assetAssignment);

	List<AssetAssignment> findAllByEmployeeAndUnassignmentDateIsNotNull(Employee employee);

	List<AssetAssignment> findAllByEmployeeAndUnassignmentDateIsNull(Employee employee);

}
