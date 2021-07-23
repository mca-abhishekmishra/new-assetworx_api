package com.softtek.assetworx_api.daoImpl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.softtek.assetworx_api.dao.ReportDao;
import com.softtek.assetworx_api.entity.Report;
import com.softtek.assetworx_api.util.DBUtil;

@Repository
public class ReportDaoImpl implements ReportDao {

	@Autowired 
	EntityManager em;
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<Object> getReportData(String queryString, Report report, Class<T> clazz) {
		
		Query q1 = DBUtil.parseQueryString(queryString, report.getQuery(), clazz, em);
		List<Object> reportData = q1.getResultList();
		return reportData;
	}

}
