package com.softtek.assetworx_api.serviceImpl;

import static com.softtek.assetworx_api.util.Constants.ASSET_MODEL_EXISTS;
import static com.softtek.assetworx_api.util.Constants.ASSET_MODEL_NOTFOUND_ID;
import static com.softtek.assetworx_api.util.Constants.ASSET_MODEL_NOTSAVED;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.softtek.assetworx_api.entity.AssetModel;
import com.softtek.assetworx_api.exception.GenericRestException;
import com.softtek.assetworx_api.exception.InvalidEntityException;
import com.softtek.assetworx_api.repository.AssetModelRepository;
import com.softtek.assetworx_api.service.AssetModelService;

@Service
public class AssetModelServiceImpl implements AssetModelService {

	@Autowired
	AssetModelRepository assetModelRepository;

	@Autowired
	Validator validator;

	@Override
	public AssetModel findById(String id) {
		return assetModelRepository.findById(id).orElse(null);
	}

	@Override
	public AssetModel findByName(String name) {
		return assetModelRepository.findByName(name).orElse(null);
	}

	public boolean validate(AssetModel assetModel) {
		List<String> messages = validator.validate(assetModel).stream()
				.map(e -> e.getPropertyPath() + ":" + e.getMessage()).collect(Collectors.toList());
		AssetModel f = assetModelRepository.findFirstByNameAndIdNotLike(assetModel.getName(), assetModel.getId());
		if (f != null) {
			messages.add(ASSET_MODEL_EXISTS + assetModel.getName());
		}
		if (!messages.isEmpty()) {
			throw new InvalidEntityException(ASSET_MODEL_NOTSAVED, messages);
		}
		return true;

	}

	@Override
	public AssetModel save(AssetModel assetModel) {
		assetModel.setId("");
		if (validate(assetModel)) {
			return assetModelRepository.save(assetModel);
		}
		return null;
	}

	@Override
	public AssetModel update(AssetModel assetModel) {
		AssetModel f = findById(assetModel.getId());
		if (f == null) {
			throw new GenericRestException(ASSET_MODEL_NOTFOUND_ID + assetModel.getId(), HttpStatus.NOT_FOUND);
		} else {
			f.setName(assetModel.getName());
			f.setDescription(assetModel.getDescription());
			if (validate(f)) {
				return assetModelRepository.save(f);
			}
			return null;
		}
	}

	public boolean isDeletable(AssetModel assetModel) {
		/*
		 * if (assetModel.getAssets().size()>0) { throw new
		 * ResourceNotDeletableException("assetModel cannot be deleted since assets with this assetModel exists."
		 * ); }
		 */
		return true;
	}

	@Override
	public boolean delete(String id) {
		AssetModel assetModel = findById(id);
		if (assetModel == null) {
			throw new GenericRestException(ASSET_MODEL_NOTFOUND_ID + id, HttpStatus.NOT_FOUND);
		}
		if (isDeletable(assetModel)) {
			assetModel.setActive(false);
			assetModel.setName(assetModel.getName() + "~" + System.nanoTime());
			assetModelRepository.save(assetModel);
			return true;
		} else {
			return false;
		}
	}

}
