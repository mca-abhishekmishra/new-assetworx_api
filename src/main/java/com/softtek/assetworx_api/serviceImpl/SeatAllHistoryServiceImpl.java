package com.softtek.assetworx_api.serviceImpl;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softtek.assetworx_api.entity.SeatAllHistory;
import com.softtek.assetworx_api.repository.SeatAllHistoryRepository;
import com.softtek.assetworx_api.service.ReportService;
import com.softtek.assetworx_api.service.SeatAllHistoryService;

@Service
public class SeatAllHistoryServiceImpl implements SeatAllHistoryService {

	@Autowired
	ReportService reportService;

	@Autowired
	SeatAllHistoryRepository seatAllHistoryRepository;

	@Override
	public SeatAllHistory findById(String id) {
		return null;
	}

	@Override
	public SeatAllHistory save(SeatAllHistory seatAllHistory) {
		seatAllHistory.setId("");
		return seatAllHistoryRepository.save(seatAllHistory);
	}

	@Override
	public SeatAllHistory update(SeatAllHistory seatAllHistory) {
		return null;
	}

	@Override
	public boolean delete(String id) {
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(rollbackOn = Exception.class)
	public void addHistory() {
		// yesterday
		Date yesterdayDate = java.sql.Date.valueOf(LocalDate.now().minusDays(1));
		
		List<Object[]> floorReport = ((Collection<Object[]>) reportService.getSeatallReport("floor-report", null)).stream().collect(Collectors.toList());
		seatAllHistoryRepository.saveAll(CreateSeatAllHistoryEntityList("floor-report", yesterdayDate, floorReport));
		
		List<Object[]> departmentReport = (List<Object[]>) reportService.getSeatallReport("department-report", null);
		seatAllHistoryRepository
				.saveAll(CreateSeatAllHistoryEntityList("department-report", yesterdayDate, departmentReport));
		
		List<Object[]> cdsReport = (List<Object[]>) reportService.getSeatallReport("cds-report", null);
		seatAllHistoryRepository.saveAll(CreateSeatAllHistoryEntityList("cds-report", yesterdayDate, cdsReport));
		
	}

	private List<SeatAllHistory> CreateSeatAllHistoryEntityList(String reportName, Date forDate, List<Object[]> data) {
		List<SeatAllHistory> output = new ArrayList<SeatAllHistory>();
		for (Object[] arraysData : data) {
			try {
				output.add(CreateSeatAllHistoryEntity(reportName, forDate, arraysData));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return output;
	}

	private SeatAllHistory CreateSeatAllHistoryEntity(String reportName, Date forDate, Object[] data)
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		SeatAllHistory obj = new SeatAllHistory();
		obj.setForDate(forDate);
		obj.setReportName(reportName);
		Class<? extends SeatAllHistory> clazz = obj.getClass();
		for (int i = 1; i < data.length; i++) {
			Field field = clazz.getField("field" + i);
			field.setAccessible(true);
			field.set(obj, data[i]);
		}
		return obj;
	}

	@Override
	public Collection<Object[]> getByReportNameAndForDate(String reportName, Date forDate) {
		return seatAllHistoryRepository.getByReportNameAndForDate(reportName, forDate).orElse(null);
	}

}
