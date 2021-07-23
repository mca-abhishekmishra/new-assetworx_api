package com.softtek.assetworx_api.service;

import com.softtek.assetworx_api.entity.CostCenter;

public interface CostCenterService {

	CostCenter findById(String id);

	CostCenter save(CostCenter floor);

	CostCenter update(CostCenter floor);

	boolean delete(String id);

	CostCenter findByName(String name);

}
