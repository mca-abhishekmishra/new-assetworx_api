package com.softtek.assetworx_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.softtek.assetworx_api.entity.Category;
import com.softtek.assetworx_api.entity.SubCategory;

public interface SubCategoryRepository extends JpaRepository<SubCategory, String> {

	Optional<SubCategory> findByName(String name);

	SubCategory findFirstByNameAndIdNotLike(String name, String id);

	SubCategory findFirstByNameAndIdNotLikeAndCategoryNotLike(String name, String id, Category category);

	SubCategory findFirstByNameAndIdNotLikeAndCategory(String name, String id, Category category);

	SubCategory findByCategoryAndName(Category category, String name);

}
