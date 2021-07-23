package com.softtek.assetworx_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.softtek.assetworx_api.entity.Floor;
import com.softtek.assetworx_api.entity.Area;

public interface AreaRepository extends JpaRepository<Area, String> {

	Optional<Area> findByName(String name);

	Area findFirstByNameAndIdNotLike(String name, String id);

	Area findFirstByNameAndIdNotLikeAndFloorNotLike(String name, String id, Floor floor);

	Area findFirstByNameAndIdNotLikeAndFloor(String name, String id, Floor floor);

}
