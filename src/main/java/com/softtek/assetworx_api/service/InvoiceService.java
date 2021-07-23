package com.softtek.assetworx_api.service;

import com.softtek.assetworx_api.entity.Invoice;

public interface InvoiceService {

	Invoice findById(String id);

	Invoice findByInvoiceNo(String invoiceNo);

	Invoice save(Invoice invoice);

	Invoice update(Invoice invoice);

	boolean delete(String id);

	Invoice submitInvoiceForApproval(Invoice invoice);

	Invoice approveInvoice(Invoice invoice);

	Invoice submitInvoiceForRework(Invoice invoice, boolean includeAssets, String comment);

	Invoice submitInvoiceForAdminRework(Invoice invoice, String comment);

	void findInvoiceWithDiscrepancy();

	void findInvoiceAndAssetWithStateAndMail(String[] statusArray);


}
