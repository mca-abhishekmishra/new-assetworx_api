package com.softtek.assetworx_api.serviceImpl;

import static com.softtek.assetworx_api.util.Constants.*;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.softtek.assetworx_api.entity.OperatingSystem;
import com.softtek.assetworx_api.exception.GenericRestException;
import com.softtek.assetworx_api.exception.InvalidEntityException;
import com.softtek.assetworx_api.repository.OperatingSystemRepository;
import com.softtek.assetworx_api.service.OperatingSystemService;

@Service
public class OperatingSystemServiceImpl implements OperatingSystemService {

	@Autowired
	OperatingSystemRepository operatingSystemRepository;

	@Autowired
	Validator validator;

	@Override
	public OperatingSystem findById(String id) {
		return operatingSystemRepository.findById(id).orElse(null);
	}

	@Override
	public OperatingSystem findByName(String name) {
		return operatingSystemRepository.findByName(name).orElse(null);
	}

	public boolean validate(OperatingSystem operatingSystem) {
		List<String> messages = validator.validate(operatingSystem).stream()
				.map(e -> e.getPropertyPath() + ":" + e.getMessage()).collect(Collectors.toList());
		OperatingSystem f = operatingSystemRepository.findFirstByNameAndIdNotLike(operatingSystem.getName(),
				operatingSystem.getId());
		if (f != null) {
			messages.add(OS_EXISTS + operatingSystem.getName());
		}
		if (!messages.isEmpty()) {
			throw new InvalidEntityException(OS_NOTSAVED, messages);
		}
		return true;

	}

	@Override
	public OperatingSystem save(OperatingSystem operatingSystem) {
		operatingSystem.setId("");
		if (validate(operatingSystem)) {
			return operatingSystemRepository.save(operatingSystem);
		}
		return null;
	}

	@Override
	public OperatingSystem update(OperatingSystem operatingSystem) {
		OperatingSystem f = findById(operatingSystem.getId());
		if (f == null) {
			throw new GenericRestException(OS_NOTFOUND_ID + operatingSystem.getId(), HttpStatus.NOT_FOUND);
		} else {
			f.setName(operatingSystem.getName());
			f.setDescription(operatingSystem.getDescription());
			if (validate(f)) {
				return operatingSystemRepository.save(f);
			}
			return null;
		}
	}

	public boolean isDeletable(OperatingSystem operatingSystem) {
		/*
		 * if (operatingSystem.getAssets().size()>0) { throw new
		 * ResourceNotDeletableException("operatingSystem cannot be deleted since assets with this operatingSystem exists."
		 * ); }
		 */
		return true;
	}

	@Override
	public boolean delete(String id) {
		OperatingSystem operatingSystem = findById(id);
		if (operatingSystem == null) {
			throw new GenericRestException(OS_NOTFOUND_ID + id, HttpStatus.NOT_FOUND);
		}
		if (isDeletable(operatingSystem)) {
			operatingSystem.setActive(false);
			operatingSystem.setName(operatingSystem.getName() + "~" + System.nanoTime());
			operatingSystemRepository.save(operatingSystem);
			return true;
		} else {
			return false;
		}
	}

}
