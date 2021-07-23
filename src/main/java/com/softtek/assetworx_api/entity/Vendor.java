package com.softtek.assetworx_api.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name="vendor")
@EntityListeners(AuditingEntityListener.class)
public class Vendor {

	@Id
	@GenericGenerator(name = "uuid",strategy = "uuid2")
	@GeneratedValue(generator = "uuid")
	private String id;

	@NotBlank(message="Vendor name is required")
	@Column(nullable=false)
	private String name;

	@NotBlank(message="Vendor id is required")
	@Column(nullable=false, unique=true)
	private String vendorId;
	
	@NotBlank(message="Vendor address is required")
	@Column(nullable=false)
	private String vendorAddress;

	@NotBlank(message="Email is required")
	@Column(nullable=false)
	private String email;

	@NotNull(message="Mobile number is required")
	@Column(nullable=false)
	private String mobileNo;
	
	private String district;
	
	private String state;
	
	private String country;
	
	private String pincode;

	private String primaryContactName;

	private String primaryContactEmail;

	private String primaryContactMobileNo;

	private String secondaryContactName;

	private String secondaryContactEmail;

	private String secondaryContactMobileNo;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
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

	public String getPrimaryContactName() {
		return primaryContactName;
	}

	public void setPrimaryContactName(String primaryContactName) {
		this.primaryContactName = primaryContactName;
	}

	public String getPrimaryContactEmail() {
		return primaryContactEmail;
	}

	public void setPrimaryContactEmail(String primaryContactEmail) {
		this.primaryContactEmail = primaryContactEmail;
	}

	public String getPrimaryContactMobileNo() {
		return primaryContactMobileNo;
	}

	public void setPrimaryContactMobileNo(String primaryContactMobileNo) {
		this.primaryContactMobileNo = primaryContactMobileNo;
	}

	public String getSecondaryContactName() {
		return secondaryContactName;
	}

	public void setSecondaryContactName(String secondaryContactName) {
		this.secondaryContactName = secondaryContactName;
	}

	public String getSecondaryContactEmail() {
		return secondaryContactEmail;
	}

	public void setSecondaryContactEmail(String secondaryContactEmail) {
		this.secondaryContactEmail = secondaryContactEmail;
	}

	public String getSecondaryContactMobileNo() {
		return secondaryContactMobileNo;
	}

	public void setSecondaryContactMobileNo(String secondaryContactMobileNo) {
		this.secondaryContactMobileNo = secondaryContactMobileNo;
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

	public String getVendorAddress() {
		return vendorAddress;
	}

	public void setVendorAddress(String vendorAddress) {
		this.vendorAddress = vendorAddress;
	}
	

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	@Override
	public String toString() {
		return "Vendor [id=" + id + ", name=" + name + ", vendorId=" + vendorId + ", email=" + email + ", mobileNo="
				+ mobileNo + ", primaryContactName=" + primaryContactName + ", primaryContactEmail="
				+ primaryContactEmail + ", primaryContactMobileNo=" + primaryContactMobileNo + ", secondaryContactName="
				+ secondaryContactName + ", secondaryContactEmail=" + secondaryContactEmail
				+ ", secondaryContactMobileNo=" + secondaryContactMobileNo + ", createdBy=" + createdBy + ", created="
				+ created + ", lastUpdatedBy=" + lastUpdatedBy + ", lastUpdated=" + lastUpdated + ", isActive="
				+ isActive + "]";
	}
}
