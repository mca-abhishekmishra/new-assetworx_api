package com.softtek.assetworx_api.controller;

import static com.softtek.assetworx_api.util.Constants.INVOICE_COPY;
import static com.softtek.assetworx_api.util.Constants.INVOICE_DELETED;
import static com.softtek.assetworx_api.util.Constants.INVOICE_NOTDELETED;
import static com.softtek.assetworx_api.util.Constants.INVOICE_NOTFOUND_ID;
import static com.softtek.assetworx_api.util.Constants.INVOICE_NOTFOUND_NUMBER;
import static com.softtek.assetworx_api.util.Constants.INVOICE_NOTSAVED;
import static com.softtek.assetworx_api.util.Constants.INVOICE_NOTUPDATED;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.softtek.assetworx_api.entity.Document;
import com.softtek.assetworx_api.entity.Invoice;
import com.softtek.assetworx_api.exception.GenericRestException;
import com.softtek.assetworx_api.service.AssetService;
import com.softtek.assetworx_api.service.DocumentService;
import com.softtek.assetworx_api.service.InvoiceService;

@RestController
@RequestMapping("/invoice")
public class InvoiceController {

	@Autowired
	InvoiceService invoiceService;
	
	@Autowired
	DocumentService documentService;
	
	@Autowired
	AssetService assetService;
	


	@GetMapping("/{id}")
	private ResponseEntity<?> findById(@PathVariable("id") String id) {
		Invoice invoice = invoiceService.findById(id);
		if(invoice!=null) {
			return new ResponseEntity<Invoice>(invoice, HttpStatus.OK);
		}
		throw new GenericRestException(INVOICE_NOTFOUND_ID + id, HttpStatus.NOT_FOUND);	
	}

	@GetMapping("/findByInvoiceNo/{invoiceNo}")
	private ResponseEntity<?> findByInvoiceNo(@PathVariable("invoiceNo") String invoiceNo) {
		Invoice invoice = invoiceService.findByInvoiceNo(invoiceNo);
		if(invoice!=null) {
			return new ResponseEntity<Invoice>(invoice, HttpStatus.OK);
		}
		throw new GenericRestException(INVOICE_NOTFOUND_NUMBER + invoiceNo, HttpStatus.NOT_FOUND);	
	}
	
	@PostMapping("/")
	private ResponseEntity<?> save(@RequestBody Invoice invoice) {
		System.out.println(invoice);
		Invoice createdinvoice = invoiceService.save(invoice);
		if(createdinvoice != null) {
			return new ResponseEntity<Invoice>(createdinvoice, HttpStatus.CREATED);
		}
		throw new GenericRestException(INVOICE_NOTSAVED, HttpStatus.BAD_REQUEST);	
	} 
	
	@PutMapping("/")
	private ResponseEntity<?> update(@RequestBody Invoice invoice) {
		Invoice updatedinvoice = invoiceService.update(invoice);
		if(updatedinvoice != null) {
			return new ResponseEntity<Invoice>(updatedinvoice, HttpStatus.OK);
		}
		throw new GenericRestException(INVOICE_NOTUPDATED, HttpStatus.BAD_REQUEST);	
	}
	
	@DeleteMapping("/{id}")
	private ResponseEntity<?> delete(@PathVariable("id") String id) {
		if(invoiceService.delete(id)) {
			return new ResponseEntity<String>(INVOICE_DELETED, HttpStatus.OK);
		}
		throw new GenericRestException(INVOICE_NOTDELETED, HttpStatus.BAD_REQUEST);	
	}
	
	@PutMapping("/submitInvoiceForRework")
	private ResponseEntity<String> submitInvoiceForRework(@RequestParam String invoiceId, @RequestParam  boolean includeAssets, @RequestParam String comment) {
		Invoice invoice = invoiceService.findById(invoiceId);
		if(invoice == null)
			return new ResponseEntity<String>(INVOICE_NOTFOUND_ID, HttpStatus.NOT_FOUND);
		String status = invoice.getInvoiceStatus().getName().toUpperCase();
		if(!status.equals("APPROVAL"))
			return new ResponseEntity<String>("Invoice cannot be submitted for REWORK, since it is in "+ status + " state.", HttpStatus.BAD_REQUEST);
		invoice = invoiceService.submitInvoiceForRework(invoice, includeAssets, comment);
		if(invoice != null) {
			return new ResponseEntity<String>("Invoice submitted for REWORK.", HttpStatus.OK);
		}
		return new ResponseEntity<String>("Invoice could not be submitted for REWORK.", HttpStatus.BAD_REQUEST);	
	}
	
	@PutMapping("/submitInvoiceForAdminRework")
	private ResponseEntity<String> submitInvoiceForAdminRework(@RequestParam String invoiceId, @RequestParam String comment) {
		Invoice invoice = invoiceService.findById(invoiceId);
		if(invoice == null)
			return new ResponseEntity<String>(INVOICE_NOTFOUND_ID, HttpStatus.NOT_FOUND);
		String status = invoice.getInvoiceStatus().getName().toUpperCase();
		if(!status.equals("APPROVED"))
			return new ResponseEntity<String>("Invoice cannot be submitted for REWORK, since it is in "+ status + " state.", HttpStatus.BAD_REQUEST);
		invoice = invoiceService.submitInvoiceForAdminRework(invoice,  comment);
		if(invoice != null) {
			return new ResponseEntity<String>("Invoice submitted for REWORK.", HttpStatus.OK);
		}
		return new ResponseEntity<String>("Invoice could not be submitted for REWORK.", HttpStatus.BAD_REQUEST);	
	}
	
