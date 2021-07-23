package com.softtek.assetworx_api.serviceImpl;

import static com.softtek.assetworx_api.util.Constants.*;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.softtek.assetworx_api.entity.Manufacturer;
import com.softtek.assetworx_api.exception.GenericRestException;
import com.softtek.assetworx_api.exception.InvalidEntityException;
import com.softtek.assetworx_api.repository.ManufacturerRepository;
import com.softtek.assetworx_api.service.ManufacturerService;

@Service
public class ManufacturerServiceImpl implements ManufacturerService {

	@Autowired
	ManufacturerRepository manufacturerRepository;

	@Autowired
	Validator validator;

	@Override
	public Manufacturer findById(String id) {
		return manufacturerRepository.findById(id).orElse(null);
	}

	@Override
	public Manufacturer findByName(String name) {
		return manufacturerRepository.findByName(name).orElse(null);
	}

	public boolean validate(Manufacturer manufacturer) {
		List<String> messages = validator.validate(manufacturer).stream()
				.map(e -> e.getPropertyPath() + ":" + e.getMessage()).collect(Collectors.toList());
		Manufacturer f = manufacturerRepository.findFirstByNameAndIdNotLike(manufacturer.getName(),
				manufacturer.getId());
		if (f != null) {
			messages.add(MANUFACTURER_EXISTS + manufacturer.getName());
		}
		if (!messages.isEmpty()) {
			throw new InvalidEntityException(MANUFACTURER_NOTSAVED, messages);
		}
		return true;

	}

	@Override
	public Manufacturer save(Manufacturer manufacturer) {
		manufacturer.setId("");
		if (validate(manufacturer)) {
			return manufacturerRepository.save(manufacturer);
		}
		return null;
	}

	@Override
	public Manufacturer update(Manufacturer manufacturer) {
		Manufacturer f = findById(manufacturer.getId());
		if (f == null) {
			throw new GenericRestException(MANUFACTURER_NOTFOUND_ID + manufacturer.getId(), HttpStatus.NOT_FOUND);
		} else {
			f.setName(manufacturer.getName());
			f.setDescription(manufacturer.getDescription());
			if (validate(f)) {
				return manufacturerRepository.save(f);
			}
			return null;
		}
	}

	public boolean isDeletable(Manufacturer manufacturer) {
		/*
		 * if (manufacturer.getAssets().size()>0) { throw new
		 * ResourceNotDeletableException("manufacturer cannot be deleted since assets with this manufacturer exists."
		 * ); }
		 */
		return true;
	}

	@Override
	public boolean delete(String id) {
		Manufacturer manufacturer = findById(id);
		if (manufacturer == null) {
			throw new GenericRestException(MANUFACTURER_NOTFOUND_ID + id, HttpStatus.NOT_FOUND);
		}
		if (isDeletable(manufacturer)) {
			manufacturer.setActive(false);
			manufacturer.setName(manufacturer.getName() + "~" + System.nanoTime());
			manufacturerRepository.save(manufacturer);
			return true;
		} else {
			return false;
		}
	}

}
