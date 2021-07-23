package com.softtek.assetworx_api.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softtek.assetworx_api.entity.Area;
import com.softtek.assetworx_api.entity.Employee;
import com.softtek.assetworx_api.entity.Workstation;
import com.softtek.assetworx_api.repository.WorkStationRepository;
import com.softtek.assetworx_api.service.WorkStationService;

@Service
public class WorkStationServiceImpl implements WorkStationService {

	@Autowired
	WorkStationRepository workStationRepo;

	@Override
	public Workstation findById(String id) {
		return workStationRepo.findById(id).orElseGet(null);
	}

	@Override
	public Workstation findWorkStationByArea(Area area) {
		return workStationRepo.findByArea(area);
	}

	@Override
	public Workstation save(Workstation workstation) {
		workstation.setId("");
		return workStationRepo.save(workstation);
	}

	@Override
	public Workstation update(Workstation workstation) {
		return workStationRepo.save(workstation);
	}

	@Override
	public boolean delete(String id) {
		Workstation workstation = findById(id);
		if (workstation == null) {
			return false;
		}
		workstation.setActive(false);
		update(workstation);
		return true;
	}

	@Override
	public List<Employee> findEmployeeByWorkstationId(String workstationId) {
		return workStationRepo.findEmployeeByWorkstationId(workstationId);
	}

	@Override
	public List<Workstation> findAllByActive(boolean isActive) {
		return workStationRepo.findAllByIsActive(isActive);
	}

}
