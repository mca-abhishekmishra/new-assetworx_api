package com.softtek.assetworx_api.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softtek.assetworx_api.dao.PagingDao;
import com.softtek.assetworx_api.model.PagingRequest;
import com.softtek.assetworx_api.model.PagingResponseModel;
import com.softtek.assetworx_api.service.PagingService;

@Service
public class PagingSerivceImpl implements PagingService {
	
	@Autowired
	PagingDao pagingDao;

	@Override
	public <T> PagingResponseModel<T> findByPagingRequest(PagingRequest pagingRequest, Class<T> clazz) {
		return pagingDao.findByPagingRequest(pagingRequest, clazz);
	}

}
