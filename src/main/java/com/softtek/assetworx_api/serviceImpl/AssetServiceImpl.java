package com.softtek.assetworx_api.serviceImpl;

import static com.softtek.assetworx_api.util.Constants.ASSET_EXISTS_SERIALNO;
import static com.softtek.assetworx_api.util.Constants.ASSET_EXISTS_TAGID;
import static com.softtek.assetworx_api.util.Constants.ASSET_EXPIRY_DATE_ERROR;
import static com.softtek.assetworx_api.util.Constants.ASSET_NOTFOUND_ID;
import static com.softtek.assetworx_api.util.Constants.ASSET_NOTSAVED;
import static com.softtek.assetworx_api.util.Constants.BULK_UPLOAD;
import static com.softtek.assetworx_api.util.Constants.COMPLETED;
import static com.softtek.assetworx_api.util.Constants.IN_PROGRESS;
import static com.softtek.assetworx_api.util.Constants.NEW;
import static com.softtek.assetworx_api.util.Constants.TEMP;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Validator;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softtek.assetworx_api.entity.Asset;
import com.softtek.assetworx_api.entity.AssetAssignment;
import com.softtek.assetworx_api.entity.AssetModel;
import com.softtek.assetworx_api.entity.AssetType;
import com.softtek.assetworx_api.entity.Category;
import com.softtek.assetworx_api.entity.Contract;
import com.softtek.assetworx_api.entity.Document;
import com.softtek.assetworx_api.entity.Invoice;
import com.softtek.assetworx_api.entity.LicenseType;
import com.softtek.assetworx_api.entity.Manufacturer;
import com.softtek.assetworx_api.entity.OperatingSystem;
import com.softtek.assetworx_api.entity.Process;
import com.softtek.assetworx_api.entity.Status;
import com.softtek.assetworx_api.entity.SubCategory;
import com.softtek.assetworx_api.exception.GenericRestException;
import com.softtek.assetworx_api.exception.InvalidEntityException;
import com.softtek.assetworx_api.model.Mail;
import com.softtek.assetworx_api.repository.AssetRepository;
import com.softtek.assetworx_api.service.AssetLocationService;
import com.softtek.assetworx_api.service.AssetModelService;
import com.softtek.assetworx_api.service.AssetService;
import com.softtek.assetworx_api.service.AssetTypeService;
import com.softtek.assetworx_api.service.CategoryService;
import com.softtek.assetworx_api.service.ContractService;
import com.softtek.assetworx_api.service.DocumentService;
import com.softtek.assetworx_api.service.InvoiceService;
import com.softtek.assetworx_api.service.LicenseTypeService;
import com.softtek.assetworx_api.service.ManufacturerService;
import com.softtek.assetworx_api.service.OperatingSystemService;
import com.softtek.assetworx_api.service.ProcessService;
import com.softtek.assetworx_api.service.StatusService;
import com.softtek.assetworx_api.service.SubCategoryService;
import com.softtek.assetworx_api.util.EmailSenderService;
import com.softtek.assetworx_api.util.ExcelUtil;

@Service
public class AssetServiceImpl implements AssetService {

	@Autowired
	AssetRepository assetRepository;

	@Autowired
	Validator validator;

	@Autowired
	ContractService contractService;

	@Autowired
	AssetTypeService assetTypeService;

	@Autowired
	CategoryService categoryService;

	@Autowired
	SubCategoryService subCategoryService;

	@Autowired
	StatusService statusService;

	@Autowired
	InvoiceService invoiceService;

	@Autowired
	ManufacturerService manufacturerService;

	@Autowired
	AssetModelService assetModelService;

	@Autowired
	OperatingSystemService operatingSystemService;

	@Autowired
	LicenseTypeService licenseTypeService;
	
	@Autowired
	AssetLocationService assetLocationService;

	@Autowired
	TaskExecutor executor;

	@Autowired
	ObjectMapper mapper;

	@Autowired
	DocumentService documentService;

	@Autowired
	ProcessService processService;

	@Autowired
	HttpServletRequest httpServletRequest;

	@Autowired
	EmailSenderService emailSenderService;

	@Autowired
	Environment env;

	private final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

	@Override
	public Asset findById(String id) {
		return assetRepository.findById(id).orElse(null);
	}

