package com.softtek.assetworx_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.softtek.assetworx_api.entity.Division;

@Repository
public interface DivisionRepository extends JpaRepository<Division, String> {

}
