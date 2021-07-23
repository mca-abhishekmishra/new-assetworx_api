package com.softtek.assetworx_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.softtek.assetworx_api.entity.AssetType;
import com.softtek.assetworx_api.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {

	Optional<Category> findByName(String name);

	Category findFirstByNameAndIdNotLike(String name, String id);

	Category findFirstByNameAndIdNotLikeAndAssetType(String name, String id, AssetType assetType);

	Category findByAssetTypeAndName(AssetType assetType, String name);

}
