package com.softtek.assetworx_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.softtek.assetworx_api.entity.Process;
import com.softtek.assetworx_api.entity.Status;

@Repository
public interface ProcessRepository extends JpaRepository<Process, String> {


	Optional<Process> findByRelativeIdAndIsActive(String relativeId, boolean isActive);

	Optional<Process> findFirstByRelativeIdAndProcessStatus(String relativeId, Status processStatus);

}
