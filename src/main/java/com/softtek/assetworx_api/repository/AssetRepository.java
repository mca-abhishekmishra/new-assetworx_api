package com.softtek.assetworx_api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.softtek.assetworx_api.entity.Asset;
import com.softtek.assetworx_api.entity.Contract;
import com.softtek.assetworx_api.entity.Invoice;
import com.softtek.assetworx_api.entity.Status;

@Repository
public interface AssetRepository extends JpaRepository<Asset, String> {

	Asset findFirstByTagIdAndIdNotLike(String tagId, String id);

	Asset findFirstBySerialNoAndIdNotLike(String serialNo, String id);

	List<Asset> findByAssetStatusInAndIdIn(List<Status> statusList, List<String> assetIds);

	List<Asset> findAllByInvoiceAndAssetStatusIn(Invoice invoice, List<Status> statusList);

	@Query("SELECT SUM(a.grossValue) FROM Asset a where a.invoice = :invoice and isActive = true")
	Optional<Double> invoiceAssetsGrossValueSum(@Param("invoice") Invoice invoice);

	List<Asset> findAllByInvoiceAndIsActive(Invoice invoice, boolean isActive);

	@Query("SELECT COUNT(a.id), a.assetStatus.name FROM Asset a where a.invoice = :invoice and isActive = true Group By a.assetStatus.name")
	List<Object> assetCountForInvoiceGroupByStatus(Invoice invoice);

	List<Asset> findByAssetStatusNotInAndIdIn(List<Status> statusList, List<String> assetIdList);

	Optional<Asset> findByTagId(String id);
	
	List<Asset> findAllByContractAndIsActive(Contract contract, boolean isActive);
	
	List<Asset> findAllByInvoiceInAndIsActiveAndAssetStatusIn(List<Invoice> invoice, boolean isActive, List<Status> statusList);



}
