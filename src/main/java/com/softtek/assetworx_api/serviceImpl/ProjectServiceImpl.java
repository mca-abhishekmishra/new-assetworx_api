package com.softtek.assetworx_api.serviceImpl;

import static com.softtek.assetworx_api.util.Constants.PROJECT_EXISTS;
import static com.softtek.assetworx_api.util.Constants.PROJECT_EXISTS_ID;
import static com.softtek.assetworx_api.util.Constants.PROJECT_NOTFOUND_ID;
import static com.softtek.assetworx_api.util.Constants.PROJECT_NOTSAVED;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.softtek.assetworx_api.entity.Project;
import com.softtek.assetworx_api.exception.GenericRestException;
import com.softtek.assetworx_api.exception.InvalidEntityException;
import com.softtek.assetworx_api.repository.ProjectRepository;
import com.softtek.assetworx_api.service.ProjectService;

@Service
public class ProjectServiceImpl implements ProjectService {

	@Autowired
	ProjectRepository projectRepository;

	@Autowired
	Validator validator;


	@Override
	public Project findById(String id) {
		return projectRepository.findById(id).orElse(null);
	}

	@Override
	public Project findByName(String name) {
		return projectRepository.findByName(name).orElse(null);
	}

	public boolean validate(Project project) {
		List<String> messages = validator.validate(project).stream().map(e->
		e.getPropertyPath()+":"+e.getMessage()).collect(Collectors.toList());
		/*
		 * Project f =
		 * projectRepository.findFirstByNameAndIdNotLike(project.getName(),project.getId
		 * ()); if(f!=null) messages.add(PROJECT_EXISTS + project.getName());
		 */
		Project f = projectRepository.findFirstByProjectIdAndIdNotLike(project.getProjectId(),project.getId());
		if(f!=null) messages.add(PROJECT_EXISTS_ID + project.getProjectId());
		if(!messages.isEmpty()) {
			throw new InvalidEntityException(PROJECT_NOTSAVED, messages);
		}
		return true;

	}

	@Override
	public Project save(Project project) {
		project.setId("");
		if(validate(project)) {
			return projectRepository.save(project);
		}		
		return null; 
	}

	@Override
	public Project update(Project project) {
		Project f = findById(project.getId());
		if(f==null) {
			throw new GenericRestException(PROJECT_NOTFOUND_ID + project.getId(), HttpStatus.NOT_FOUND);
		}
		else {
			f.setName(project.getName());
			f.setProjectId(project.getProjectId());
			f.setDescription(project.getDescription());
			if(validate(f)) {
				return projectRepository.save(f);
			}
			return null;
		}
	}

	public boolean isDeletable(Project project) {
		/*if (project.getAssets().size()>0) {
			throw new ResourceNotDeletableException("project cannot be deleted since assets with this project exists.");
		}*/
		return true;
	}

	@Override
	public boolean delete(String id) {
		Project project = findById(id);
		if(project == null) {
			throw new GenericRestException(PROJECT_NOTFOUND_ID + id, HttpStatus.NOT_FOUND);
		}
		if(isDeletable(project)) {
			project.setActive(false);
			project.setName(project.getName()+"~"+System.nanoTime());
			project.setProjectId(project.getProjectId()+"~"+System.nanoTime());
			projectRepository.save(project);	
			return true;
		}
		else{
			return false;
		}
	}

}
