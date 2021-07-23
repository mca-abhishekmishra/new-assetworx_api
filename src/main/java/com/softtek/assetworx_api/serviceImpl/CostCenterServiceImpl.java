package com.softtek.assetworx_api.serviceImpl;

import static com.softtek.assetworx_api.util.Constants.COSTCENTER_EXISTS;
import static com.softtek.assetworx_api.util.Constants.COSTCENTER_NOTFOUND_ID;
import static com.softtek.assetworx_api.util.Constants.COSTCENTER_NOTSAVED;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.softtek.assetworx_api.entity.CostCenter;
import com.softtek.assetworx_api.exception.GenericRestException;
import com.softtek.assetworx_api.exception.InvalidEntityException;
import com.softtek.assetworx_api.repository.CostCenterRepository;
import com.softtek.assetworx_api.service.CostCenterService;

@Service
public class CostCenterServiceImpl implements CostCenterService {

	@Autowired
	CostCenterRepository costCenterRepository;

	@Autowired
	Validator validator;


	@Override
	public CostCenter findById(String id) {
		return costCenterRepository.findById(id).orElse(null);
	}

	@Override
	public CostCenter findByName(String name) {
		return costCenterRepository.findByName(name).orElse(null);
	}

	public boolean validate(CostCenter costCenter) {
		List<String> messages = validator.validate(costCenter).stream().map(e->
		e.getPropertyPath()+":"+e.getMessage()).collect(Collectors.toList());
		CostCenter f = costCenterRepository.findFirstByNameAndIdNotLike(costCenter.getName(),costCenter.getId());
		if(f!=null) {
			messages.add(COSTCENTER_EXISTS + costCenter.getName());
		}
		f = costCenterRepository.findFirstByNumberAndIdNotLike(costCenter.getNumber(),costCenter.getId());
		if(f!=null) {
			messages.add("number:Cost Center already exists for the given number: " + costCenter.getNumber());
		}
		if(!messages.isEmpty()) {
			throw new InvalidEntityException(COSTCENTER_NOTSAVED, messages);
		}
		return true;

	}

	@Override
	public CostCenter save(CostCenter costCenter) {
		costCenter.setId("");
		if(validate(costCenter)) {
			return costCenterRepository.save(costCenter);
		}		
		return null; 
	}

	@Override
	public CostCenter update(CostCenter costCenter) {
		CostCenter f = findById(costCenter.getId());
		if(f==null) {
			throw new GenericRestException(COSTCENTER_NOTFOUND_ID + costCenter.getId(), HttpStatus.NOT_FOUND);
		}
		else {
			f.setName(costCenter.getName());
			f.setNumber(costCenter.getNumber());
			f.setDescription(costCenter.getDescription());
			if(validate(f)) {
				return costCenterRepository.save(f);
			}
			return null;
		}
	}

	public boolean isDeletable(CostCenter costCenter) {
		/*if (costCenter.getAssets().size()>0) {
			throw new ResourceNotDeletableException("costCenter cannot be deleted since assets with this costCenter exists.");
		}*/
		return true;
	}

	@Override
	public boolean delete(String id) {
		CostCenter costCenter = findById(id);
		if(costCenter == null) {
			throw new GenericRestException(COSTCENTER_NOTFOUND_ID + id, HttpStatus.NOT_FOUND);
		}
		if(isDeletable(costCenter)) {
			costCenter.setActive(false);
			costCenter.setName(costCenter.getName()+"~"+System.nanoTime());
			costCenterRepository.save(costCenter);	
			return true;
		}
		else{
			return false;
		}
	}

}
