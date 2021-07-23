package com.softtek.assetworx_api.service;


import java.util.List;

import org.springframework.http.ResponseEntity;

import com.softtek.assetworx_api.entity.Area;
import com.softtek.assetworx_api.entity.Employee;
import com.softtek.assetworx_api.entity.Workstation;
import com.softtek.assetworx_api.entity.WorkstationAllocation;

public interface WorkStationService {

	Workstation findById(String id);
	
	Workstation save(Workstation workstation);

	Workstation update(Workstation workstation);

	boolean delete(String id);
	
	List<Employee> findEmployeeByWorkstationId(String workstationId);

	List<Workstation> findAllByActive(boolean isActive);

	Workstation findWorkStationByArea(Area s);


}
