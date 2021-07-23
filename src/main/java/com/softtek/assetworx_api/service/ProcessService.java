package com.softtek.assetworx_api.service;

import com.softtek.assetworx_api.entity.Process;

public interface ProcessService {

	Process findById(String id);

	Process save(Process process);

	void delete(String id);

	Process findByRelativeId(String relativeId, String status);

	Process save(Process process, String status);

	Process save(Process process, double percentage,  String description, String status);
}
