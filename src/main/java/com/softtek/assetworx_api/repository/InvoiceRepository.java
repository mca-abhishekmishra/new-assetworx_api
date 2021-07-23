package com.softtek.assetworx_api.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.softtek.assetworx_api.entity.Invoice;
import com.softtek.assetworx_api.entity.Vendor;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, String> {

	Optional<Invoice> findByInvoiceNo(String invoiceNo);

	Invoice findFirstByInvoiceNoAndIdNotLike(String invoiceNo, String id);

	Invoice findFirstByInvoiceNoAndVendorAndIdNotLike(String invoiceNo, Vendor vendor, String id);

	List<Invoice> findByInvoiceStatus_NameAndIsValid(String status, boolean isValid);

	@Query("from Invoice i where Date(i.lastUpdated) <= Date(:forDate) "
			+ " and i.isActive = :isActive and i.invoiceStatus.name in  :status ")
	List<Invoice> findByInvoiceStatusNameAndIsActiveAndLastUpdatedDate(@Param("status") String[] status,
			@Param("isActive") boolean isActive,
			@Param("forDate") Date forDate);

}
