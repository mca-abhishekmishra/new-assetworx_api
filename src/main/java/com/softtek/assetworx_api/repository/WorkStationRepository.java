package com.softtek.assetworx_api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.softtek.assetworx_api.entity.Area;
import com.softtek.assetworx_api.entity.Employee;
import com.softtek.assetworx_api.entity.Workstation;

public interface WorkStationRepository extends JpaRepository<Workstation, String> {

	Optional<Workstation> findByArea_Id(String areaId);

	@Query(value = "select emp from Workstation station  join station.workstationAllocations allocation "
			+ " join allocation.employee emp where station.id = :id ")
	List<Employee> findEmployeeByWorkstationId(@Param("id") String workstationId);

	List<Workstation> findAllByIsActive(boolean isActive);

	Workstation findByArea(Area area);

}
