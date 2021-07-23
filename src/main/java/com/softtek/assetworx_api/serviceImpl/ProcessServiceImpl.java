package com.softtek.assetworx_api.serviceImpl;

import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softtek.assetworx_api.entity.Process;
import com.softtek.assetworx_api.entity.Status;
import com.softtek.assetworx_api.repository.ProcessRepository;
import com.softtek.assetworx_api.service.ProcessService;
import com.softtek.assetworx_api.service.StatusService;

@Service
public class ProcessServiceImpl implements ProcessService {

	@Autowired
	ProcessRepository processRepository;

	@Autowired
	Validator validator;

	@Autowired
	StatusService statusService;


	@Override
	public Process findById(String id) {
		return processRepository.findById(id).orElse(null);
	}

	@Override
	public Process findByRelativeId(String relativeId, String status) {
		Status processStatus = statusService.findByName(status);
		return processRepository.findFirstByRelativeIdAndProcessStatus(relativeId, processStatus).orElse(null);
	}


	@Override
	public Process save(Process process) {
		return processRepository.save(process);
	}

	@Override
	public Process save(Process process, String status) {
		Process pr = findById(process.getId());
		pr.setProcessStatus(statusService.findByName(status));
		return processRepository.save(pr);
	}

	@Override
	public Process save(Process process, double percentage, String description, String status) {
		System.out.println("Saving "+process.getProcessRefId()+" with percentage:"+percentage);
		Process pr = findById(process.getId());
		pr.setProcessPercentage(percentage);
		pr.setDescription(description);
		pr.setProcessStatus(statusService.findByName(status));
		return processRepository.save(pr);
	}



	@Override
	public void delete(String id) {
		Process process = findById(id);
		if(process != null) {
			processRepository.deleteById(process.getId());
		}
	}

}
