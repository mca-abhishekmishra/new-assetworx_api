package com.softtek.assetworx_api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.softtek.assetworx_api.entity.Document;
import com.softtek.assetworx_api.service.DocumentService;

@RestController
@RequestMapping("/document")
public class DocumentController {

	@Autowired
	DocumentService documentService;

	@GetMapping("/{id}")
	private ResponseEntity<?> findById(@PathVariable("id") String id) {
		Document document = documentService.findById(id);
		return new ResponseEntity<Document>(document, HttpStatus.OK);
	}
	
	@GetMapping("/findAllByRelativeId/{relativeId}")
	private ResponseEntity<List<Document>> findAllByRelativeId(@PathVariable("relativeId") String relativeId) {
		List<Document> documentList = documentService.findAllByRelativeId(relativeId);
		return new ResponseEntity<List<Document>>(documentList, HttpStatus.OK);
	}

	@GetMapping("/getPath/{id}")
	private ResponseEntity<String> getPath(@PathVariable("id") String id) {
		Document document = documentService.findById(id);
		if (document != null) {
			String path = document.getFilePath() + document.getFileName();
			return new ResponseEntity<String>(path, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PostMapping("/upload")
	public ResponseEntity<Document> upload(@RequestParam("file") MultipartFile file, @RequestParam String relativeId,@RequestParam String documentType) {
		Document document = documentService.save(documentType, relativeId, file);
		return new ResponseEntity<Document>(document, HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	private ResponseEntity<?> delete(@PathVariable("id") String id) {
		documentService.delete(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
