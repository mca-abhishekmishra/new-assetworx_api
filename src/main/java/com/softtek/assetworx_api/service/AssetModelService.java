package com.softtek.assetworx_api.service;

import com.softtek.assetworx_api.entity.AssetModel;

public interface AssetModelService {

	AssetModel findById(String id);

	AssetModel findByName(String name);

	AssetModel save(AssetModel model);

	AssetModel update(AssetModel model);

	boolean delete(String id);

}