	@Override
	public Asset findByTagId(String id) {
		return assetRepository.findByTagId(id).orElse(null);
	}

	public boolean validate(Asset asset, boolean isSave) {
		List<String> messages = validator.validate(asset).stream().map(e -> e.getPropertyPath() + ":" + e.getMessage())
				.collect(Collectors.toList());
		Asset a = assetRepository.findFirstByTagIdAndIdNotLike(asset.getTagId(), asset.getId());
		if (a != null)
			messages.add(ASSET_EXISTS_TAGID + asset.getTagId());
		a = assetRepository.findFirstBySerialNoAndIdNotLike(asset.getSerialNo(), asset.getId());
		if (a != null)
			messages.add(ASSET_EXISTS_SERIALNO + asset.getSerialNo());
		if (asset.getWarrantyStartDate() == null || asset.getWarrantyEndDate() == null
				|| (asset.getWarrantyStartDate().after(asset.getWarrantyEndDate()))) {
			messages.add(ASSET_EXPIRY_DATE_ERROR);
		}
		if (isSave && asset.getInvoice() != null) {
			if (asset.getInvoice().getInvoiceStatus().getName().equalsIgnoreCase("APPROVED")) {
				messages.add("Asset cannot be added to the invoice, because the invoice is in "
						+ asset.getInvoice().getInvoiceStatus().getName() + " state.");
			}
		}
		if (!messages.isEmpty()) {
			throw new InvalidEntityException(ASSET_NOTSAVED, messages);
		}
		return true;

	}

	@Override
	public Asset save(Asset asset) {
		asset.setId("");
		asset.setCreatedBy(httpServletRequest.getHeader("username"));
		asset.setInvoice(invoiceService.findById(asset.getInvoice() != null ? asset.getInvoice().getId() : ""));
//		asset.setContract(contractService.findById(asset.getContract() != null ? asset.getContract().getId() : ""));
		asset.setAssetStatus(statusService.findByName(NEW));
		asset.setAssetType(assetTypeService.findById(asset.getAssetType() != null ? asset.getAssetType().getId() : ""));
		asset.setCategory(categoryService.findById(asset.getCategory() != null ? asset.getCategory().getId() : ""));
		asset.setSubCategory(
				subCategoryService.findById(asset.getSubCategory() != null ? asset.getSubCategory().getId() : ""));
		asset.setLicenseType(
				licenseTypeService.findById(asset.getLicenseType() != null ? asset.getLicenseType().getId() : ""));
		asset.setTagId(this.getTagId(asset));
		asset.setWarrantyStartDate(asset.getWarrantyStartDate() != null ? asset.getWarrantyStartDate() : new Date());
		asset.setManufacturer(
				manufacturerService.findById(asset.getManufacturer() != null ? asset.getManufacturer().getId() : ""));
		asset.setAssetModel(
				assetModelService.findById(asset.getAssetModel() != null ? asset.getAssetModel().getId() : ""));
		asset.setOperatingSystem(operatingSystemService
				.findById(asset.getOperatingSystem() != null ? asset.getOperatingSystem().getId() : ""));
		if (validate(asset, true)) {
			return assetRepository.save(asset);
		}
		return null;
	}

	public Asset save_v2(Asset asset) {
		asset.setId("");
		asset.setInvoice(invoiceService.findById(asset.getInvoice() != null ? asset.getInvoice().getId() : ""));
		/*
		 * asset.setContract(contractService .findByContractNo(asset.getContract() !=
		 * null ? asset.getContract().getContractNo() : ""));
		 */
		asset.setAssetStatus(statusService.findByName(NEW));
		AssetType assetType = assetTypeService
				.findByName(asset.getAssetType() != null ? asset.getAssetType().getName() : "");
		asset.setAssetType(assetType);
		Category category = categoryService.findByAssetTypeAndName(assetType,
				(asset.getCategory() != null ? asset.getCategory().getName() : ""));
		asset.setCategory(category);
		SubCategory subCategory = subCategoryService.findByCategoryAndName(category,
				(asset.getSubCategory() != null ? asset.getSubCategory().getName() : ""));
		asset.setSubCategory(subCategory);
		asset.setLicenseType(
				licenseTypeService.findByName(asset.getLicenseType() != null ? asset.getLicenseType().getName() : ""));
		asset.setTagId(this.getTagId(asset));
		asset.setWarrantyStartDate(asset.getWarrantyStartDate() != null ? asset.getWarrantyStartDate() : new Date());
		asset.setManufacturer(manufacturerService
				.findByName(asset.getManufacturer() != null ? asset.getManufacturer().getName() : ""));
		asset.setAssetModel(
				assetModelService.findByName(asset.getAssetModel() != null ? asset.getAssetModel().getName() : ""));
		asset.setOperatingSystem(operatingSystemService
				.findByName(asset.getOperatingSystem() != null ? asset.getOperatingSystem().getName() : ""));
		if (validate(asset, true)) {
			return assetRepository.save(asset);
		}
		return null;
	}

