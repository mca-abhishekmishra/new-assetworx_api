package com.softtek.assetworx_api.service;

import java.util.List;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.multipart.MultipartFile;

import com.softtek.assetworx_api.entity.AssetAssignment;
import com.softtek.assetworx_api.entity.Document;

public interface DocumentService {

	Document findById(String id);

	Document save(String docType, String relativeId, MultipartFile file);
	
	Document save(String docType, String relativeId, Workbook workbook, String fileName);

	List<Document> findAllByRelativeId(String relativeId);

	List<Document> findAllByRelativeIdAndDocumentType(String relativeId, String documentType);

	void delete(String id);

	Document createLiabilityLetter(AssetAssignment a1);
}
