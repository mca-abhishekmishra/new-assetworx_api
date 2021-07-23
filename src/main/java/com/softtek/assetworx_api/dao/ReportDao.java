package com.softtek.assetworx_api.dao;

import java.util.List;

import com.softtek.assetworx_api.entity.Report;

public interface ReportDao {

	<T> List<Object> getReportData(String queryString, Report report, Class<T> clazz);

}