	private String getTagId(Asset asset) {
		if (asset.getTagId() != null && !asset.getTagId().isEmpty()) {
			return asset.getTagId();
		} else {
			/*
			 * return asset.getSubCategory() != null ? asset.getSubCategory().getTagId() +
			 * "-" + System.nanoTime() : "Tag-Id-" + System.nanoTime();
			 */
			return "TAG-ID-" + System.nanoTime();
		}
	}

	@Override
	public Asset update(Asset asset) {
		Asset a = findById(asset.getId());
		if (a == null) {
			throw new GenericRestException(ASSET_NOTFOUND_ID + asset.getId(), HttpStatus.NOT_FOUND);
		} else {
			String[] statusList = { "NEW", "APPROVAL", "REWORK" };
			if (Arrays.asList(statusList).contains(a.getAssetStatus().getName().toUpperCase())) {
				a.setAssetType(
						assetTypeService.findById(asset.getAssetType() != null ? asset.getAssetType().getId() : ""));
				a.setCategory(categoryService.findById(asset.getCategory() != null ? asset.getCategory().getId() : ""));
				a.setSubCategory(subCategoryService
						.findById(asset.getSubCategory() != null ? asset.getSubCategory().getId() : ""));
				a.setSerialNo(asset.getSerialNo());
				// a.setGrossValue(asset.getGrossValue());
				a.setManufacturer(manufacturerService
						.findById(asset.getManufacturer() != null ? asset.getManufacturer().getId() : ""));
			}
			/*
			 * a.setContract(contractService.findById(asset.getContract() != null ?
			 * asset.getContract().getId() : ""));
			 */
			a.setLicenseType(
					licenseTypeService.findById(asset.getLicenseType() != null ? asset.getLicenseType().getId() : ""));
			String[] statusList1 = { "NEW", "APPROVAL", "REWORK", "APPROVED" };
			if (Arrays.asList(statusList1).contains(a.getAssetStatus().getName().toUpperCase())) {
				a.setTagId(this.getTagId(asset));
			}
			a.setWarrantyStartDate(asset.getWarrantyStartDate() != null ? asset.getWarrantyStartDate() : new Date());
			a.setWarrantyEndDate(asset.getWarrantyEndDate());
			a.setAssetModel(
					assetModelService.findById(asset.getAssetModel() != null ? asset.getAssetModel().getId() : ""));
			a.setOperatingSystem(operatingSystemService
					.findById(asset.getOperatingSystem() != null ? asset.getOperatingSystem().getId() : ""));
			a.setDescription(asset.getDescription());
			a.setComment(asset.getComment());
			a.setAssetLocation(
					assetLocationService.findById(asset.getAssetLocation() != null ? asset.getAssetLocation().getId() : ""));
			if (validate(a, false)) {

				return assetRepository.save(a);
			}
			return null;
		}
	}

