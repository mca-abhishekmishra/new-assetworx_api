package com.softtek.assetworx_api.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="employee")
@EntityListeners(AuditingEntityListener.class)
public class Employee {

	@Id
	@GenericGenerator(name = "uuid",strategy = "uuid2")
	@GeneratedValue(generator = "uuid")
	private String id;

	@NotBlank(message="Employee name is required")
	@Column(nullable=false)
	private String name;

	@NotBlank(message="Employee id is required")
	@Column(nullable=false, unique=true)
	private String employeeId;

	@NotBlank(message="Employee ISID is required")
	@Column(nullable=false, unique=true)
	private String isid;

	@NotBlank(message="Email is required")
	@Column(nullable=false, unique=true)
	private String email;
	
	private String mobileNo;
	
	private String voip;

	@JsonIgnoreProperties({"createdBy","created","lastUpdated","lastUpdatedBy","description","active"})
	@ManyToOne
	@JoinColumn(name = "department_id")
	private Department department;

	@NotBlank(message="Employment Type is required")
	@Column(nullable=false)
	private String employmentType; 

	
	 @Column(nullable=false) 
	 private boolean employmentStatus = true;
	
	@Column(name = "lastWorkingDate")
	@JsonFormat(pattern="yyyy-MM-dd")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastWorkingDate;

	@JsonIgnoreProperties({"createdBy","created","lastUpdated","lastUpdatedBy","description","active"})
	@ManyToOne
	@JoinColumn(name = "project_id")
	private Project project;

	/*
	 * @NotBlank(message="Reporting To Type is required")
	 * 
	 * @Column(nullable=false) private String reportingTo;
	 */

	@JsonIgnoreProperties({"createdBy","created","lastUpdated","lastUpdatedBy","description","active", "reportingTo"})
	@ManyToOne
	@JoinColumn(name="reportingTo")
	private Employee reportingTo;
	
	

	@CreatedBy
	private String createdBy;

	@CreatedDate
	private Date created;

	@LastModifiedBy
	private String lastUpdatedBy;

	@LastModifiedDate
	private Date lastUpdated;

	private boolean isActive = true;

	public String getEmploymentType() {
		return employmentType;
	}

	public void setEmploymentType(String employmentType) {
		this.employmentType = employmentType;
	}

	/*
	 * public boolean isEmploymentStatus() { return employmentStatus; }
	 * 
	 * public void setEmploymentStatus(boolean employmentStatus) {
	 * this.employmentStatus = employmentStatus; }
	 */

	public Date getLastWorkingDate() {
		return lastWorkingDate;
	}

	public void setLastWorkingDate(Date lastWorkingDate) {
		this.lastWorkingDate = lastWorkingDate;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public Employee getReportingTo() {
		return reportingTo;
	}

	public void setReportingTo(Employee reportingTo) {
		this.reportingTo = reportingTo;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getIsid() {
		return isid;
	}

	public void setIsid(String isid) {
		this.isid = isid;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getVoip() {
		return voip;
	}

	public void setVoip(String voip) {
		this.voip = voip;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public boolean isEmploymentStatus() {
		return employmentStatus;
	}

	public void setEmploymentStatus(boolean employmentStatus) {
		this.employmentStatus = employmentStatus;
	}

	@Override
	public String toString() {
		return "Employee [id=" + id + ", name=" + name + ", employeeId=" + employeeId + ", isid=" + isid + ", email="
				+ email + ", employmentType=" + employmentType + ", lastWorkingDate=" + lastWorkingDate + ", createdBy="
				+ createdBy + ", created=" + created + ", lastUpdatedBy=" + lastUpdatedBy + ", lastUpdated="
				+ lastUpdated + ", isActive=" + isActive + "]";
	}




	/*
	 * @Override public String toString() { return "Employee [id=" + id + ", name="
	 * + name + ", employeeId=" + employeeId + ", isid=" + isid + ", email=" + email
	 * + ", createdBy=" + createdBy + ", created=" + created + ", lastUpdatedBy=" +
	 * lastUpdatedBy + ", lastUpdated=" + lastUpdated + ", isActive=" + isActive +
	 * "]"; }
	 */







}
