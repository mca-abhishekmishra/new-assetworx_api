package com.softtek.assetworx_api.serviceImpl;

import static com.softtek.assetworx_api.util.Constants.FLOOR_EXISTS;
import static com.softtek.assetworx_api.util.Constants.FLOOR_NOTFOUND_ID;
import static com.softtek.assetworx_api.util.Constants.FLOOR_NOTSAVED;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.softtek.assetworx_api.entity.Floor;
import com.softtek.assetworx_api.exception.GenericRestException;
import com.softtek.assetworx_api.exception.InvalidEntityException;
import com.softtek.assetworx_api.repository.FloorRepository;
import com.softtek.assetworx_api.service.FloorService;

@Service
public class FloorServiceImpl implements FloorService {

	@Autowired
	FloorRepository floorRepository;

	@Autowired
	Validator validator;


	@Override
	public Floor findById(String id) {
		return floorRepository.findById(id).orElse(null);
	}

	@Override
	public Floor findByName(String name) {
		return floorRepository.findByName(name).orElse(null);
	}
	

	@Override
	public List<Floor> getAllByIsActive(boolean b) {
		// TODO Auto-generated method stub
		return floorRepository.findAllByIsActive(true);
	}

	public boolean validate(Floor floor) {
		List<String> messages = validator.validate(floor).stream().map(e->
		e.getPropertyPath()+":"+e.getMessage()).collect(Collectors.toList());
		Floor f = floorRepository.findFirstByNameAndIdNotLike(floor.getName(),floor.getId());
		if(f!=null) {
			messages.add(FLOOR_EXISTS + floor.getName());
		}
		if(!messages.isEmpty()) {
			throw new InvalidEntityException(FLOOR_NOTSAVED, messages);
		}
		return true;

	}

	@Override
	public Floor save(Floor floor) {
		floor.setId("");
		if(validate(floor)) {
			return floorRepository.save(floor);
		}		
		return null; 
	}

	@Override
	public Floor update(Floor floor) {
		Floor f = findById(floor.getId());
		if(f==null) {
			throw new GenericRestException(FLOOR_NOTFOUND_ID + floor.getId(), HttpStatus.NOT_FOUND);
		}
		else {
			f.setName(floor.getName());
			f.setDescription(floor.getDescription());
			if(validate(f)) {
				return floorRepository.save(f);
			}
			return null;
		}
	}

	public boolean isDeletable(Floor floor) {
		/*if (floor.getAssets().size()>0) {
			throw new ResourceNotDeletableException("floor cannot be deleted since assets with this floor exists.");
		}*/
		return true;
	}

	@Override
	public boolean delete(String id) {
		Floor floor = findById(id);
		if(floor == null) {
			throw new GenericRestException(FLOOR_NOTFOUND_ID + id, HttpStatus.NOT_FOUND);
		}
		if(isDeletable(floor)) {
			floor.setActive(false);
			floor.setName(floor.getName()+"~"+System.nanoTime());
			floorRepository.save(floor);	
			return true;
		}
		else{
			return false;
		}
	}


}
