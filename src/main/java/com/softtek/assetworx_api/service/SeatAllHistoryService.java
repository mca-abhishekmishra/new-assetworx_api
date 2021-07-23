package com.softtek.assetworx_api.service;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.softtek.assetworx_api.entity.SeatAllHistory;

public interface SeatAllHistoryService {

	SeatAllHistory findById(String id);

	SeatAllHistory save(SeatAllHistory seatAllHistory);

	SeatAllHistory update(SeatAllHistory seatAllHistory);
	
	public void addHistory();

	boolean delete(String id);
	
	//List<SeatAllHistory> findByReportNameAndForDate(String reportName , Date forDate);

	Collection<Object[]> getByReportNameAndForDate(String reportName, Date forDate);
}
