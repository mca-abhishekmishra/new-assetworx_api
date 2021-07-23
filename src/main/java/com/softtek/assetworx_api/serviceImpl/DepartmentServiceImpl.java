package com.softtek.assetworx_api.serviceImpl;

import static com.softtek.assetworx_api.util.Constants.DEPARTMENT_EXISTS;
import static com.softtek.assetworx_api.util.Constants.DEPARTMENT_NOTFOUND_ID;
import static com.softtek.assetworx_api.util.Constants.DEPARTMENT_NOTSAVED;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.softtek.assetworx_api.entity.Department;
import com.softtek.assetworx_api.exception.GenericRestException;
import com.softtek.assetworx_api.exception.InvalidEntityException;
import com.softtek.assetworx_api.repository.DepartmentRepository;
import com.softtek.assetworx_api.service.DepartmentService;
import com.softtek.assetworx_api.service.DivisionService;

@Service
public class DepartmentServiceImpl implements DepartmentService {

	@Autowired
	DepartmentRepository departmentRepository;

	@Autowired
	Validator validator;
	
	@Autowired
	DivisionService divisionService;


	@Override
	public Department findById(String id) {
		return departmentRepository.findById(id).orElse(null);
	}

	@Override
	public Department findByName(String name) {
		return departmentRepository.findByName(name).orElse(null);
	}

	public boolean validate(Department department) {
		List<String> messages = validator.validate(department).stream().map(e->
		e.getPropertyPath()+":"+e.getMessage()).collect(Collectors.toList());
		Department f = departmentRepository.findFirstByNameAndIdNotLike(department.getName(),department.getId());
		if(f!=null) {
			messages.add(DEPARTMENT_EXISTS + department.getName());
		}
		if(!messages.isEmpty()) {
			throw new InvalidEntityException(DEPARTMENT_NOTSAVED, messages);
		}
		return true;

	}

	@Override
	public Department save(Department department) {
		department.setId("");
		department.setDivision(divisionService.findById(department.getDivision().getId()));
		if(validate(department)) {
			return departmentRepository.save(department);
		}		
		return null; 
	}

	@Override
	public Department update(Department department) {
		Department f = findById(department.getId());
		if(f==null) {
			throw new GenericRestException(DEPARTMENT_NOTFOUND_ID + department.getId(), HttpStatus.NOT_FOUND);
		}
		else {
			f.setName(department.getName());
			f.setDescription(department.getDescription());
			f.setDivision(divisionService.findById(department.getDivision().getId()));
			if(validate(f)) {
				return departmentRepository.save(f);
			}
			return null;
		}
	}

	public boolean isDeletable(Department department) {
		/*if (department.getAssets().size()>0) {
			throw new ResourceNotDeletableException("department cannot be deleted since assets with this department exists.");
		}*/
		return true;
	}

	@Override
	public boolean delete(String id) {
		Department department = findById(id);
		if(department == null) {
			throw new GenericRestException(DEPARTMENT_NOTFOUND_ID + id, HttpStatus.NOT_FOUND);
		}
		if(isDeletable(department)) {
			department.setActive(false);
			department.setName(department.getName()+"~"+System.nanoTime());
			departmentRepository.save(department);	
			return true;
		}
		else{
			return false;
		}
	}

}
