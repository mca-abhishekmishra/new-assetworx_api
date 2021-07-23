package com.softtek.assetworx_api.serviceImpl;

import static com.softtek.assetworx_api.util.Constants.ASSET_DOCUMENT;
import static com.softtek.assetworx_api.util.Constants.BULK_UPLOAD;
import static com.softtek.assetworx_api.util.Constants.CONTRACT_DOCUMENT;
import static com.softtek.assetworx_api.util.Constants.INVOICE_COPY;
import static com.softtek.assetworx_api.util.Constants.INVOICE_DOCUMENT;
import static com.softtek.assetworx_api.util.Constants.ORDER_COPY;
import static com.softtek.assetworx_api.util.Constants.OTHER;
import static com.softtek.assetworx_api.util.Constants.RESPONSIBILITY_LETTER;
import static com.softtek.assetworx_api.util.Constants.PURCHASE_ORDER;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.softtek.assetworx_api.entity.AssetAssignment;
import com.softtek.assetworx_api.entity.Document;
import com.softtek.assetworx_api.repository.DocumentRepository;
import com.softtek.assetworx_api.service.DocumentService;
import com.softtek.assetworx_api.service.DocumentTypeService;
import com.softtek.assetworx_api.util.PdfCreatorService;
import static com.softtek.assetworx_api.util.Constants.TEMP;
@Service
public class DocumentServiceImpl implements DocumentService {

	@Autowired
	DocumentRepository documentRepository;

	@Autowired
	DocumentTypeService documentTypeService;

	@Autowired
	Environment environment;

	@Autowired
	PdfCreatorService pdfCreatorService;

	@Override
	public Document findById(String id) {
		return documentRepository.findById(id).orElse(null);
	}

	@Override
	public Document createLiabilityLetter(AssetAssignment a1) {
		Document doc = new Document();
		String filePath = getFilePath(RESPONSIBILITY_LETTER);
		new File(filePath).mkdirs();
		String fileName = "LIABILITY_LETTER_" + a1.getAssetAssignmentType().getName();
		if(a1.getAssetAssignmentType().getName().equalsIgnoreCase("Employee")) {
			fileName += "_"+a1.getEmployee().getEmployeeId()+".pdf";
		} else {
			fileName += ".pdf";
		}
		String newFileName = System.nanoTime() + "_-_" + fileName;
		doc.setDocumentType(documentTypeService.findByName(RESPONSIBILITY_LETTER));
		doc.setRelativeId(a1.getAsset().getId());
		doc.setFilePath(filePath);
		doc.setOriginalFileName(fileName);
		doc.setFileName(newFileName);
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("assetAssignment", a1);
		pdfCreatorService.createPdf("liability_letter_pdf_template", variables,
				filePath + File.separator + newFileName);
		return documentRepository.save(doc);
	}

	@Override
	public Document save(String docType, String relativeId, MultipartFile file) {
		Document doc = new Document();
		String filePath = getFilePath(docType);
		new File(filePath).mkdirs();
		String newFileName = System.nanoTime() + "_-_" + file.getOriginalFilename();
		doc.setDocumentType(documentTypeService.findByName(docType));
		doc.setRelativeId(relativeId);
		doc.setFilePath(filePath);
		doc.setOriginalFileName(file.getOriginalFilename());
		doc.setFileName(newFileName);
		try (FileOutputStream outFile = new FileOutputStream(filePath + newFileName)) {
			outFile.write(file.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return documentRepository.save(doc);
	}

	@Override
	public Document save(String docType, String relativeId, Workbook workbook, String fileName) {
		Document doc = new Document();
		String filePath = getFilePath(docType);
		new File(filePath).mkdirs();
		String newFileName = System.nanoTime() + "_-_" + fileName;
		doc.setDocumentType(documentTypeService.findByName(docType));
		doc.setRelativeId(relativeId);
		doc.setFilePath(filePath);
		doc.setOriginalFileName(fileName);
		doc.setFileName(newFileName);
		System.out.println("filePath + newFileName==> "+filePath + newFileName);
		try (FileOutputStream file = new FileOutputStream(filePath + newFileName)) {
			workbook.write(file);
			workbook.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return documentRepository.save(doc);
	}

	@Override
	public List<Document> findAllByRelativeId(String relativeId) {
		return documentRepository.findAllByRelativeIdAndIsActiveOrderByCreatedDesc(relativeId, true);
	}

	@Override
	public List<Document> findAllByRelativeIdAndDocumentType(String relativeId, String documentType) {
		return documentRepository.findAllByRelativeIdAndDocumentTypeAndIsActive(relativeId,
				documentTypeService.findByName(documentType), true);
	}

	private String getFilePath(String docType) {
		switch (docType) {
		case (BULK_UPLOAD):
			return environment.getProperty("invoice.bulk_upload");
		case (INVOICE_COPY):
			return environment.getProperty("invoice.invoice_copy");
		case (ORDER_COPY):
			return environment.getProperty("invoice.order_copy");
		case (ASSET_DOCUMENT):
			return environment.getProperty("asset.document");
		case (INVOICE_DOCUMENT):
			return environment.getProperty("invoice.document");
		case (CONTRACT_DOCUMENT):
			return environment.getProperty("contract.document");
		case (RESPONSIBILITY_LETTER):
			return environment.getProperty("responsibility.letter");
		case (PURCHASE_ORDER):
			return environment.getProperty("purchase_order");
		case (TEMP):
			return environment.getProperty("temp");
		case (OTHER):
			return environment.getProperty("misc");
		default:
			return environment.getProperty("misc");
		}
	}

	@Override
	public void delete(String id) {
		Document document = findById(id);
		document.setActive(false);
		documentRepository.save(document);
	}

}
