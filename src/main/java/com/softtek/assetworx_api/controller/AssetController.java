package com.softtek.assetworx_api.controller;

import static com.softtek.assetworx_api.util.Constants.ASSETS_NOTSAVED;
import static com.softtek.assetworx_api.util.Constants.ASSET_DELETED;
import static com.softtek.assetworx_api.util.Constants.ASSET_NOTDELETED;
import static com.softtek.assetworx_api.util.Constants.ASSET_NOTFOUND_ID;
import static com.softtek.assetworx_api.util.Constants.ASSET_NOTSAVED;
import static com.softtek.assetworx_api.util.Constants.ASSET_NOTUPDATED;

import java.util.Arrays;
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
import org.springframework.web.multipart.MultipartFile;

import com.softtek.assetworx_api.entity.Asset;
import com.softtek.assetworx_api.entity.Process;
import com.softtek.assetworx_api.exception.GenericRestException;
import com.softtek.assetworx_api.service.AssetService;

@RestController
@RequestMapping("/asset")
public class AssetController {

	@Autowired
	AssetService assetService;


	@GetMapping("/{id}")
	private ResponseEntity<?> findById(@PathVariable("id") String id) {
		Asset asset = assetService.findById(id);
		if(asset!=null) {
			return new ResponseEntity<Asset>(asset, HttpStatus.OK);
		}
		throw new GenericRestException(ASSET_NOTFOUND_ID + id, HttpStatus.NOT_FOUND);	
	}
	
	@GetMapping("/findByTagId/{tagId}")
	private ResponseEntity<?> findByTagId(@PathVariable("tagId") String tagId) {
		System.out.println(tagId);
		Asset asset = assetService.findByTagId(tagId);
		if(asset!=null) {
			return new ResponseEntity<Asset>(asset, HttpStatus.OK);
		}
		return new ResponseEntity<Asset>(HttpStatus.NOT_FOUND);	
	}
	
	@PostMapping("/")
	private ResponseEntity<?> save(@RequestBody Asset asset) {
		System.out.println(asset);
		Asset createdasset = assetService.save(asset);
		if(createdasset != null) {
			return new ResponseEntity<Asset>(createdasset, HttpStatus.CREATED);
		}
		throw new GenericRestException(ASSET_NOTSAVED, HttpStatus.BAD_REQUEST);	
	}
	
