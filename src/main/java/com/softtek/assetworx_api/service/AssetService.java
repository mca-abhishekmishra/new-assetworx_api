package com.softtek.assetworx_api.service;

import java.util.List;
import java.util.Map;
import com.softtek.assetworx_api.entity.Process;

import org.springframework.web.multipart.MultipartFile;

import com.softtek.assetworx_api.entity.Asset;
import com.softtek.assetworx_api.entity.AssetAssignment;
import com.softtek.assetworx_api.entity.Contract;
import com.softtek.assetworx_api.entity.Invoice;

public interface AssetService {

	Asset findById(String id);

	Asset save(Asset asset);

	Asset update(Asset asset);

	boolean delete(String id);

	Process bulkUploadHandler(MultipartFile file, String invoiceId);

	Map<String, Object> saveAll(List<Asset> assets, String saveType, Process process);

	List<Asset> inActivateAssets(List<String> assetIds);

	List<Asset> submitAssetsForApproval(List<String> assetIds);

	List<Asset> approveAssets(List<String> assetIds);

	void submitInvoiceAssetsForApproval(Invoice invoice);

	void approveInvoiceAssets(Invoice invoice);

	List<Asset> submitAssetsForRework(List<String> assetIds, String comment);

	void submitInvoiceAssetsForRework(Invoice invoice, String comment);
	
	double invoiceAssetsGrossValueSum(Invoice invoice);

	int getActiveAssetCountForInvoice(Invoice invoice);

	List<Object> assetCountForInvoiceGroupByStatus(Invoice invoice);

	void saveAssignment(Asset asset, AssetAssignment assetAssignment);

	void saveUnAssignment(AssetAssignment a1);

	List<Asset> findAllByIdIn(List<String> assetIdList);

	void attachContractToAsset(Asset asset, Contract contract);

	List<Asset> findAllByIdInAndStatusNotIn(List<String> assetIdList, String[] statusList);

	void detachContract(Asset asset);

	Asset findByTagId(String findByTagId);

	Asset update_v2(Asset asset);

	Asset retire(String status, Asset asset);
	
	int getActiveAssetCountForContract(Contract contract);

	public List<Asset> findAllByInvoiceInAndAssetStatusIn(String[] statusArray, List<Invoice> invoices) ;

}
