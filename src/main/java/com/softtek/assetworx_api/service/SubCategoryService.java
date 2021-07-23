package com.softtek.assetworx_api.service;


import com.softtek.assetworx_api.entity.Category;
import com.softtek.assetworx_api.entity.SubCategory;

public interface SubCategoryService {

	SubCategory findById(String id);

	SubCategory findByName(String name);

	SubCategory save(SubCategory subCategory);

	SubCategory update(SubCategory subCategory);

	boolean delete(String id);

	SubCategory findByCategoryAndName(Category category, String name);

}
