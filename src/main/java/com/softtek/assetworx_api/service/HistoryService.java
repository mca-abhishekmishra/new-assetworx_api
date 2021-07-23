package com.softtek.assetworx_api.service;

import java.util.List;

import com.softtek.assetworx_api.entity.History;

public interface HistoryService {

	List<History> findAllByRelativeId(String id);

	List<History> findAllByRelativeIdOrderByLastUpdated(String relativeId);

}
