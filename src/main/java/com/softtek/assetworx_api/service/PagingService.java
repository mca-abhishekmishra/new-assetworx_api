package com.softtek.assetworx_api.service;

import com.softtek.assetworx_api.model.PagingRequest;
import com.softtek.assetworx_api.model.PagingResponseModel;

public interface PagingService {

	<T> PagingResponseModel<T> findByPagingRequest(PagingRequest pagingRequest, Class<T> clazz);
	
	

}
