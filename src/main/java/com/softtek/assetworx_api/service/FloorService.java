package com.softtek.assetworx_api.service;

import java.util.List;

import com.softtek.assetworx_api.entity.Floor;

public interface FloorService {

	Floor findById(String id);

	Floor findByName(String name);

	Floor save(Floor floor);

	Floor update(Floor floor);

	boolean delete(String id);

	List<Floor> getAllByIsActive(boolean b);

}
