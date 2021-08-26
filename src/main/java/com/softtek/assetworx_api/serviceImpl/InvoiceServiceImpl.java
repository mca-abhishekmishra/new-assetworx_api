package com.softtek.assetworx_api.serviceImpl;

import static com.softtek.assetworx_api.util.Constants.INVOICE_COPY;
import static com.softtek.assetworx_api.util.Constants.INVOICE_EXISTS;
import static com.softtek.assetworx_api.util.Constants.INVOICE_NOTFOUND_ID;
import static com.softtek.assetworx_api.util.Constants.INVOICE_NOTSAVED;
import static com.softtek.assetworx_api.util.Constants.NEW;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Validator;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.softtek.assetworx_api.entity.Document;
import com.softtek.assetworx_api.entity.Invoice;
import com.softtek.assetworx_api.entity.Status;
import com.softtek.assetworx_api.exception.GenericRestException;
import com.softtek.assetworx_api.exception.InvalidEntityException;
import com.softtek.assetworx_api.model.Mail;
import com.softtek.assetworx_api.repository.InvoiceRepository;
import com.softtek.assetworx_api.service.AssetService;
import com.softtek.assetworx_api.service.DocumentService;
import com.softtek.assetworx_api.service.InvoiceService;
import com.softtek.assetworx_api.service.StatusService;
import com.softtek.assetworx_api.service.VendorService;
import com.softtek.assetworx_api.util.EmailSenderService;

@Service
public class InvoiceServiceImpl implements InvoiceService {

	@Autowired
	InvoiceRepository invoiceRepository;

	@Autowired
	DocumentService documentService;

	@Autowired
	Validator validator;

	@Autowired
	VendorService vendorService;

	@Autowired
	StatusService statusService;

	@Autowired
	AssetService assetService;

	@Autowired
	EmailSenderService emailSenderService;

	@Autowired
	HttpServletRequest httpServletRequest;

	@Autowired
	TaskExecutor executor;

	@Autowired
	Environment env;

	@Override
	public Invoice findById(String id) {
		return invoiceRepository.findById(id).orElse(null);
	}

	@Override
	public Invoice findByInvoiceNo(String invoiceNo) {
		return invoiceRepository.findByInvoiceNo(invoiceNo).orElse(null);
	}

	public boolean validate(Invoice invoice) {
		List<String> messages = validator.validate(invoice).stream()
				.map(e -> e.getPropertyPath() + ":" + e.getMessage()).collect(Collectors.toList());
		Invoice f = invoiceRepository.findFirstByInvoiceNoAndVendorAndIdNotLike(invoice.getInvoiceNo(),
				invoice.getVendor(), invoice.getId());
		if (f != null) {
			messages.add(INVOICE_EXISTS + invoice.getInvoiceNo());
		}
		System.out.println(messages.size());
		if (!messages.isEmpty()) {
			throw new InvalidEntityException(INVOICE_NOTSAVED, messages);
		}
		return true;
	}

	@Override
	public Invoice save(Invoice invoice) {
		invoice.setId("");
		invoice.setVendor(vendorService.findById((invoice.getVendor() != null ? invoice.getVendor().getId() : "")));
		invoice.setInvoiceStatus(statusService.findByName(NEW));
		if (validate(invoice)) {
			invoice.setValid(false);
			Invoice savedInvoice = invoiceRepository.save(invoice);
			executor.execute(() -> {
				String mailTo = env.getProperty("mail.finance");
				String mailCc = env.getProperty("mail.finance");
				String subject = "NEW INVOICE CREATED";
				Map<String, Object> variables = new HashMap<String, Object>();
				variables.put("name", "TEAM");
				variables.put("body",
						"This is to inform you that a new invoice " + invoice.getInvoiceNo() + " was created.");
				Mail mail = new Mail(mailTo, mailCc, subject, null, variables, "html_template",
						"/invoice/details/" + savedInvoice.getId(), "invoice");
				emailSenderService.sendEmail(mail);
			});
			return savedInvoice;
		}
		return null;
	}

