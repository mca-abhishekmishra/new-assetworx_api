package com.softtek.assetworx_api.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softtek.assetworx_api.entity.History;
import com.softtek.assetworx_api.repository.HistoryRepository;
import com.softtek.assetworx_api.service.HistoryService;

@Service
public class HistoryServiceImpl implements HistoryService {

	
	@Autowired
	HistoryRepository historyRepository;
	
	@Override
	public List<History> findAllByRelativeId(String id) {
		return historyRepository.findAllByRelativeId(id);
	}

	@Override
	public List<History> findAllByRelativeIdOrderByLastUpdated(String relativeId) {
		return historyRepository.findAllByRelativeIdOrderByLastUpdatedDesc(relativeId);
	}

}
