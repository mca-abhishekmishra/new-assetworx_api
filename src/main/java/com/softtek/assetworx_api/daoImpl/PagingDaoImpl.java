package com.softtek.assetworx_api.daoImpl;

import static com.softtek.assetworx_api.util.Constants.FILTERED_COUNT;
import static com.softtek.assetworx_api.util.Constants.LIST;
import static com.softtek.assetworx_api.util.Constants.TOTAL_COUNT;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.softtek.assetworx_api.dao.PagingDao;
import com.softtek.assetworx_api.model.PagingRequest;
import com.softtek.assetworx_api.model.PagingResponseModel;
import com.softtek.assetworx_api.util.DBUtil;

@Repository
public class PagingDaoImpl implements PagingDao {
	
	@Autowired
	EntityManager em;
	
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> PagingResponseModel<T> findByPagingRequest(PagingRequest pagingRequest,Class<T> clazz) {
		PagingResponseModel<T> responseModel = new PagingResponseModel<T>();
		responseModel.setData(DBUtil.createQuery(pagingRequest, clazz,em, LIST).getResultList());
		responseModel.setRecordsTotal(((Number)DBUtil.createQuery(pagingRequest, clazz,em ,TOTAL_COUNT).getSingleResult()).intValue());
		responseModel.setRecordsFiltered(((Number)DBUtil.createQuery(pagingRequest, clazz,em ,FILTERED_COUNT).getSingleResult()).intValue());
		responseModel.setDraw(pagingRequest.getDraw());
		return responseModel;
	}

}
