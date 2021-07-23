package com.softtek.assetworx_api.serviceImpl;

import static com.softtek.assetworx_api.util.Constants.SUBCATEGORY_EXISTS;
import static com.softtek.assetworx_api.util.Constants.SUBCATEGORY_NOTFOUND_ID;
import static com.softtek.assetworx_api.util.Constants.SUBCATEGORY_NOTSAVED;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.softtek.assetworx_api.entity.Category;
import com.softtek.assetworx_api.entity.SubCategory;
import com.softtek.assetworx_api.exception.GenericRestException;
import com.softtek.assetworx_api.exception.InvalidEntityException;
import com.softtek.assetworx_api.repository.SubCategoryRepository;
import com.softtek.assetworx_api.service.CategoryService;
import com.softtek.assetworx_api.service.SubCategoryService;

@Service
public class SubCategoryServiceImpl implements SubCategoryService {

	@Autowired
	SubCategoryRepository subCategoryRepository;

	@Autowired
	Validator validator;
	
	@Autowired
	CategoryService categoryService;


	@Override
	public SubCategory findById(String id) {
		return subCategoryRepository.findById(id).orElse(null);
	}

	@Override
	public SubCategory findByName(String name) {
		return subCategoryRepository.findByName(name).orElse(null);
	}

	public boolean validate(SubCategory subCategory) {
		List<String> messages = validator.validate(subCategory).stream().map(e->
		e.getPropertyPath()+":"+e.getMessage()).collect(Collectors.toList());
		SubCategory s = subCategoryRepository.findFirstByNameAndIdNotLikeAndCategory(subCategory.getName(),subCategory.getId(),subCategory.getCategory());
		if(s!=null) {
			messages.add(SUBCATEGORY_EXISTS + subCategory.getName());
		}
		if(!messages.isEmpty()) {
			throw new InvalidEntityException(SUBCATEGORY_NOTSAVED, messages);
		}
		return true;

	}

	@Override
	public SubCategory save(SubCategory subCategory) {
		subCategory.setId("");
		subCategory.setCategory(categoryService.findById(subCategory.getCategory() !=null ? subCategory.getCategory().getId() : ""));
		if(validate(subCategory)) {
			return subCategoryRepository.save(subCategory);
		}		
		return null; 
	}

	@Override
	public SubCategory update(SubCategory subCategory) {
		SubCategory s = findById(subCategory.getId());
		if(s==null) {
			throw new GenericRestException(SUBCATEGORY_NOTFOUND_ID + subCategory.getId(), HttpStatus.NOT_FOUND);
		}
		else {
			s.setName(subCategory.getName());
			s.setDescription(subCategory.getDescription());
			s.setCategory(categoryService.findById(subCategory.getCategory() !=null ? subCategory.getCategory().getId() : ""));
			s.setTagId(subCategory.getTagId()); 
			if(validate(s)) {
				return subCategoryRepository.save(s);
			}
			return null;
		}
	}

	public boolean isDeletable(SubCategory subCategory) {
		/*if (subCategory.getAssets().size()>0) {
			throw new ResourceNotDeletableException("Category cannot be deleted since assets with this subCategory exists.");
		}*/
		return true;
	}

	@Override
	public boolean delete(String id) {
		SubCategory subCategory = findById(id);
		if(subCategory == null) {
			throw new GenericRestException(SUBCATEGORY_NOTFOUND_ID + id, HttpStatus.NOT_FOUND);
		}
		if(isDeletable(subCategory)) {
			subCategory.setActive(false);
			subCategory.setName(subCategory.getName()+"~"+System.nanoTime());
			subCategoryRepository.save(subCategory);	
			return true;
		}
		else{
			return false;
		}
	}

	@Override
	public SubCategory findByCategoryAndName(Category category, String name) {
		return subCategoryRepository.findByCategoryAndName(category, name);
	}

}