	@PutMapping("/submitInvoiceForApproval")
	private ResponseEntity<String> submitInvoiceForApproval(@RequestParam String invoiceId) {
		Invoice invoice = invoiceService.findById(invoiceId);
		if(invoice == null)
			return new ResponseEntity<String>(INVOICE_NOTFOUND_ID, HttpStatus.NOT_FOUND);
		if(assetService.getActiveAssetCountForInvoice(invoice) == 0) 
			return new ResponseEntity<String>("Please add assets to the invoice, before performing this action.", HttpStatus.BAD_REQUEST);
		if(assetService.getActiveAssetCountForInvoice(invoice) != invoice.getAssetQuantity()) 
			return new ResponseEntity<String>("The invoice asset quantity and number of assets added to the invoice do not match.", HttpStatus.BAD_REQUEST);
		List<Document> documentList = documentService.findAllByRelativeIdAndDocumentType(invoiceId, INVOICE_COPY);
		if(documentList == null || documentList.isEmpty())
			return new ResponseEntity<String>("Please upload the invoice copy, before performing this action.", HttpStatus.BAD_REQUEST);
		double grossValueSum = assetService.invoiceAssetsGrossValueSum(invoice);
		System.out.println("grossValueSum:"+grossValueSum);
		if(Math.round(grossValueSum) != Math.round((invoice.getInvoiceAmount()+invoice.getOtherAmount())))
			return new ResponseEntity<String>("The sum of asset gross value and the amount mentioned in invoice do no match.", HttpStatus.BAD_REQUEST);
		String[] statusList = { "NEW", "REWORK" };
		if (!Arrays.asList(statusList).contains(invoice.getInvoiceStatus().getName().toUpperCase()))
			return new ResponseEntity<String>("Invoice cannot be submitted for APPROVAL, since it is in "+ invoice.getInvoiceStatus().getName() + " state.", HttpStatus.BAD_REQUEST);
		invoice = invoiceService.submitInvoiceForApproval(invoice);
		if(invoice != null) {
			return new ResponseEntity<String>("Invoice submitted for APPROVAL.", HttpStatus.OK);
		}
		return new ResponseEntity<String>("Invoice could not be submitted for APPROVAL.", HttpStatus.BAD_REQUEST);
	}
	
	@PutMapping("/approveInvoice")
	private ResponseEntity<String> approveInvoice(@RequestParam String invoiceId) {
		Invoice invoice = invoiceService.findById(invoiceId);
		if(invoice == null)
			return new ResponseEntity<String>(INVOICE_NOTFOUND_ID, HttpStatus.NOT_FOUND);
		if(assetService.getActiveAssetCountForInvoice(invoice) == 0) 
			return new ResponseEntity<String>("Please add assets to the invoice, before performing this action.", HttpStatus.BAD_REQUEST);
		if(assetService.getActiveAssetCountForInvoice(invoice) != invoice.getAssetQuantity()) 
			return new ResponseEntity<String>("The invoice asset quantity and number of assets added to the invoice do not match.", HttpStatus.BAD_REQUEST);
		List<Document> documentList = documentService.findAllByRelativeIdAndDocumentType(invoiceId, INVOICE_COPY);
		if(documentList == null || documentList.isEmpty())
			return new ResponseEntity<String>("Please upload the invoice copy, before performing this action.", HttpStatus.BAD_REQUEST);
		double grossValueSum = assetService.invoiceAssetsGrossValueSum(invoice);
		System.out.println("grossValueSum:"+grossValueSum);
		if(Math.round(grossValueSum) != Math.round((invoice.getInvoiceAmount()+invoice.getOtherAmount())))
			return new ResponseEntity<String>("The sum of asset gross value and the amount mentioned in invoice do no match.", HttpStatus.BAD_REQUEST);
		String[] statusList = { "NEW", "REWORK" ,"APPROVAL" };
		if (!Arrays.asList(statusList).contains(invoice.getInvoiceStatus().getName().toUpperCase()))
			return new ResponseEntity<String>("Invoice cannot be approved, since it is in "+ invoice.getInvoiceStatus().getName() + " state.", HttpStatus.BAD_REQUEST);
		invoice = invoiceService.approveInvoice(invoice);
		if(invoice != null) {
			return new ResponseEntity<String>("Invoice APPROVED successfully.", HttpStatus.OK);
		}
		return new ResponseEntity<String>("Invoice could not be APPROVED.", HttpStatus.BAD_REQUEST);	
	}
	
	@GetMapping("/invoiceSummary/{id}")
	private ResponseEntity<Object> invoiceSummary(@PathVariable String id) {
		Invoice invoice = invoiceService.findById(id);
		if(invoice!=null) {
			Map<String, Object> invoiceSummary = new HashMap<String, Object>();
			int assetCount = assetService.getActiveAssetCountForInvoice(invoice);
			invoiceSummary.put("currentAssetCount", assetCount);
			if(assetCount > 0) {
				invoiceSummary.put("currentAssetCountByStatus", assetService.assetCountForInvoiceGroupByStatus(invoice));
				invoiceSummary.put("currentAssetGrossValueSum", Math.round(assetService.invoiceAssetsGrossValueSum(invoice)));
			}
			invoiceSummary.put("invoiceTotalAmounnt", Math.round(invoice.getInvoiceAmount()+invoice.getOtherAmount()));
			return new ResponseEntity<Object>(invoiceSummary, HttpStatus.OK);
		}
		return new ResponseEntity<Object>(INVOICE_NOTFOUND_ID + id, HttpStatus.NOT_FOUND);
	}
	
}
