package com.softtek.assetworx_api.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.softtek.assetworx_api.service.SeatAllHistoryService;

@Component
public class SeatAllHistoryScheduler {

	private static final Logger log = LoggerFactory.getLogger(SeatAllHistoryScheduler.class);

	@Autowired
	SeatAllHistoryService seatAllHistoryService;
	
	// will run at 12:01:00 everyday
	// @Scheduled(cron = "0 1 0 * * *")
	public synchronized void addHistory() {

		log.info("addHistory job started.....");
		seatAllHistoryService.addHistory();
		log.info("addHistory job Completed.....");

	}

}
