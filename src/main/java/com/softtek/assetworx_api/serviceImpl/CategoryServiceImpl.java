package com.softtek.assetworx_api.serviceImpl;

import static com.softtek.assetworx_api.util.Constants.CATEGORY_EXISTS;
import static com.softtek.assetworx_api.util.Constants.CATEGORY_NOTFOUND_ID;
import static com.softtek.assetworx_api.util.Constants.CATEGORY_NOTSAVED;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.softtek.assetworx_api.entity.AssetType;
import com.softtek.assetworx_api.entity.Category;
import com.softtek.assetworx_api.exception.GenericRestException;
import com.softtek.assetworx_api.exception.InvalidEntityException;
import com.softtek.assetworx_api.repository.CategoryRepository;
import com.softtek.assetworx_api.service.AssetTypeService;
import com.softtek.assetworx_api.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	CategoryRepository categoryRepository;

	
	@Autowired
	AssetTypeService assetTypeService;
	
	@Autowired
	Validator validator;


	@Override
	public Category findById(String id) {
		return categoryRepository.findById(id).orElse(null);
	}

	@Override
	public Category findByName(String name) {
		return categoryRepository.findByName(name).orElse(null);
	}

	public boolean validate(Category category) {
		List<String> messages = validator.validate(category).stream().map(e->
		e.getPropertyPath()+":"+e.getMessage()).collect(Collectors.toList());
		Category c = categoryRepository.findFirstByNameAndIdNotLikeAndAssetType(category.getName(),category.getId(),category.getAssetType());
		if(c!=null) {
			messages.add(CATEGORY_EXISTS + category.getName());
		}
		if(!messages.isEmpty()) {
			throw new InvalidEntityException(CATEGORY_NOTSAVED, messages);
		}
		return true;

	}

	@Override
	public Category save(Category category) {
		category.setId("");
		category.setAssetType(assetTypeService.findById(category.getAssetType() !=null ? category.getAssetType().getId() : ""));
		if(validate(category)) {
			return categoryRepository.save(category);
		}		
		return null; 
	}

	@Override
	public Category update(Category category) {
		Category c = findById(category.getId());
		if(c==null) {
			throw new GenericRestException(CATEGORY_NOTFOUND_ID + category.getId(), HttpStatus.NOT_FOUND);
		}
		else {
			c.setName(category.getName());
			c.setMail(category.getMail());
			c.setAssetType(assetTypeService.findById(category.getAssetType() !=null ? category.getAssetType().getId() : ""));
			c.setDescription(category.getDescription());
			if(validate(c)) {
				return categoryRepository.save(c);
			}
			return null;
		}
	}

	public boolean isDeletable(Category category) {
		/*if (category.getAssets().size()>0) {
			throw new ResourceNotDeletableException("Category cannot be deleted since assets with this category exists.");
		}*/
		return true;
	}

	@Override
	public boolean delete(String id) {
		Category category = findById(id);
		if(category == null) {
			throw new GenericRestException(CATEGORY_NOTFOUND_ID + id, HttpStatus.NOT_FOUND);
		}
		if(isDeletable(category)) {
			category.setActive(false);
			category.setName(category.getName()+"~"+System.nanoTime());
			categoryRepository.save(category);	
			return true;
		}
		else{
			return false;
		}
	}

	@Override
	public Category findByAssetTypeAndName(AssetType assetType, String name) {
		
		return categoryRepository.findByAssetTypeAndName(assetType, name);
	}

}
