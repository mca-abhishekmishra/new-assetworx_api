package com.softtek.assetworx_api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.softtek.assetworx_api.entity.Status;

public interface StatusRepository extends JpaRepository<Status, String> {

	Optional<Status> findByName(String name);

	List<Status> findAllByNameIn(List<String> asList);

}
