package com.softtek.assetworx_api.serviceImpl;

import static com.softtek.assetworx_api.util.Constants.CONTRACT_TYPE_EXISTS;
import static com.softtek.assetworx_api.util.Constants.CONTRACT_TYPE_NOTFOUND_ID;
import static com.softtek.assetworx_api.util.Constants.CONTRACT_TYPE_NOTSAVED;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.softtek.assetworx_api.entity.ContractType;
import com.softtek.assetworx_api.exception.GenericRestException;
import com.softtek.assetworx_api.exception.InvalidEntityException;
import com.softtek.assetworx_api.repository.ContractTypeRepository;
import com.softtek.assetworx_api.service.ContractTypeService;

@Service
public class ContractTypeServiceImpl implements ContractTypeService {
	
	@Autowired
	ContractTypeRepository contractTypeRepository;
	
	@Autowired
	Validator validator;


	@Override
	public ContractType findById(String id) {
		return contractTypeRepository.findById(id).orElse(null);
	}

	@Override
	public ContractType findByName(String name) {
		return contractTypeRepository.findByName(name).orElse(null);
	}

	public boolean validate(ContractType contractType) {
		List<String> messages = validator.validate(contractType).stream().map(e->
		e.getPropertyPath()+":"+e.getMessage()).collect(Collectors.toList());
		ContractType f = contractTypeRepository.findFirstByNameAndIdNotLike(contractType.getName(),contractType.getId());
		if(f!=null) {
			messages.add(CONTRACT_TYPE_EXISTS + contractType.getName());
		}
		if(!messages.isEmpty()) {
			throw new InvalidEntityException(CONTRACT_TYPE_NOTSAVED, messages);
		}
		return true;

	}

	@Override
	public ContractType save(ContractType contractType) {
		contractType.setId("");
		if(validate(contractType)) {
			return contractTypeRepository.save(contractType);
		}		
		return null; 
	}

	@Override
	public ContractType update(ContractType contractType) {
		ContractType f = findById(contractType.getId());
		if(f==null) {
			throw new GenericRestException(CONTRACT_TYPE_NOTFOUND_ID + contractType.getId(), HttpStatus.NOT_FOUND);
		}
		else {
			f.setName(contractType.getName());
			f.setDescription(contractType.getDescription());
			if(validate(f)) {
				return contractTypeRepository.save(f);
			}
			return null;
		}
	}

	public boolean isDeletable(ContractType contractType) {
		/*if (contractType.getAssets().size()>0) {
			throw new ResourceNotDeletableException("contractType cannot be deleted since assets with this contractType exists.");
		}*/
		return true;
	}

	@Override
	public boolean delete(String id) {
		ContractType contractType = findById(id);
		if(contractType == null) {
			throw new GenericRestException(CONTRACT_TYPE_NOTFOUND_ID + id, HttpStatus.NOT_FOUND);
		}
		if(isDeletable(contractType)) {
			contractType.setActive(false);
			contractType.setName(contractType.getName()+"~"+System.nanoTime());
			contractTypeRepository.save(contractType);	
			return true;
		}
		else{
			return false;
		}
	}

}