	public Asset update_v2(Asset asset) {
		Asset a = findById(asset.getId());
		if (a == null) {
			throw new GenericRestException(ASSET_NOTFOUND_ID + asset.getId(), HttpStatus.NOT_FOUND);
		} else if (a.getInvoice().getInvoiceStatus().getName().equalsIgnoreCase("APPROVED")) {
			throw new GenericRestException("Asset cannot be updated because the invoice is Approved",
					HttpStatus.BAD_REQUEST);
		} else {
			a.setAssetType(assetTypeService.findById(asset.getAssetType() != null ? asset.getAssetType().getId() : ""));
			a.setCategory(categoryService.findById(asset.getCategory() != null ? asset.getCategory().getId() : ""));
			a.setSubCategory(
					subCategoryService.findById(asset.getSubCategory() != null ? asset.getSubCategory().getId() : ""));
			a.setSerialNo(asset.getSerialNo());
			a.setGrossValue(asset.getGrossValue());
			a.setManufacturer(manufacturerService
					.findById(asset.getManufacturer() != null ? asset.getManufacturer().getId() : ""));
			/*
			 * a.setContract(contractService.findById(asset.getContract() != null ?
			 * asset.getContract().getId() : ""));
			 */
			a.setLicenseType(
					licenseTypeService.findById(asset.getLicenseType() != null ? asset.getLicenseType().getId() : ""));
			// a.setTagId(this.getTagId(asset));
			a.setWarrantyStartDate(asset.getWarrantyStartDate() != null ? asset.getWarrantyStartDate() : new Date());
			a.setWarrantyEndDate(asset.getWarrantyEndDate());

			a.setAssetModel(
					assetModelService.findById(asset.getAssetModel() != null ? asset.getAssetModel().getId() : ""));
			a.setOperatingSystem(operatingSystemService
					.findById(asset.getOperatingSystem() != null ? asset.getOperatingSystem().getId() : ""));

			a.setDescription(asset.getDescription());
			a.setComment(asset.getComment());
			if (validate(a, false)) {
				return assetRepository.save(a);
			}
			return null;
		}
	}

	public boolean isDeletable(Asset asset) {
		/*
		 * if (asset.getAssets().size()>0) { throw new
		 * ResourceNotDeletableException("asset cannot be deleted since assets with this asset exists."
		 * ); }
		 */
		return true;
	}

