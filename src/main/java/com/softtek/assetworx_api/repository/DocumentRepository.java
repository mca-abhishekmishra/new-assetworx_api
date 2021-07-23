package com.softtek.assetworx_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.softtek.assetworx_api.entity.Document;
import com.softtek.assetworx_api.entity.DocumentType;

@Repository
public interface DocumentRepository extends JpaRepository<Document, String> {

	List<Document> findAllByRelativeId(String relativeId);
	
	List<Document> findAllByRelativeIdAndDocumentType(String relativeId, DocumentType documentType);

	List<Document> findAllByRelativeIdAndIsActive(String relativeId, boolean isActive);

	List<Document> findAllByRelativeIdAndIsActiveOrderByCreatedDesc(String relativeId, boolean isActive);

	List<Document> findAllByRelativeIdAndDocumentTypeAndIsActive(String relativeId, DocumentType findByName, boolean isActive);


}
