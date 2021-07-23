package com.softtek.assetworx_api.controller;

import static com.softtek.assetworx_api.util.Constants.PAGINIG_ERROR_MESSAGE;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softtek.assetworx_api.exception.GenericRestException;
import com.softtek.assetworx_api.model.Column;
import com.softtek.assetworx_api.model.Direction;
import com.softtek.assetworx_api.model.Order;
import com.softtek.assetworx_api.model.PagingRequest;
import com.softtek.assetworx_api.model.PagingResponseModel;
import com.softtek.assetworx_api.model.SearchOperation;
import com.softtek.assetworx_api.service.PagingService;

@RestController
public class PagingController {

	@Autowired
	PagingService pagingService;
	
	@Autowired
	ObjectMapper objectMapper;

	@SuppressWarnings("unchecked")
	@PostMapping("/pagingRequest")
	public <T> PagingResponseModel<T> findByPagingRequest(@RequestBody PagingRequest pagingRequest) {
		Class<T> clazz;
		try {
			clazz = (Class<T>) Class.forName("com.softtek.assetworx_api.entity." + pagingRequest.getEntity());
			Object obj = clazz.newInstance();
			Order order = pagingRequest.getOrder().get(0);
			if (order.getColumn().equalsIgnoreCase("id")) {
				order.setColumn("lastUpdated");
				order.setDir(Direction.desc);
			}
			return (PagingResponseModel<T>) pagingService.findByPagingRequest(pagingRequest, obj.getClass());
		} catch (Exception e) {
			e.printStackTrace();
			throw new GenericRestException(PAGINIG_ERROR_MESSAGE, HttpStatus.BAD_REQUEST);
		}
	}

	@SuppressWarnings("unchecked")
	@PostMapping("/pagingRequest/approvedAssets")
	public <T> PagingResponseModel<T> findByApprovedAssetPagingRequest(@RequestBody Map<String, Object> requestBody) {
		Class<T> clazz;
		try {
			PagingRequest pagingRequest = objectMapper.convertValue(requestBody.get("pagingRequest"), PagingRequest.class);
			String assetTypes = (String) requestBody.get("assetTypes");
			System.out.println("assetTypes:"+assetTypes);
			clazz = (Class<T>) Class.forName("com.softtek.assetworx_api.entity." + pagingRequest.getEntity());
			Object obj = clazz.newInstance();
			Column column = Column.createColumn("New,Rework,Approval", SearchOperation.NOT_IN, "assetStatusNames",
					"assetStatus.name");
			pagingRequest.addColumn(column);
			column = Column.createColumn(assetTypes, SearchOperation.IN, "assetType", "assetType.name");
			pagingRequest.addColumn(column);
			Order order = pagingRequest.getOrder().get(0);
			if (order.getColumn().equalsIgnoreCase("id")) {
				order.setColumn("lastUpdated");
				order.setDir(Direction.desc);
			}
			return (PagingResponseModel<T>) pagingService.findByPagingRequest(pagingRequest, obj.getClass());
		} catch (Exception e) {
			e.printStackTrace();
			throw new GenericRestException(PAGINIG_ERROR_MESSAGE, HttpStatus.BAD_REQUEST);
		}
	}

}
