package com.softtek.assetworx_api.controller;

import static com.softtek.assetworx_api.util.Constants.CONTRACT_DELETED;
import static com.softtek.assetworx_api.util.Constants.CONTRACT_DOCUMENT;
import static com.softtek.assetworx_api.util.Constants.CONTRACT_NOTDELETED;
import static com.softtek.assetworx_api.util.Constants.CONTRACT_NOTFOUND_ID;
import static com.softtek.assetworx_api.util.Constants.CONTRACT_NOTFOUND_NUMBER;
import static com.softtek.assetworx_api.util.Constants.CONTRACT_NOTSAVED;
import static com.softtek.assetworx_api.util.Constants.CONTRACT_NOTUPDATED;

import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;
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

import com.softtek.assetworx_api.entity.AssetContract;
import com.softtek.assetworx_api.entity.Contract;
import com.softtek.assetworx_api.entity.Document;
import com.softtek.assetworx_api.exception.GenericRestException;
import com.softtek.assetworx_api.service.AssetService;
import com.softtek.assetworx_api.service.ContractService;
import com.softtek.assetworx_api.service.DocumentService;

@RestController
@RequestMapping("/contract")
public class ContractController {

	@Autowired
	ContractService contractService;
	
	@Autowired
	DocumentService documentService;
	
	@Autowired
	AssetService assetService;


	@GetMapping("/{id}")
	private ResponseEntity<?> findById(@PathVariable("id") String id) {
		Contract contract = contractService.findById(id);
		System.out.println("contract:"+contract);
		if(contract!=null) {
			return new ResponseEntity<Contract>(contract, HttpStatus.OK);
		}
		throw new GenericRestException(CONTRACT_NOTFOUND_ID + id, HttpStatus.NOT_FOUND);	
	}

	@GetMapping("/findByContractNo/{contractNo}")
	private ResponseEntity<?> findByContractNo(@PathVariable("contractNo") String contractNo) {
		Contract contract = contractService.findByContractNo(contractNo);
		if(contract!=null) {
			return new ResponseEntity<Contract>(contract, HttpStatus.OK);
		}
		throw new GenericRestException(CONTRACT_NOTFOUND_NUMBER + contractNo, HttpStatus.NOT_FOUND);	
	}
	
	@PostMapping("/")
	private ResponseEntity<?> save(@RequestBody Contract contract) {
		System.out.println(contract);
		Contract createdcontract = contractService.save(contract);
		if(createdcontract != null) {
			return new ResponseEntity<Contract>(createdcontract, HttpStatus.CREATED);
		}
		throw new GenericRestException(CONTRACT_NOTSAVED, HttpStatus.BAD_REQUEST);	
	} 
	
	@PutMapping("/")
	private ResponseEntity<?> update(@RequestBody Contract contract) {
		Contract updatedcontract = contractService.update(contract);
		if(updatedcontract != null) {
			return new ResponseEntity<Contract>(updatedcontract, HttpStatus.OK);
		}
		throw new GenericRestException(CONTRACT_NOTUPDATED, HttpStatus.BAD_REQUEST);	
	}
	
