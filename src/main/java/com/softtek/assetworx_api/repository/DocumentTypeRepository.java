package com.softtek.assetworx_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.softtek.assetworx_api.entity.DocumentType;

public interface DocumentTypeRepository extends JpaRepository<DocumentType, String> {

	Optional<DocumentType> findByName(String name);

}
