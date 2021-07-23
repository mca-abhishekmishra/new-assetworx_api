package com.softtek.assetworx_api.service;

import java.util.List;

import com.softtek.assetworx_api.entity.Employee;
import com.softtek.assetworx_api.entity.WorkstationAllocation;

public interface WorkstationAllocationService {

	WorkstationAllocation findById(String id);

	WorkstationAllocation update(WorkstationAllocation allocation);

	boolean delete(String id);

	WorkstationAllocation save(WorkstationAllocation allocation);

	public WorkstationAllocation allocate(String employeeId, String workstationId);

	WorkstationAllocation deallocate(String string, String string2);

	List<WorkstationAllocation> findAllByEmployeeAndIsAllocated(Employee employee, boolean b);

}