	@DeleteMapping("/{id}")
	private ResponseEntity<?> delete(@PathVariable("id") String id) {
		if(contractService.delete(id)) {
			return new ResponseEntity<String>(CONTRACT_DELETED, HttpStatus.OK);
		}
		throw new GenericRestException(CONTRACT_NOTDELETED, HttpStatus.BAD_REQUEST);	
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping("/add-asset")
	private ResponseEntity<?> addAssets(@RequestBody Map<String,Object> data) {
		List<String> assetIdList = (List<String>)data.get("assetIds");
		String contractId = (String)data.get("id");
		Map<String,String> returnData = contractService.addAssets(assetIdList, contractId);
		if(returnData == null) {
			return new ResponseEntity<String>("Assets attached successfully to the contract", HttpStatus.OK);
		}
		throw new GenericRestException(returnData.get("message"), HttpStatus.BAD_REQUEST, returnData);
	} 
	
	@PostMapping("/removeAssets")
	private ResponseEntity<String> removeAssets(@RequestBody List<String> assetContractIds) {
		List<AssetContract> inActivatedAssetContracts = contractService.inActivateAssetContracts(assetContractIds);
		if(inActivatedAssetContracts != null) {
			return new ResponseEntity<String>(inActivatedAssetContracts.size()+ " asset(s) have been removed.", HttpStatus.OK);
		}
		return new ResponseEntity<String>("Assets could not be removed.", HttpStatus.BAD_REQUEST);	
	}
	
	@PutMapping("/rework")
	private ResponseEntity<String> submitInvoiceForRework(@RequestParam String contractId, @RequestParam String comment) {
		Contract contract = contractService.findById(contractId);
		if(contract == null)
			return new ResponseEntity<String>(CONTRACT_NOTFOUND_ID, HttpStatus.NOT_FOUND);
		String status = contract.getContractStatus().getName().toUpperCase();
		if(!status.equals("APPROVAL"))
			return new ResponseEntity<String>("Contract cannot be submitted for REWORK, since it is in "+ status + " state.", HttpStatus.BAD_REQUEST);
		contract = contractService.rework(contract, comment);
		if(contract != null) {
			return new ResponseEntity<String>("Contract submitted for REWORK.", HttpStatus.OK);
		}
		return new ResponseEntity<String>("Contract could not be submitted for REWORK.", HttpStatus.BAD_REQUEST);	
	}
	
	@PutMapping("/extending")
	private ResponseEntity<String> extending(@RequestParam String contractId, @RequestParam String comment) {
		Contract contract = contractService.findById(contractId);
		if(contract == null)
			return new ResponseEntity<String>(CONTRACT_NOTFOUND_ID, HttpStatus.NOT_FOUND);
		String status = contract.getContractStatus().getName().toUpperCase();
		if(!status.equals("APPROVED"))
			return new ResponseEntity<String>("Contract cannot be EXTENDED, since it is in "+ status + " state.", HttpStatus.BAD_REQUEST);
		contract = contractService.extending(contract, comment);
		if(contract != null) {
			return new ResponseEntity<String>("Contract changed to extending state.", HttpStatus.OK);
		}
		return new ResponseEntity<String>("Contract could not be submitted for extention.", HttpStatus.BAD_REQUEST);	
	}
	
	@PutMapping("/close")
	private ResponseEntity<String> close(@RequestParam String contractId, @RequestParam String comment) {
		Contract contract = contractService.findById(contractId);
		if(contract == null)
			return new ResponseEntity<String>(CONTRACT_NOTFOUND_ID, HttpStatus.NOT_FOUND);
		String status = contract.getContractStatus().getName().toUpperCase();
		if(!status.equals("APPROVED"))
			return new ResponseEntity<String>("Contract cannot be CLOSED, since it is in "+ status + " state.", HttpStatus.BAD_REQUEST);
		contract = contractService.close(contract, comment);
		if(contract != null) {
			return new ResponseEntity<String>("Contract closed successfully.", HttpStatus.OK);
		}
		return new ResponseEntity<String>("Contract could not be closed.", HttpStatus.BAD_REQUEST);	
	}
	
	@PutMapping("/approval")
	private ResponseEntity<String> approval(@RequestParam String contractId) {
		Contract contract = contractService.findById(contractId);
		if(contract == null)
			return new ResponseEntity<String>(CONTRACT_NOTFOUND_ID, HttpStatus.NOT_FOUND);
		List<Document> documentList = documentService.findAllByRelativeIdAndDocumentType(contractId, CONTRACT_DOCUMENT);
		if(documentList == null || documentList.isEmpty())
			return new ResponseEntity<String>("Please upload the contract copy/document, before performing this action.", HttpStatus.BAD_REQUEST);
		String[] statusList = { "NEW", "REWORK" , "EXTENDING" };
		if (!Arrays.asList(statusList).contains(contract.getContractStatus().getName().toUpperCase()))
			return new ResponseEntity<String>("Contract cannot be submitted for APPROVAL, since it is in "+ contract.getContractStatus().getName() + " state.", HttpStatus.BAD_REQUEST);
		contract = contractService.approval(contract);
		if(contract != null) {
			return new ResponseEntity<String>("Contract submitted for APPROVAL.", HttpStatus.OK);
		}
		return new ResponseEntity<String>("Contract could not be submitted for APPROVAL.", HttpStatus.BAD_REQUEST);
	}
	
	@PutMapping("/approve")
	private ResponseEntity<String> approve(@RequestParam String contractId) {
		Contract contract = contractService.findById(contractId);
		if(contract == null)
			return new ResponseEntity<String>(CONTRACT_NOTFOUND_ID, HttpStatus.NOT_FOUND);
		List<Document> documentList = documentService.findAllByRelativeIdAndDocumentType(contractId, CONTRACT_DOCUMENT);
		if(documentList == null || documentList.isEmpty())
			return new ResponseEntity<String>("Please upload the contract copy/document, before performing this action.", HttpStatus.BAD_REQUEST);
		String[] statusList = { "NEW", "REWORK" , "EXTENDING", "APPROVAL" };
		if (!Arrays.asList(statusList).contains(contract.getContractStatus().getName().toUpperCase()))
			return new ResponseEntity<String>("Contract cannot be submitted for APPROVAL, since it is in "+ contract.getContractStatus().getName() + " state.", HttpStatus.BAD_REQUEST);
		contract = contractService.approve(contract);
		if(contract != null) {
			return new ResponseEntity<String>("Contract approved successfully.", HttpStatus.OK);
		}
		return new ResponseEntity<String>("Contract could not be approved.", HttpStatus.BAD_REQUEST);
	}
	
	@GetMapping("/contractSummary/{id}")
	private ResponseEntity<Object> contractSummary(@PathVariable String id) {
		Contract contract = contractService.findById(id);
		if(contract!=null) {
			Map<String, Object> contractSummary = new HashMap<String, Object>();
			int assetCount = contractService.getActiveAssetCountForContract(contract);
			contractSummary.put("currentAssetCount", assetCount);
			contractSummary.put("remainingDaysForExpiration", ChronoUnit.DAYS.between(new Date().toInstant(),contract.getAmcEndDate().toInstant()));
			return new ResponseEntity<Object>(contractSummary, HttpStatus.OK);
		}
		return new ResponseEntity<Object>(CONTRACT_NOTFOUND_ID + id, HttpStatus.NOT_FOUND);
	}
	
	
}
