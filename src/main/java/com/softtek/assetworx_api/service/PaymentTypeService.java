package com.softtek.assetworx_api.service;

import com.softtek.assetworx_api.entity.PaymentType;

public interface PaymentTypeService {
	
	PaymentType findById(String id);
	
	PaymentType findByName(String name);
	
	PaymentType save(PaymentType pt);

	PaymentType update(PaymentType pt);

	boolean delete(String id);

}
