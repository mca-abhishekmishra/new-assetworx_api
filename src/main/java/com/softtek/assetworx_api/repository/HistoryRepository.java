package com.softtek.assetworx_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.softtek.assetworx_api.entity.History;

public interface HistoryRepository extends JpaRepository<History, String> {

	List<History> findAllByRelativeId(String id);

	List<History> findAllByRelativeIdOrderByLastUpdated(String relativeId);

	List<History> findAllByRelativeIdOrderByLastUpdatedDesc(String relativeId);

}
