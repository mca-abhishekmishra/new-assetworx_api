package com.softtek.assetworx_api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.softtek.assetworx_api.entity.Floor;

@Repository
public interface FloorRepository extends JpaRepository<Floor, String> {

	Optional<Floor> findByName(String name);

	Floor findFirstByNameAndIdNotLike(String name, String id);

	List<Floor> findAllByIsActive(boolean b);

}
