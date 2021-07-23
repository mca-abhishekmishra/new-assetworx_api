package com.softtek.assetworx_api.service;


import com.softtek.assetworx_api.entity.Area;

public interface AreaService {

	Area findById(String id);

	Area findByName(String name);

	Area save(Area area);

	Area update(Area area);

	boolean delete(String id);

}
