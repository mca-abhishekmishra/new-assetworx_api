package com.softtek.assetworx_api.service;

import com.softtek.assetworx_api.entity.Vendor;

public interface VendorService {

	Vendor findById(String id);

	Vendor save(Vendor vendor);

	Vendor update(Vendor vendor);

	boolean delete(String id);

	Vendor findByVendorId(String vendorId);

}
