package com.softtek.assetworx_api.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.softtek.assetworx_api.service.InvoiceService;
import com.softtek.assetworx_api.service.JobServcie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class EmailScheduler {
	private static final Logger log = LoggerFactory.getLogger(EmailScheduler.class);

	@Autowired
	private JobServcie jobService;

	@Autowired
	InvoiceService invoiceService;

	 @Scheduled(cron = "0 0 1 * * *")
	public synchronized void selectReminderDate() {
		log.info("select Reminder job started.....");
		jobService.pickReminderDate();
		log.info("select Reminder job Completed.....");

	}

	@Scheduled(cron = "0 0 3 * * *")
	public synchronized void findInvoiceAndAssetWithApprovalStatusAndMail() {
		log.info("findInvoiceAndAssetWithApprovalStatusAndMail job started.....");
		invoiceService.findInvoiceAndAssetWithStateAndMail(new String[] { "Approval" });
		log.info("findInvoiceAndAssetWithApprovalStatusAndMail job Completed.....");
	}

	@Scheduled(cron = "0 0 4 * * *")
	public synchronized void findInvoiceAndAssetWithReWorkStatusAndMail() {
		log.info("findInvoiceAndAssetWithReWorkStateAndMail job started.....");
		invoiceService.findInvoiceAndAssetWithStateAndMail(new String[] { "Rework" });
		log.info("findInvoiceAndAssetWithReWorkStateAndMail job Completed.....");
	}

}
