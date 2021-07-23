package com.softtek.assetworx_api.entity;

import java.util.Date;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "workstation_allocation")
@EntityListeners(AuditingEntityListener.class)
public class WorkstationAllocation {

	@Id
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@GeneratedValue(generator = "uuid")
	private String id;

	@JsonIgnoreProperties({ "createdBy", "created", "lastUpdated", 
		"lastUpdatedBy", "description", "active","workstationAllocations","workstation" })
	@ManyToOne
	@JoinColumn(name = "workstation_id")
	private Workstation workstation;

	private boolean isAllocated = true;

	@JsonIgnoreProperties({ "createdBy", "created", "lastUpdated", 
		"lastUpdatedBy", "description", "active","project","reportingTo" })
	@OneToOne
	@JoinColumn(name = "employee_id")
	private Employee employee;

	@CreatedBy
	private String createdBy;

	@CreatedDate
	private Date created;

	@LastModifiedBy
	private String lastUpdatedBy;

	@LastModifiedDate
	private Date lastUpdated;

	private boolean isActive = true;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Workstation getWorkstation() {
		return workstation;
	}

	public void setWorkstation(Workstation workstation) {
		this.workstation = workstation;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
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

	public boolean isAllocated() {
		return isAllocated;
	}

	public void setAllocated(boolean isAllocated) {
		this.isAllocated = isAllocated;
	}

	// for comparing it with other Objects
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WorkstationAllocation other = (WorkstationAllocation) obj;
		return Objects.equals(id, other.id);
	}

}
