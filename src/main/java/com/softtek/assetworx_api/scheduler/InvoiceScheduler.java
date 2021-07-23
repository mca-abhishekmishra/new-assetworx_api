package com.softtek.assetworx_api.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.softtek.assetworx_api.service.InvoiceService;

@Component
public class InvoiceScheduler {
	private static final Logger log = LoggerFactory.getLogger(InvoiceScheduler.class);

	@Autowired
	private InvoiceService invoiceService;

	@Scheduled(cron = "0 0 1 * * *")
	public synchronized void findInvoiceWithDiscrepancy() {
		log.info("findInvoiceWithDiscrepancy job started.....");
		invoiceService.findInvoiceWithDiscrepancy();
		log.info("findInvoiceWithDiscrepancy job Completed.....");
	}

}