	@Override
	public Invoice update(Invoice invoice) {
		Invoice f = findById(invoice.getId());
		if (f == null) {
			throw new GenericRestException(INVOICE_NOTFOUND_ID + invoice.getId(), HttpStatus.NOT_FOUND);
		}
		if (f.getInvoiceStatus().getName().equalsIgnoreCase("APPROVED")) {
			throw new GenericRestException(
					"Invoice cannot be update because it is in " + f.getInvoiceStatus().getName() + " state.",
					HttpStatus.NOT_FOUND);
		} else {
			f.setInvoiceNo(invoice.getInvoiceNo());
			f.setPoNo(invoice.getPoNo());
			f.setInvoiceDate(invoice.getInvoiceDate());
			f.setVendor(vendorService.findById((invoice.getVendor() != null ? invoice.getVendor().getId() : "")));
			f.setInvoiceAmount(invoice.getInvoiceAmount());
			f.setOtherAmount(invoice.getOtherAmount());
			f.setAssetQuantity(invoice.getAssetQuantity());
			f.setDescription(invoice.getDescription());
			f.setComment(invoice.getComment());

			if (validate(f)) {
				f.setValid(false);
				Invoice savedInvoice = invoiceRepository.save(f);
				executor.execute(() -> {
					String mailTo = env.getProperty("mail.finance");
					String mailCc = env.getProperty("mail.finance");
					String subject = "INVOICE UPDATED";
					Map<String, Object> variables = new HashMap<String, Object>();
					variables.put("name", "TEAM");
					variables.put("body",
							"This is to inform you that the invoice " + f.getInvoiceNo() + " was updated.");
					Mail mail = new Mail(mailTo, mailCc, subject, null, variables, "html_template",
							"/invoice/details/" + f.getId(), "update");
					emailSenderService.sendEmail(mail);
				});
				return savedInvoice;
			}
			return null;
		}
	}

	public boolean isDeletable(Invoice invoice) {
		/*
		 * if (invoice.getAssets().size()>0) { throw new
		 * ResourceNotDeletableException("invoice cannot be deleted since assets with this invoice exists."
		 * ); }
		 */
		return true;
	}

