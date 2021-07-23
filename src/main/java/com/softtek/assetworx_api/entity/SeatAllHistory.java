package com.softtek.assetworx_api.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "seat_all_history")
public class SeatAllHistory implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@Column(name = "id")
	private String id;
	
	@Column(name = "report_name")
	private String reportName;
	
	//@JsonFormat(pattern="yyyy-MM-dd")
	@Column(name = "for_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date forDate;
	
	@Column(name = "field1")
	private String field1;
	
	@Column(name = "field2")
	private String field2;
	
	@Column(name = "field3")
	private String field3;
	
	@Column(name = "field4")
	private String field4;
	
	@Column(name = "field5")
	private String field5;
	
	@Column(name = "field6")
	private String field6;
	
	@Column(name = "field7")
	private String field7;
	
	@Column(name = "field8")
	private String field8;
	
	@Column(name = "field9")
	private String field9;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public Date getForDate() {
		return forDate;
	}

	public void setForDate(Date forDate) {
		this.forDate = forDate;
	}

	public String getField1() {
		return field1;
	}

	public void setField1(String field1) {
		this.field1 = field1;
	}

	public String getField2() {
		return field2;
	}

	public void setField2(String field2) {
		this.field2 = field2;
	}

	public String getField3() {
		return field3;
	}

	public void setField3(String field3) {
		this.field3 = field3;
	}

	public String getField4() {
		return field4;
	}

	public void setField4(String field4) {
		this.field4 = field4;
	}

	public String getField5() {
		return field5;
	}

	public void setField5(String field5) {
		this.field5 = field5;
	}

	public String getField6() {
		return field6;
	}

	public void setField6(String field6) {
		this.field6 = field6;
	}

	public String getField7() {
		return field7;
	}

	public void setField7(String field7) {
		this.field7 = field7;
	}

	public String getField8() {
		return field8;
	}

	public void setField8(String field8) {
		this.field8 = field8;
	}

	public String getField9() {
		return field9;
	}

	public void setField9(String field9) {
		this.field9 = field9;
	}

	@Override
	public String toString() {
		return "SeatAllHistory [id=" + id + ", reportName=" + reportName + ", forDate=" + forDate + ", field1=" + field1
				+ ", field2=" + field2 + ", field3=" + field3 + ", field4=" + field4 + ", field5=" + field5
				+ ", field6=" + field6 + ", field7=" + field7 + ", field8=" + field8 + ", field9=" + field9 + "]";
	}
	
	
	
	

}
