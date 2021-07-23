package com.softtek.assetworx_api.dao;

import com.softtek.assetworx_api.model.PagingRequest;
import com.softtek.assetworx_api.model.PagingResponseModel;

public interface PagingDao {

	<T> PagingResponseModel<T> findByPagingRequest(PagingRequest pagingRequest,Class<T> clazz);
	

}
