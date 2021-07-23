package com.softtek.assetworx_api.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.softtek.assetworx_api.entity.Employee;
import com.softtek.assetworx_api.entity.Workstation;
import com.softtek.assetworx_api.entity.WorkstationAllocation;
import com.softtek.assetworx_api.exception.GenericRestException;
import com.softtek.assetworx_api.repository.WorkStationAllocationRepository;
import com.softtek.assetworx_api.service.EmployeeService;
import com.softtek.assetworx_api.service.WorkStationService;
import com.softtek.assetworx_api.service.WorkstationAllocationService;

@Service
public class WorkStationAllocationServiceImpl implements WorkstationAllocationService {

	@Autowired
	WorkStationAllocationRepository allocationRepo;

	@Autowired
	EmployeeService empService;

	@Autowired
	WorkStationService stationService;

	@Override
	public WorkstationAllocation findById(String id) {
		return allocationRepo.findById(id).orElse(null);
	}

	@Override
	public WorkstationAllocation update(WorkstationAllocation area) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WorkstationAllocation save(WorkstationAllocation allocation) {
		return allocationRepo.save(allocation);
	}

	@Override
	public boolean delete(String id) {
		WorkstationAllocation allocation = findById(id);
		allocation.setActive(false);
		return save(allocation) != null ? true : false;
	}

	@Override
	public WorkstationAllocation allocate(String employeeId, String workstationId) {
		Employee employee = empService.findById(employeeId);
		Workstation workstation = stationService.findById(workstationId);
		List<WorkstationAllocation> workstationAllocationList = allocationRepo.findAllByEmployeeAndIsAllocated(employee,
				true);
		for (WorkstationAllocation wa : workstationAllocationList) {
			this.deallocate(wa);
		}
		allocationRepo.saveAll(workstationAllocationList);
		int occupencySize = workstation.getWorkstationAllocations().size();
		if (employee == null || workstation == null) {
			throw new GenericRestException("WorkStation can't be Allocated ", HttpStatus.BAD_REQUEST);
		}
		if (occupencySize < 2) {
			WorkstationAllocation allocation = new WorkstationAllocation();
			allocation.setEmployee(employee);
			allocation.setWorkstation(workstation);
			workstation.getWorkstationAllocations().add(allocation);
			workstation.setOccupied(true);
			if (workstation.getWorkstationAllocations().size() >= 2) {
				workstation.setShared(true);
			}
			stationService.update(workstation);
			return allocationRepo.save(allocation);
		} else {
			throw new GenericRestException("WorkStation already has 2 occupants can't add", HttpStatus.BAD_REQUEST);
		}
	}

	@Override
	public WorkstationAllocation deallocate(String employeeId, String workstationId) {
		Employee employee = empService.findById(employeeId);
		Workstation workstation = stationService.findById(workstationId);
		int occupencySize = workstation.getWorkstationAllocations().size();

		if (employee == null || workstation == null) {
			throw new GenericRestException("WorkStation can't be deallocated ", HttpStatus.BAD_REQUEST);
		}
		if (occupencySize >= 1) {
			// find allocation based on emp and workstation id
			WorkstationAllocation allocation = allocationRepo.findFirstByEmployeeAndWorkstationAndIsAllocated(employee, workstation, true);
			allocation.setAllocated(false);
			workstation.getWorkstationAllocations().remove(allocation);

			if (workstation.getWorkstationAllocations().size() == 1) {
				workstation.setShared(false);
			}
			if (workstation.getWorkstationAllocations().size() == 0) {
				workstation.setOccupied(false);
				workstation.setShared(false);
			}
			stationService.update(workstation);
			return allocationRepo.save(allocation);
		} else {
			throw new GenericRestException("WorkStation can't be deallocated ", HttpStatus.BAD_REQUEST);
		}
	}

	public void deallocate(WorkstationAllocation allocation) {
		Workstation workstation = allocation.getWorkstation();
		int occupencySize = workstation.getWorkstationAllocations().size();

		if (occupencySize >= 1) {
			allocation.setAllocated(false);
			workstation.getWorkstationAllocations().remove(allocation);

			if (workstation.getWorkstationAllocations().size() == 1) {
				workstation.setShared(false);
			}
			if (workstation.getWorkstationAllocations().size() == 0) {
				workstation.setOccupied(false);
				workstation.setShared(false);
			}
			stationService.update(workstation);
			allocationRepo.save(allocation);
		}
	}

	@Override
	public List<WorkstationAllocation> findAllByEmployeeAndIsAllocated(Employee employee, boolean b) {
		return allocationRepo.findAllByEmployeeAndIsAllocated(employee, b);
	}
}
