package com.softtek.assetworx_api.serviceImpl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.softtek.assetworx_api.entity.ProjectWbsId;

@Repository
public interface ProjectWbsIdRepository extends JpaRepository<ProjectWbsId, String> {

}