	@Override
	public boolean delete(String id) {
		Invoice invoice = findById(id);
		if (invoice == null) {
			throw new GenericRestException(INVOICE_NOTFOUND_ID + id, HttpStatus.NOT_FOUND);
		}
		if (isDeletable(invoice)) {
			invoice.setActive(false);
			invoice.setInvoiceNo(invoice.getInvoiceNo() + "~" + System.nanoTime());
			invoice.setValid(false);
			invoiceRepository.save(invoice);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Invoice submitInvoiceForRework(Invoice invoice, boolean includeAssets, String comment) {
		Status status = statusService.findByName("Rework");
		if (includeAssets) {
			assetService.submitInvoiceAssetsForRework(invoice, comment);
		}
		invoice.setInvoiceStatus(status);
		invoice.setHiddenComment(comment);
		invoice.setValid(false);
		Invoice savedInvoice = invoiceRepository.save(invoice);
		executor.execute(() -> {
			String mailTo = env.getProperty("mail.finance");
			String mailCc = env.getProperty("mail.finance");
			String subject = "INVOICE REVERTED TO REWORK";
			Map<String, Object> variables = new HashMap<String, Object>();
			variables.put("name", "TEAM");
			variables.put("comment", comment);
			variables.put("body", "This is to inform you that the invoice " + invoice.getInvoiceNo()
					+ " is reverted back to REWORK. Please take necessary actions.");
			Mail mail = new Mail(mailTo, mailCc, subject, null, variables, "html_template",
					"/invoice/details/" + invoice.getId(), "rework");
			emailSenderService.sendEmail(mail);
		});
		return savedInvoice;
	}

	@Override
	public Invoice submitInvoiceForAdminRework(Invoice invoice, String comment) {
		Status status = statusService.findByName("Rework");
		invoice.setInvoiceStatus(status);
		invoice.setHiddenComment(comment);
		invoice.setValid(false);
		Invoice savedInvoice = invoiceRepository.save(invoice);
		executor.execute(() -> {
			String mailTo = env.getProperty("mail.finance");
			String mailCc = env.getProperty("mail.finance");
			String subject = "INVOICE REVERTED TO REWORK";
			Map<String, Object> variables = new HashMap<String, Object>();
			variables.put("name", "TEAM");
			variables.put("comment", comment);
			variables.put("body", "This is to inform you that the invoice " + invoice.getInvoiceNo()
					+ " is reverted back to REWORK. Please take necessary actions.");
			Mail mail = new Mail(mailTo, mailCc, subject, null, variables, "html_template",
					"/invoice/details/" + invoice.getId(), "rework");
			emailSenderService.sendEmail(mail);
		});
		return savedInvoice;
	}

	@Override
	public Invoice submitInvoiceForApproval(Invoice invoice) {
		Status status = statusService.findByName("Approval");
		assetService.submitInvoiceAssetsForApproval(invoice);
		invoice.setInvoiceStatus(status);
		Invoice savedInvoice = invoiceRepository.save(invoice);
		executor.execute(() -> {
			String mailTo = env.getProperty("mail.finance");
			String mailCc = env.getProperty("mail.finance");
			String subject = "INVOICE SUBMITTED TO APPROVAL";
			Map<String, Object> variables = new HashMap<String, Object>();
			variables.put("name", "TEAM");
			variables.put("body", "This is to inform you that the invoice " + invoice.getInvoiceNo()
					+ " is submitted for approval. Please take necessary actions.");
			Mail mail = new Mail(mailTo, mailCc, subject, null, variables, "html_template",
					"/invoice/details/" + invoice.getId(), "approval");
			emailSenderService.sendEmail(mail);
		});
		return savedInvoice;
	}

	@Override
	public Invoice approveInvoice(Invoice invoice) {
		Status status = statusService.findByName("Approved");
		invoice.setApprovedBy(httpServletRequest.getHeader("username"));
		invoice.setApprovedDate(new Date());
		assetService.approveInvoiceAssets(invoice);
		invoice.setInvoiceStatus(status);
		Invoice savedInvoice = invoiceRepository.save(invoice);
		executor.execute(() -> {
			String mailTo = env.getProperty("mail.finance");
			String mailCc = env.getProperty("mail.finance");
			String subject = "INVOICE APPROVED";
			Map<String, Object> variables = new HashMap<String, Object>();
			variables.put("name", "TEAM");
			variables.put("body", "This is to inform you that the invoice " + invoice.getInvoiceNo() + " is approved.");
			Mail mail = new Mail(mailTo, mailCc, subject, null, variables, "html_template",
					"/invoice/details/" + invoice.getId(), "reminder");
			emailSenderService.sendEmail(mail);
		});
		return savedInvoice;
	}

	@Override
	public void findInvoiceWithDiscrepancy() {

		List<Invoice> approvedInvoices = invoiceRepository.findByInvoiceStatus_NameAndIsValid("Approved", false);

		System.out.println("approvedInvoices : " + approvedInvoices.size());
		String filePath = "/opt/application/documents/invoice/discrepant/";
		String fileName = System.nanoTime()+".xls";
		File f = new File(filePath);
		f.mkdirs();
		boolean sendFlag = false;
		int rowNo = 1;
		try (Workbook workbook = new HSSFWorkbook()) {
			Sheet sheet = workbook.createSheet("discrepancies");
			/*
			 * headers at row 0
			 */

			CellStyle cellStyle = workbook.createCellStyle();
			cellStyle.setAlignment(HorizontalAlignment.CENTER);
			cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			cellStyle.setWrapText(true);

			Row headers = sheet.createRow(0);
			Cell headerCell = headers.createCell(0);
			headerCell.setCellValue("S.No.");
			headerCell.setCellStyle(cellStyle);

			headerCell = headers.createCell(1);
			headerCell.setCellValue("Invoice No.");
			headerCell.setCellStyle(cellStyle);

			headerCell = headers.createCell(2);
			headerCell.setCellValue("PO No.");
			headerCell.setCellStyle(cellStyle);

			headerCell = headers.createCell(3);
			headerCell.setCellValue("Message");
			headerCell.setCellStyle(cellStyle);

			for (Invoice invoice : approvedInvoices) {
				System.out.println(" iteration ");
				String message = "";
				if (assetService.getActiveAssetCountForInvoice(invoice) != invoice.getAssetQuantity())
					message += "The invoice asset quantity and number of assets added to the invoice do not match. \n";
				List<Document> documentList = documentService.findAllByRelativeIdAndDocumentType(invoice.getId(),
						INVOICE_COPY);
				// if (documentList == null || documentList.isEmpty())
					// message += "Please upload the invoice copy, before performing this action. \n";
				double grossValueSum = assetService.invoiceAssetsGrossValueSum(invoice);
				System.out.println("grossValueSum:" + grossValueSum);
				if (Math.round(grossValueSum) != Math.round((invoice.getInvoiceAmount() + invoice.getOtherAmount())))
					message += "The sum of asset gross value and the amount mentioned in invoice do no match.  ";
				System.out.println("message for Invoice id " + invoice.getId() + " : " + message);

				if (!message.trim().isEmpty()) {
					sendFlag = true;
					// add the inovice details and message in the excel file
					Row row = sheet.createRow(rowNo);
					row.createCell(0).setCellValue(rowNo);
					row.createCell(1).setCellValue(invoice.getInvoiceNo());
					row.createCell(2).setCellValue(invoice.getPoNo());
					row.createCell(3).setCellValue(message);
					row.setRowStyle(cellStyle);
					rowNo++;

				} else {
					// set the flag to of invoice to true }
					System.out.println("invoice is valid " + invoice.getId());
					invoice.setValid(true);
					invoiceRepository.saveAndFlush(invoice);
				}

			}
			if (sendFlag) {
				sheet.autoSizeColumn(0);
				sheet.autoSizeColumn(1);
				sheet.autoSizeColumn(2);
				sheet.autoSizeColumn(3);
				// write the excel file
				FileOutputStream stream = new FileOutputStream(filePath + fileName);
				workbook.write(stream);
				stream.close();

				Document document = new Document();
				document.setFilePath(filePath);
				document.setFileName(fileName);
				document.setOriginalFileName(fileName);

				String mailTo = env.getProperty("mail.finance");
				String mailCc =  env.getProperty("mail.finance");
				String subject = "Invoice Discrepancy";

				Map<String, Object> variables = new HashMap<String, Object>();
				// variables.put("name", process.getCreatedBy());
				variables.put("body",
						"This is to inform you that we have found a few discrepancies in the approved invoices."
								+ " We request you to correct them, in order to avoid such notifications. "
								+ "You can find more details about it in the excel sheet attached to this email..");

				Mail mail = new Mail(mailTo, mailCc, subject, document, variables, "html_template", "/invoice",
						"reminder");
				emailSenderService.sendEmail(mail);
				workbook.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void findInvoiceAndAssetWithStateAndMail(String[] statusArray) {
		System.out.println("findInvoiceAndAssetWithStateAndMail");
		// fetch yesterday or invoices in approval state
		Date yesterdayDate = java.sql.Date.valueOf(LocalDate.now().minusDays(1));
		System.out.println("yesterdayDate :" + yesterdayDate);
		List<Invoice> invoiceInApprovalList = invoiceRepository
				.findByInvoiceStatusNameAndIsActiveAndLastUpdatedDate(statusArray, true, yesterdayDate);
		System.out.println("-------------invoiceInApprovalList--------" + invoiceInApprovalList.size() + "--------");
		int invoiceInApprivalCount = invoiceInApprovalList.size();
		int assetInApprovalCount = assetService.findAllByInvoiceInAndAssetStatusIn(statusArray, invoiceInApprovalList)
				.size();

		System.out.println("-------------assetInApprovalCount---------" + assetInApprovalCount + "------");
		String mailTo = env.getProperty("mail.finance");
		String mailCc = env.getProperty("mail.finance");
		String subject = "Gentel Reminder";

		Map<String, Object> variables = new HashMap<String, Object>();
		/*
		 * for time being i am using first array index value to send mail to different
		 * uses
		 */
		String body = "";
		if (statusArray[0] != null && statusArray[0].trim().equalsIgnoreCase("Approval")
				&& (invoiceInApprivalCount > 0 || assetInApprovalCount > 0)) {
			body = "This is to inform you that the ";
			if (assetInApprovalCount > 0) {
				body += assetInApprovalCount + " assets ";
			}
			if (assetInApprovalCount > 0 && assetInApprovalCount > 0) {
				body += "and ";
			}
			if (invoiceInApprivalCount > 0) {
				body += invoiceInApprivalCount + " invoices ";
			}
			body += "are in " + statusArray[0] + " state. Please review them and do the needfull... ";
			variables.put("body", body);
			Mail mail = new Mail(mailTo, mailCc, subject, null, variables, "html_template", "/invoice",
					"reminder");
			emailSenderService.sendEmail(mail);
		} else if (statusArray[0] != null && statusArray[0].trim().equalsIgnoreCase("rework")
				&& (invoiceInApprivalCount > 0 || assetInApprovalCount > 0)) {
			body = "this is to inform you that the ";
			if (assetInApprovalCount > 0) {
				body += assetInApprovalCount + " assets ";
			}
			if (assetInApprovalCount > 0 && assetInApprovalCount > 0) {
				body += "and ";
			}
			if (invoiceInApprivalCount > 0) {
				body += invoiceInApprivalCount + " invoices ";
			}
			body += "are in " + statusArray[0]
					+ " state. Please make necessary corrections and submit for approval again.. ";
			variables.put("body", body);
			Mail mail = new Mail(mailTo, mailCc, subject, null, variables, "html_template", "/invoice", "reminder");
			emailSenderService.sendEmail(mail);

		} else {
			// in future add more conditions here
		}
	}
}
