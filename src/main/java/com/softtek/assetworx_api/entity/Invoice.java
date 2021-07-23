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
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "invoice")
@EntityListeners(AuditingEntityListener.class)
public class Invoice {

	@Id
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@GeneratedValue(generator = "uuid")
	private String id;

	@NotBlank(message = "Invoice number is required")
	@Column(nullable = false)
	private String invoiceNo;

	@NotBlank(message = "PO number is required")
	@Column(nullable = false)
	private String poNo;

	@NotNull(message = "Invoice date is required")
	@Column(name = "invoice_date", nullable = false)
	@JsonFormat(pattern = "yyyy-MM-dd")
	@Temporal(TemporalType.TIMESTAMP)
	private Date invoiceDate;

	@ManyToOne
	@NotNull(message = "Vendor is required")
	@JoinColumn(name = "vendor_id", nullable = false)
	@JsonIgnoreProperties({ "createdBy", "created", "lastUpdated", "lastUpdatedBy", "description", "active",
			"vendorAddress", "email", "mobileNo", "primaryContactName", "primaryContactEmail", "primaryContactMobileNo",
			"secondaryContactName", "secondaryContactEmail", "secondaryContactMobileNo" })
	private Vendor vendor;

	@Min(value = 1, message = "Invoice amount is required")
	@Column(name = "invoice_amount", nullable = false)
	private double invoiceAmount;

	@Column(name = "other_amount")
	private double otherAmount;

	@Min(value = 1, message = "Asset quantity is required")
	@Column(name = "quantity", nullable = false)
	private int assetQuantity;

	@ManyToOne
	@JoinColumn(name = "invoice_status_id", nullable = false)
	@JsonIgnoreProperties({ "createdBy", "created", "lastUpdated", "lastUpdatedBy", "description", "active" })
	private Status invoiceStatus;

	@JoinColumn(name = "approved_by")
	private String approvedBy;

	@Column(name = "approved_date")
	@JsonFormat(pattern = "yyyy-MM-dd")
	@Temporal(TemporalType.TIMESTAMP)
	private Date approvedDate;

	// if boolean is used in Place of Boolean change every value in db to 0 first
	// and them run the app
	@Column(name = "is_valid")
	private boolean isValid = false;

	@CreatedBy
	private String createdBy;

	@CreatedDate
	private Date created;

	@LastModifiedBy
	private String lastUpdatedBy;

	@LastModifiedDate
	private Date lastUpdated;

	@Column(name = "description", columnDefinition = "varchar(1000)")
	private String description;

	@Column(name = "comment", columnDefinition = "varchar(1000)")
	private String comment;

	@Column(name = "hidden_comment", columnDefinition = "varchar(1000)")
	private String hiddenComment;

	private boolean isActive = true;

	public Invoice() {
		super();
	}

	public Invoice(String id) {
		super();
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getPoNo() {
		return poNo;
	}

	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}

	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public Vendor getVendor() {
		return vendor;
	}

	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}

	public double getInvoiceAmount() {
		return invoiceAmount;
	}

	public void setInvoiceAmount(double invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}

	public double getOtherAmount() {
		return otherAmount;
	}

	public void setOtherAmount(double otherAmount) {
		this.otherAmount = otherAmount;
	}

	public int getAssetQuantity() {
		return assetQuantity;
	}

	public void setAssetQuantity(int assetQuantity) {
		this.assetQuantity = assetQuantity;
	}

	public Status getInvoiceStatus() {
		return invoiceStatus;
	}

	public void setInvoiceStatus(Status invoiceStatus) {
		this.invoiceStatus = invoiceStatus;
	}

	public String getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}

	public Date getApprovedDate() {
		return approvedDate;
	}

	public void setApprovedDate(Date approvedDate) {
		this.approvedDate = approvedDate;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public String getHiddenComment() {
		return hiddenComment;
	}

	public void setHiddenComment(String hiddenComment) {
		this.hiddenComment = hiddenComment;
	}

	public boolean isValid() {
		return isValid;
	}

	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}

	@Override
	public String toString() {
		return "Invoice [id=" + id + ", invoiceNo=" + invoiceNo + ", poNo=" + poNo + ", invoiceDate=" + invoiceDate
				+ ", vendor=" + vendor + ", invoiceAmount=" + invoiceAmount + ", otherAmount=" + otherAmount
				+ ", assetQuantity=" + assetQuantity + ", invoiceStatus=" + invoiceStatus + ", approvedBy=" + approvedBy
				+ ", approvedDate=" + approvedDate + ", isValid=" + isValid + ", createdBy=" + createdBy + ", created="
				+ created + ", lastUpdatedBy=" + lastUpdatedBy + ", lastUpdated=" + lastUpdated + ", description="
				+ description + ", comment=" + comment + ", hiddenComment=" + hiddenComment + ", isActive=" + isActive
				+ "]";
	}

}
