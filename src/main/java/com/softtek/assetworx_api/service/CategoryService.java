package com.softtek.assetworx_api.service;

import com.softtek.assetworx_api.entity.AssetType;
import com.softtek.assetworx_api.entity.Category;

public interface CategoryService {

	Category findById(String id);

	Category findByName(String name);

	Category save(Category category);

	Category update(Category category);

	boolean delete(String id);

	Category findByAssetTypeAndName(AssetType assetType, String name);

}
