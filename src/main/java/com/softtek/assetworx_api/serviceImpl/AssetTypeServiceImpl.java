package com.softtek.assetworx_api.serviceImpl;

import static com.softtek.assetworx_api.util.Constants.ASSET_TYPE_EXISTS;
import static com.softtek.assetworx_api.util.Constants.ASSET_TYPE_NOTFOUND_ID;
import static com.softtek.assetworx_api.util.Constants.ASSET_TYPE_NOTSAVED;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.softtek.assetworx_api.entity.AssetType;
import com.softtek.assetworx_api.exception.GenericRestException;
import com.softtek.assetworx_api.exception.InvalidEntityException;
import com.softtek.assetworx_api.repository.AssetTypeRepository;
import com.softtek.assetworx_api.service.AssetTypeService;

@Service
public class AssetTypeServiceImpl implements AssetTypeService {
	
	@Autowired
	AssetTypeRepository assetTypeRepository;
	
	@Autowired
	Validator validator;


	@Override
	public AssetType findById(String id) {
		return assetTypeRepository.findById(id).orElse(null);
	}

	@Override
	public AssetType findByName(String name) {
		return assetTypeRepository.findByName(name).orElse(null);
	}

	public boolean validate(AssetType assetType) {
		List<String> messages = validator.validate(assetType).stream().map(e->
		e.getPropertyPath()+":"+e.getMessage()).collect(Collectors.toList());
		AssetType f = assetTypeRepository.findFirstByNameAndIdNotLike(assetType.getName(),assetType.getId());
		if(f!=null) {
			messages.add(ASSET_TYPE_EXISTS + assetType.getName());
		}
		if(!messages.isEmpty()) {
			throw new InvalidEntityException(ASSET_TYPE_NOTSAVED, messages);
		}
		return true;

	}

	@Override
	public AssetType save(AssetType assetType) {
		assetType.setId("");
		if(validate(assetType)) {
			return assetTypeRepository.save(assetType);
		}		
		return null; 
	}

	@Override
	public AssetType update(AssetType assetType) {
		AssetType f = findById(assetType.getId());
		if(f==null) {
			throw new GenericRestException(ASSET_TYPE_NOTFOUND_ID + assetType.getId(), HttpStatus.NOT_FOUND);
		}
		else {
			f.setName(assetType.getName());
			f.setDescription(assetType.getDescription());
			if(validate(f)) {
				return assetTypeRepository.save(f);
			}
			return null;
		}
	}

	public boolean isDeletable(AssetType assetType) {
		/*if (assetType.getAssets().size()>0) {
			throw new ResourceNotDeletableException("assetType cannot be deleted since assets with this assetType exists.");
		}*/
		return true;
	}

	@Override
	public boolean delete(String id) {
		AssetType assetType = findById(id);
		if(assetType == null) {
			throw new GenericRestException(ASSET_TYPE_NOTFOUND_ID + id, HttpStatus.NOT_FOUND);
		}
		if(isDeletable(assetType)) {
			assetType.setActive(false);
			assetType.setName(assetType.getName()+"~"+System.nanoTime());
			assetTypeRepository.save(assetType);	
			return true;
		}
		else{
			return false;
		}
	}

}
