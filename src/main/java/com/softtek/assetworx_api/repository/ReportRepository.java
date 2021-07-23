package com.softtek.assetworx_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.softtek.assetworx_api.entity.Report;

public interface ReportRepository extends JpaRepository<Report, String> {

}
