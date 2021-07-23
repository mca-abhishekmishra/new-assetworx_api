package com.softtek.assetworx_api.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.softtek.assetworx_api.entity.Employee;
import com.softtek.assetworx_api.entity.Workstation;
import com.softtek.assetworx_api.entity.WorkstationAllocation;

@Repository
public interface WorkStationAllocationRepository extends CrudRepository<WorkstationAllocation, String> {

	WorkstationAllocation findByEmployeeAndWorkstation(Employee employee, Workstation workstation);

	List<WorkstationAllocation> findAllByEmployeeAndWorkstationAndIsAllocated(Employee employee,
			Workstation workstation, boolean isAllocated);

	List<WorkstationAllocation> findAllByEmployeeAndIsAllocated(Employee employee, boolean isAllocated);

	WorkstationAllocation findFirstByEmployeeAndWorkstationAndIsAllocated(Employee employee, Workstation workstation,
			boolean isAllocated);

}