	@PostMapping("/saveAll")
	private ResponseEntity<Map<String,Object>> saveAll(@RequestBody List<Asset> assets) {
		Map<String,Object> returnData = assetService.saveAll(assets, "multiple", null);
		if(returnData.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.CREATED);
		}
		else {
			throw new GenericRestException(ASSETS_NOTSAVED, HttpStatus.BAD_REQUEST,returnData);
		}
	}
	
	@PostMapping("/bulkUpload")
	public ResponseEntity<Object> bulkUpload( @RequestParam("file") MultipartFile file,@RequestParam String invoiceId ){
		Process process = assetService.bulkUploadHandler(file, invoiceId);
		return new ResponseEntity<>(process, HttpStatus.OK);
	}
	
	@PutMapping("/")
	private ResponseEntity<?> update(@RequestBody Asset asset) {
		Asset updatedasset = assetService.update(asset);
		if(updatedasset != null) {
			return new ResponseEntity<Asset>(updatedasset, HttpStatus.OK);
		}
		throw new GenericRestException(ASSET_NOTUPDATED, HttpStatus.BAD_REQUEST);	
	}
	
	@PutMapping("/update_v2")
	private ResponseEntity<?> update_v2(@RequestBody Asset asset) {
		Asset updatedasset = assetService.update_v2(asset);
		if(updatedasset != null) {
			return new ResponseEntity<Asset>(updatedasset, HttpStatus.OK);
		}
		throw new GenericRestException(ASSET_NOTUPDATED, HttpStatus.BAD_REQUEST);	
	}
	
	@DeleteMapping("/{id}")
	private ResponseEntity<?> delete(@PathVariable("id") String id) {
		if(assetService.delete(id)) {
			return new ResponseEntity<String>(ASSET_DELETED, HttpStatus.OK);
		}
		throw new GenericRestException(ASSET_NOTDELETED, HttpStatus.BAD_REQUEST);	
	}
	
	@PostMapping("/removeAssets")
	private ResponseEntity<String> removeAssets(@RequestBody List<String> assetIds) {
		List<Asset> inActivatedAssets = assetService.inActivateAssets(assetIds);
		if(inActivatedAssets != null) {
			return new ResponseEntity<String>(inActivatedAssets.size()+ " asset(s) have been removed.", HttpStatus.OK);
		}
		return new ResponseEntity<String>("Assets could not be removed.", HttpStatus.BAD_REQUEST);	
	}
	
	@PostMapping("/submitAssetsForApproval")
	private ResponseEntity<String> submitAssetsForApproval(@RequestBody List<String> assetIds) {
		List<Asset> assetsInApproval = assetService.submitAssetsForApproval(assetIds);
		if(assetsInApproval != null) {
			return new ResponseEntity<String>(assetsInApproval.size()+ " asset(s) have been submitted for approval.", HttpStatus.OK);
		}
		return new ResponseEntity<String>("Assets could not be submitted for approval.", HttpStatus.BAD_REQUEST);	
	}
	
	@PostMapping("/submitAssetsForRework")
	private ResponseEntity<String> submitAssetsForRework(@RequestBody Map<String, Object> data) {
		@SuppressWarnings("unchecked")
		List<Asset> assetsInRework = assetService.submitAssetsForRework((List<String>)data.get("assetIds"), (String)data.get("comment"));
		if(assetsInRework != null) {
			return new ResponseEntity<String>(assetsInRework.size()+ " asset(s) have been submitted for rework.", HttpStatus.OK);
		}
		return new ResponseEntity<String>("Assets could not be submitted for rework.", HttpStatus.BAD_REQUEST);	
	}
	
	@PostMapping("/approveAssets")
	private ResponseEntity<String> approveAssets(@RequestBody List<String> assetIds) {
		List<Asset> approvedAssets = assetService.approveAssets(assetIds);
		if(approvedAssets != null) {
			return new ResponseEntity<String>(approvedAssets.size()+ " asset(s) have been approved.", HttpStatus.OK);
		}
		return new ResponseEntity<String>("Assets could not be approved.", HttpStatus.BAD_REQUEST);	
	}
	
	@SuppressWarnings("unused")
	@PutMapping("/retire")
	private ResponseEntity<String> retire(@RequestParam String status, @RequestParam String assetId) {
		System.out.println("status:"+status);
		System.out.println("assetId:"+assetId);
		Asset asset = assetService.findById(assetId);
		String[] statusList = { "NEW", "APPROVAL", "REWORK", "LOST", "DAMAGED", "RETIRED" };
		String assetStatus = asset.getAssetStatus().getName().toUpperCase();
		if(asset == null) {
			return new ResponseEntity<String>(ASSET_NOTFOUND_ID, HttpStatus.NOT_FOUND);
		}
		else if (Arrays.asList(statusList).contains(assetStatus)) {
			return new ResponseEntity<String>("Asset not eligible to be retired.", HttpStatus.BAD_REQUEST);
		} else if(assetStatus.equalsIgnoreCase("ASSIGNED")) {
			return new ResponseEntity<String>("Please unassign the asset before marking the asset as retired.", HttpStatus.BAD_REQUEST);
		} else {
			asset = assetService.retire(status, asset);
			if(asset != null) {
				return new ResponseEntity<String>("Asset is marked as retired.", HttpStatus.OK);
			}
			return new ResponseEntity<String>("Asset could not be marked as retired.", HttpStatus.BAD_REQUEST);
		}
	}
	
}
