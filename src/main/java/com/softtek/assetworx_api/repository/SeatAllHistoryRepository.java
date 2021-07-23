package com.softtek.assetworx_api.repository;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.softtek.assetworx_api.entity.SeatAllHistory;

public interface SeatAllHistoryRepository extends JpaRepository<SeatAllHistory, String> {

	Optional<List<SeatAllHistory>> findByReportNameAndForDate(String reportName, Date forDate);

	@Query(name = "select field1 ,field2 ,field3 ,field4 ,field5 ,field6 ,field7 ,field8 ,field9 from SeatAllHistory"
			+ " where reportName =: reportName and Date(forDate) = Date (:forDate) ")
	Optional<Collection<Object[]>> getByReportNameAndForDate(@Param("reportName") String reportName,
			@Param("forDate") Date forDate);
}