	@Override
	public boolean delete(String id) {
		Asset asset = findById(id);
		if (asset == null) {
			throw new GenericRestException(ASSET_NOTFOUND_ID + id, HttpStatus.NOT_FOUND);
		}
		if (isDeletable(asset)) {
			asset.setActive(false);
			asset.setTagId(asset.getTagId() + "~" + System.nanoTime());
			asset.setSerialNo(asset.getSerialNo() + "~" + System.nanoTime());
			assetRepository.save(asset);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Map<String, Object> saveAll(List<Asset> assets, String saveType, Process process) {
		Map<String, Object> unsavedAssetDetails = new HashMap<String, Object>();
		int[] i = { 0 };
		assets.stream().filter(a -> a != null).forEach(asset -> {
			String id = asset.getId();
			try {
				i[0] = i[0] + 1;
				Asset savedAsset = null;
				if (saveType.equals("multiple")) {
					savedAsset = this.save(asset);
				} else {
					String description = assets.size() + " assets(s) have been validated, " + i[0] + " out of "
							+ assets.size() + " assets have been proccessed.";
					processService.save(process, ((i[0] * 100.0f) / assets.size()), description, IN_PROGRESS);
					System.out.println("createdBy:" + process.getCreatedBy());
					asset.setCreatedBy(process.getCreatedBy());
					savedAsset = this.save_v2(asset);
				}
				if (savedAsset == null) {
					unsavedAssetDetails.put(id, "[ASSET could not be saved due to an unknown error.]");
				}
			} catch (InvalidEntityException e) {
				e.printStackTrace();
				unsavedAssetDetails.put(id, e.getMessages());
			} catch (Exception e) {
				e.printStackTrace();
				unsavedAssetDetails.put(id, "[ASSET could not be saved due to an unknown error.]");
			}
		});
		return unsavedAssetDetails;
	}

	@Override
	public Process bulkUploadHandler(MultipartFile file, String invoiceId) {
		Process process = new Process();
		process.setRelativeId(invoiceId);
		process.setProcessStatus(statusService.findByName(IN_PROGRESS));
		process.setProcessRefId("BULK_UPLOAD_" + System.nanoTime());
		process.setProcessPercentage(0);
		process.setDescription("Validating and uploading assets...");
		Process pr = processService.save(process);
		Document document = documentService.save(TEMP, pr.getProcessRefId(), file);
		executor.execute(() -> {
			bulkUpload(document, invoiceId, pr);
		});
		return pr;
	}

	private void bulkUpload(Document doc, String invoiceId, Process process) {
		int x = 0, y = 0, z = 0;
		String description = "";
		Document document;
		Invoice invoice = invoiceService.findById(invoiceId);
		System.out.println("file===:" + new File(doc.getFilePath() + doc.getFileName()));
		try (Workbook workbook = WorkbookFactory.create(new File(doc.getFilePath() + doc.getFileName()))) {
			Sheet sheet = convertCellTypeToString(workbook.getSheetAt(0));
			List<Asset> assets = convertExcelRowDataToAssetList(sheet, invoiceId);
			x = assets.size();
			Map<String, Object> assetsData = saveAll(assets, "bulk", process);
			y = assetsData.size();
			appendDetailsToSheet(workbook, assetsData);
			z = x - y;
			document = documentService.save(BULK_UPLOAD, invoiceId, workbook, doc.getOriginalFileName());
			description = "{\"message\":\"RefId - " + process.getProcessRefId() + ". " + x + " - records proccssed. "
					+ z + " - records added. " + y + " - records failed to add. \", \"doc_id\":\"" + document.getId()
					+ "\"}";
			String mailTo = env.getProperty("mail.finance");
			String mailCc = env.getProperty("mail.finance");
			String subject = process.getProcessRefId();
			Map<String, Object> variables = new HashMap<String, Object>();
			variables.put("name", process.getCreatedBy());
			variables.put("body", "The Bulk upload against invoice " + invoice.getInvoiceNo()
					+ " is completed. Please check the attachment in this email for details about bulk upload.");
			Mail mail = new Mail(mailTo, mailCc, subject, document, variables, "html_template",
					"/invoice/details/" + invoiceId, "bulk-upload-success");
			processService.save(process, 100, description, COMPLETED);
			emailSenderService.sendEmail(mail);
		} catch (Exception e) {
			System.out.println("Bulk Upload Failed:" + process.getProcessRefId());
			e.printStackTrace();
			description = "{\"message\":\"RefId - " + process.getProcessRefId() + ". " + x + " - records proccssed. "
					+ z + " - records added. " + y + " - records failed to add. \"" + "\"}";
			String mailTo = env.getProperty("mail.finance");
			String mailCc = env.getProperty("mail.finance");
			String subject = process.getProcessRefId();
			Map<String, Object> variables = new HashMap<String, Object>();
			variables.put("name", process.getCreatedBy());
			variables.put("body", "The Bulk upload against invoice " + invoice.getInvoiceNo()
					+ " failed. Please upload the template again, if the issue still persists, please contact your administrator.");
			Mail mail = new Mail(mailTo, mailCc, subject, null, variables, "html_template",
					"/invoice/details/" + invoiceId, "bulk-upload-failed");
			processService.save(process, 100, description, COMPLETED);
			emailSenderService.sendEmail(mail);
		}
	}

	private List<Asset> convertExcelRowDataToAssetList(Sheet sheet, String invoiceId) {
		List<Asset> assets = new ArrayList<Asset>();
		for (Row row : sheet) {
			if (row.getRowNum() == 0 || row.getCell(0).getStringCellValue() == "") {
				continue;
			}
			Asset asset = new Asset();
			try {
				asset.setInvoice(new Invoice(invoiceId));
				asset.setId(row.getCell(0).getStringCellValue());
				assets.add(getAssetFromRow(asset, row, invoiceId));
			} catch (Exception e) {
				e.printStackTrace();
				assets.add(asset);
			}
		}
		return assets;
	}

	private Asset getAssetFromRow(Asset asset, Row row, String invoiceId) {
		try {
			/* asset.setTagId(row.getCell(1).getStringCellValue()); */
			asset.setSerialNo((row.getCell(1).getStringCellValue()));
			asset.setAssetType(new AssetType(row.getCell(2).getStringCellValue()));
			asset.setCategory(new Category(row.getCell(3).getStringCellValue()));
			asset.setSubCategory(new SubCategory(row.getCell(4).getStringCellValue()));
			double grossValue = row.getCell(5).getStringCellValue().equals("") ? 0
					: Double.valueOf(row.getCell(5).getStringCellValue());
			asset.setGrossValue(grossValue);
			Date startDate = row.getCell(6).getStringCellValue().equals("") ? null
					: DATE_FORMAT.parse(row.getCell(6).getStringCellValue());
			asset.setWarrantyStartDate(startDate);
			Date endDate = row.getCell(7).getStringCellValue().equals("") ? null
					: DATE_FORMAT.parse(row.getCell(7).getStringCellValue());
			asset.setWarrantyEndDate(endDate);
			asset.setLicenseType(new LicenseType(row.getCell(8).getStringCellValue()));
			asset.setAssetModel(new AssetModel((row.getCell(9).getStringCellValue())));
			asset.setOperatingSystem(new OperatingSystem((row.getCell(10).getStringCellValue())));
			asset.setManufacturer(new Manufacturer((row.getCell(11).getStringCellValue())));
			/* asset.setContract(new Contract(row.getCell(13).getStringCellValue())); */
			asset.setDescription((row.getCell(12).getStringCellValue()));
			asset.setComment((row.getCell(13).getStringCellValue()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return asset;
	}

	private Sheet convertCellTypeToString(Sheet sheet) {
		for (Row row : sheet) {
			for (int i = 0; i <= row.getLastCellNum() - 1; i++) {
				if (row.getCell(i) == null) {
					row.createCell(i).setCellValue("");
					row.getCell(i).setCellType(CellType.STRING);
				}
				row.getCell(i).setCellType(CellType.STRING);
			}
		}
		return sheet;
	}

	private Workbook appendDetailsToSheet(Workbook workbook, Map<String, Object> assetsData) {
		CellStyle style1 = ExcelUtil.getCellStyle(workbook, "RED");
		CellStyle style2 = ExcelUtil.getCellStyle(workbook, "GREEN");
		CellStyle style3 = ExcelUtil.getCellStyle(workbook, "BROWN");
		Set<String> keys = assetsData.keySet();
		String message = "";
		for (Row row : workbook.getSheetAt(0)) {
			if (row.getRowNum() == 0) {
				row.createCell(14).setCellValue("Remarks");
				row.createCell(15).setCellValue("Message");
			} else if (row.getCell(0).getStringCellValue() == "") {
				row.createCell(14).setCellStyle(style3);
				row.getCell(14).setCellValue("Asset was not added, due to missing sl no.");
				row.createCell(15).setCellValue("Record Ignored");
			} else if (keys.contains(row.getCell(0).getStringCellValue())) {
				row.createCell(14).setCellStyle(style1);
				row.getCell(14).setCellValue("Asset was not added");
				try {
					message = mapper.writeValueAsString(assetsData.get(row.getCell(0).getStringCellValue()));
				} catch (JsonProcessingException e) {
					e.printStackTrace();
					message = "Unexpected error occured";
				}
				row.createCell(15).setCellValue(message);
			} else {
				row.createCell(14).setCellStyle(style2);
				row.getCell(14).setCellValue("Asset was added successfully");
				row.createCell(15).setCellValue("NA");
			}
		}
		return workbook;
	}

	@Override
	public List<Asset> inActivateAssets(List<String> assetIds) {
		List<Status> statusList = statusService.findAllByNameIn(new String[] { "New", "Rework", "Approval" });
		List<Asset> inActivatedAssets = assetRepository.findByAssetStatusInAndIdIn(statusList, assetIds);
		inActivatedAssets.forEach(a -> {
			a.setActive(false);
			a.setSerialNo(a.getSerialNo().concat("~").concat("" + System.nanoTime()));
			a.setTagId(a.getTagId().concat("~").concat("" + System.nanoTime()));
		});
		List<Asset> inActivatedAssets1 = assetRepository.saveAll(inActivatedAssets);
		executor.execute(() -> {
			String mailTo = env.getProperty("mail.finance");
			String mailCc = env.getProperty("mail.finance");
			String subject = "ASSET(S) REMOVED FROM INVOICE";
			Map<String, Object> variables = new HashMap<String, Object>();
			variables.put("name", "TEAM");
			variables.put("body",
					"This is to inform you that " + inActivatedAssets.size()
							+ " asset(s) have been removed from the invoice "
							+ inActivatedAssets.get(0).getInvoice().getInvoiceNo() + ".");
			Mail mail = new Mail(mailTo, mailCc, subject, null, variables, "html_template",
					"/invoice/details/" + inActivatedAssets.get(0).getInvoice().getId(), "removed");
			emailSenderService.sendEmail(mail);
		});
		return inActivatedAssets1;
	}

	@Override
	public List<Asset> submitAssetsForApproval(List<String> assetIds) {
		List<Status> statusList = statusService.findAllByNameIn(new String[] { "New", "Rework" });
		Status status = statusService.findByName("Approval");
		List<Asset> assetsInApproval = assetRepository.findByAssetStatusInAndIdIn(statusList, assetIds);
		List<Asset> assetsInApproval1 = this.changeAssetStatus(assetsInApproval, status, null);
		executor.execute(() -> {
			String mailTo = env.getProperty("mail.finance");
			String mailCc = env.getProperty("mail.finance");
			String subject = "ASSET(S) SUBMITTED FOR APPROVAL.";
			Map<String, Object> variables = new HashMap<String, Object>();
			variables.put("name", "TEAM");
			variables.put("body",
					"This is to inform you that " + assetsInApproval.size()
							+ " asset(s) have been submitted for approval from the invoice "
							+ assetsInApproval.get(0).getInvoice().getInvoiceNo() + ". Please take necessary actions.");
			Mail mail = new Mail(mailTo, mailCc, subject, null, variables, "html_template",
					"/invoice/details/" + assetsInApproval.get(0).getInvoice().getId(), "approval");
			emailSenderService.sendEmail(mail);
		});
		return assetsInApproval1;
	}

	@Override
	public List<Asset> submitAssetsForRework(List<String> assetIds, String comment) {
		List<Status> statusList = statusService.findAllByNameIn(new String[] { "Approval" });
		Status status = statusService.findByName("Rework");
		List<Asset> assetsInRework = assetRepository.findByAssetStatusInAndIdIn(statusList, assetIds);
		List<Asset> assetsInRework1 = this.changeAssetStatus(assetsInRework, status, comment);
		executor.execute(() -> {
			String mailTo = env.getProperty("mail.finance");
			String mailCc = env.getProperty("mail.finance");
			String subject = "ASSET(S) SUBMITTED FOR REWORK.";
			Map<String, Object> variables = new HashMap<String, Object>();
			variables.put("name", "TEAM");
			variables.put("comment", comment);
			variables.put("body",
					"This is to inform you that " + assetsInRework.size()
							+ " asset(s) have been submitted for rework from the invoice "
							+ assetsInRework.get(0).getInvoice().getInvoiceNo() + ". Please take necessary actions.");
			Mail mail = new Mail(mailTo, mailCc, subject, null, variables, "html_template",
					"/invoice/details/" + assetsInRework.get(0).getInvoice().getId(), "rework");
			emailSenderService.sendEmail(mail);
		});
		return assetsInRework1;
	}

	@Override
	public List<Asset> approveAssets(List<String> assetIds) {
		List<Status> statusList = statusService.findAllByNameIn(new String[] { "New", "Rework", "Approval" });
		Status status = statusService.findByName("Approved");
		List<Asset> approvedAssets = assetRepository.findByAssetStatusInAndIdIn(statusList, assetIds);
		List<Asset> approvedAssets1 = this.changeAssetStatus(approvedAssets, status, null);
		;
		executor.execute(() -> {
			String mailTo = env.getProperty("mail.finance");
			String mailCc = env.getProperty("mail.finance");
			String subject = "ASSET(S) APPROVED.";
			Map<String, Object> variables = new HashMap<String, Object>();
			variables.put("name", "TEAM");
			variables.put("body",
					"This is to inform you that " + approvedAssets.size()
							+ " asset(s) have been approved from the invoice "
							+ approvedAssets.get(0).getInvoice().getInvoiceNo() + ".");
			Mail mail = new Mail(mailTo, mailCc, subject, null, variables, "html_template",
					"/invoice/details/" + approvedAssets.get(0).getInvoice().getId(), "approved");
			emailSenderService.sendEmail(mail);
		});
		return approvedAssets1;
	}

	@Override
	public void submitInvoiceAssetsForRework(Invoice invoice, String comment) {
		List<Status> statusList = statusService.findAllByNameIn(new String[] { "Approval" });
		Status status = statusService.findByName("Rework");
		List<Asset> assetList = assetRepository.findAllByInvoiceAndAssetStatusIn(invoice, statusList);
		this.changeAssetStatus(assetList, status, comment);
	}

	@Override
	public void submitInvoiceAssetsForApproval(Invoice invoice) {
		List<Status> statusList = statusService.findAllByNameIn(new String[] { "New", "Rework" });
		Status status = statusService.findByName("Approval");
		List<Asset> assetList = assetRepository.findAllByInvoiceAndAssetStatusIn(invoice, statusList);
		this.changeAssetStatus(assetList, status, null);
	}

	@Override
	public void approveInvoiceAssets(Invoice invoice) {
		List<Status> statusList = statusService.findAllByNameIn(new String[] { "New", "Rework", "Approval" });
		Status status = statusService.findByName("Approved");
		List<Asset> assetList = assetRepository.findAllByInvoiceAndAssetStatusIn(invoice, statusList);
		this.changeAssetStatus(assetList, status, null);
	}

	private List<Asset> changeAssetStatus(List<Asset> assetList, Status status, String comment) {
		if (assetList != null && !assetList.isEmpty()) {
			assetList.forEach(a -> {
				a.setAssetStatus(status);
				if (status.getName().equalsIgnoreCase("Approved")) {
					a.setAssetApprovedBy(httpServletRequest.getHeader("username"));
					a.setApprovedDate(new Date());
				} else if (status.getName().equalsIgnoreCase("Rework")) {
					a.setHiddenComment(comment);
				}
			});
			return assetRepository.saveAll(assetList);
		}
		return null;

	}

	@Override
	public double invoiceAssetsGrossValueSum(Invoice invoice) {
		return assetRepository.invoiceAssetsGrossValueSum(invoice).orElse(Double.valueOf(0));
	}

	@Override
	public int getActiveAssetCountForInvoice(Invoice invoice) {
		return assetRepository.findAllByInvoiceAndIsActive(invoice, true).size();
	}

	@Override
	public List<Object> assetCountForInvoiceGroupByStatus(Invoice invoice) {
		return assetRepository.assetCountForInvoiceGroupByStatus(invoice);
	}

	@Override
	public void saveAssignment(Asset asset, AssetAssignment assetAssignment) {
		asset.setHiddenComment("Asset was assigned to " + assetAssignment.getAssetAssignmentType().getName() + " by "
				+ httpServletRequest.getHeader("username"));
		asset.setAssetStatus(statusService.findByName("Assigned"));
		asset.setAssetAssignment(assetAssignment);
		assetRepository.save(asset);
	}

	@Override
	public void saveUnAssignment(AssetAssignment a1) {
		Asset asset = a1.getAsset();
		asset.setHiddenComment("Asset was unassigned from " + a1.getAssetAssignmentType().getName() + " by "
				+ httpServletRequest.getHeader("username"));
		asset.setAssetStatus(statusService.findByName("Unassigned"));
		asset.setAssetAssignment(null);
		assetRepository.save(asset);

	}

	@Override
	public List<Asset> findAllByIdIn(List<String> assetIdList) {
		// TODO Auto-generated method stub
		return assetRepository.findAllById(assetIdList);
	}

	@Override
	public void attachContractToAsset(Asset asset, Contract contract) {
		asset.setContract(contract);
		assetRepository.save(asset);

	}

	@Override
	public void detachContract(Asset asset) {
		asset.setContract(null);
		assetRepository.save(asset);

	}

	@Override
	public List<Asset> findAllByIdInAndStatusNotIn(List<String> assetIdList, String[] statusArray) {
		List<Status> statusList = statusService.findAllByNameIn(statusArray);
		List<Asset> assetList = assetRepository.findByAssetStatusNotInAndIdIn(statusList, assetIdList);
		return assetList;
	}

	@Override
	public Asset retire(String statusName, Asset asset) {
		Status status = statusService.findByName(statusName);
		asset.setAssetStatus(status);
		return assetRepository.save(asset);
	}

	@Override
	public int getActiveAssetCountForContract(Contract contract) {
		return assetRepository.findAllByContractAndIsActive(contract, true).size();
	}

	public List<Asset> findAllByInvoiceInAndAssetStatusIn(String[] statusArray, List<Invoice> invoices) {
		List<Status> statusList = statusService.findAllByNameIn(statusArray);
		return assetRepository.findAllByInvoiceInAndIsActiveAndAssetStatusIn(invoices, true, statusList);
	};

}
