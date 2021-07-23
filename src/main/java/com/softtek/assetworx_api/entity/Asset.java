package com.softtek.assetworx_api.entity;



import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "asset")
@EntityListeners(AuditingEntityListener.class)
public class Asset {
	
	@Id
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@GeneratedValue(generator = "uuid")
	private String id;
	
	@NotNull(message="Invoice is required")
	@ManyToOne
	@JoinColumn(name="invoice_id", nullable=false)
	@JsonIgnoreProperties({"createdBy","created","lastUpdated","lastUpdatedBy","description","active","poNo",
		"invoiceDate","vendor","invoiceAmount","otherAmount","assetQuantity","invoiceStatus","approvedBy",
		"approvedDate","comment","hiddenComment"})
	private Invoice invoice;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="contract_id")
	@JsonIgnoreProperties({"createdBy","created","lastUpdated","lastUpdatedBy","description","active","contractStatus",
		"costCenter","vendor","category","subCategory","amcStartDate","amcEndDate","amcCost","chargeBack","approvedBy",
		"approvedDate","comment","hiddenComment"})
	private Contract contract;
	
	/*
	 * @OneToMany(mappedBy = "asset")
	 * 
	 * @JsonIgnoreProperties({"createdBy","created","lastUpdated","lastUpdatedBy",
	 * "description","active","asset"}) private List<AssetAssignment>
	 * assetAssignmentList;
	 */
	
	@JsonIgnoreProperties({"createdBy","created","lastUpdated","lastUpdatedBy","description","active","asset"})
	 @OneToOne
	 @JoinColumn(name="asset_assignment_id")
	 private AssetAssignment assetAssignment;
	
	
	@NotNull(message="Valid Asset type is required")
	@ManyToOne
	@JoinColumn(name="asset_type_id",nullable=false)
	@JsonIgnoreProperties({"createdBy","created","lastUpdated","lastUpdatedBy","description","active"})
	private AssetType assetType; 
	
	@NotNull(message="Valid Category is required")
	@ManyToOne
	@JoinColumn(name="category_id",nullable=false)
	@JsonIgnoreProperties({"createdBy","created","lastUpdated","lastUpdatedBy","description","active","assetType"})
	private Category category;

	@NotNull(message="Valid Sub category is required")
	@ManyToOne
	@JoinColumn(name="sub_category_id",nullable=false)
	@JsonIgnoreProperties({"createdBy","created","lastUpdated","lastUpdatedBy","description","active","category"})
	private SubCategory subCategory;
	
	@NotNull(message="Asset status is required")
	@ManyToOne
	@JoinColumn(name="asset_status_id",nullable=false)
	@JsonIgnoreProperties({"createdBy","created","lastUpdated","lastUpdatedBy","description","active"})
	private Status assetStatus;
	
	@Min(value=1,message="Valid gross value is required")
	@Column(name="gross_value",nullable=false)
	private double grossValue;
	
	@NotNull(message="Warranty start date  is required")
	@Column(name="warranty_start_date",nullable=false)
	@JsonFormat(pattern="yyyy-MM-dd")
	@Temporal(TemporalType.TIMESTAMP)
	private Date warrantyStartDate;

	@NotNull(message="Warranty/Expiry end date  is required")
	@Column(name="warranty_end_date",nullable=false)
	@JsonFormat(pattern="yyyy-MM-dd")
	@Temporal(TemporalType.TIMESTAMP)
	private Date warrantyEndDate;
	
	@NotNull(message="Valid Manufacturer is required")
	@ManyToOne
	@JoinColumn(name="manufacturer_id",nullable=false)
	@JsonIgnoreProperties({"createdBy","created","lastUpdated","lastUpdatedBy","description","active"})
	private Manufacturer manufacturer; 

	@ManyToOne
	@JoinColumn(name="asset_model_id")
	@JsonIgnoreProperties({"createdBy","created","lastUpdated","lastUpdatedBy","description","active"})
	private AssetModel assetModel; 

	@ManyToOne
	@JoinColumn(name="operating_system_id")
	@JsonIgnoreProperties({"createdBy","created","lastUpdated","lastUpdatedBy","description","active"})
	private OperatingSystem operatingSystem; 
	
	@ManyToOne
	@JoinColumn(name="license_type_id")
	@JsonIgnoreProperties({"createdBy","created","lastUpdated","lastUpdatedBy","description","active"})
	private LicenseType licenseType; 

	@NotBlank(message="Serial No./License Key is required")
	@Column(name="serial_no",nullable=false,unique=true)  
	private String serialNo;
	
	@NotBlank(message="TagId is required") 
	@Column(name="tag_id",nullable=false,unique=true)
	private String tagId;
	
	@JoinColumn(name = "asset_approved_by")
	private String assetApprovedBy;
	
	@Column(name = "approved_date")
    @JsonFormat(pattern="yyyy-MM-dd")
	@Temporal(TemporalType.TIMESTAMP)
	private Date approvedDate;
	
	@Column(name="additionalDetails",columnDefinition = "varchar(1000)")
	private String additionalDetails;
	
	@Column(name="description",columnDefinition = "varchar(1000)")
	private String description;
	
	@Column(name="comment",columnDefinition = "varchar(1000)")
	private String comment;
	
	@Column(name="hidden_comment",columnDefinition = "varchar(1000)")
	private String hiddenComment;
	
    private String createdBy;

    @CreatedDate
    private Date created;

    @LastModifiedBy
    private String lastUpdatedBy;

    @LastModifiedDate
    private Date lastUpdated;
    
	@Column(name="is_active")
	boolean isActive=true;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Invoice getInvoice() {
		return invoice;
	}

	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}

	public Contract getContract() {
		return contract;
	}

	public void setContract(Contract contract) {
		this.contract = contract;
	}

	public AssetType getAssetType() {
		return assetType;
	}

	public void setAssetType(AssetType assetType) {
		this.assetType = assetType;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public SubCategory getSubCategory() {
		return subCategory;
	}

	public void setSubCategory(SubCategory subCategory) {
		this.subCategory = subCategory;
	}

	public Status getAssetStatus() {
		return assetStatus;
	}

	public void setAssetStatus(Status assetStatus) {
		this.assetStatus = assetStatus;
	}

	public double getGrossValue() {
		return grossValue;
	}

	public void setGrossValue(double grossValue) {
		this.grossValue = grossValue;
	}

	public Date getWarrantyStartDate() {
		return warrantyStartDate;
	}

	public void setWarrantyStartDate(Date warrantyStartDate) {
		this.warrantyStartDate = warrantyStartDate;
	}

	public Date getWarrantyEndDate() {
		return warrantyEndDate;
	}

	public void setWarrantyEndDate(Date warrantyEndDate) {
		this.warrantyEndDate = warrantyEndDate;
	}

	public Manufacturer getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(Manufacturer manufacturer) {
		this.manufacturer = manufacturer;
	}

	public AssetModel getAssetModel() {
		return assetModel;
	}

	public void setAssetModel(AssetModel assetModel) {
		this.assetModel = assetModel;
	}

	public OperatingSystem getOperatingSystem() {
		return operatingSystem;
	}

	public void setOperatingSystem(OperatingSystem operatingSystem) {
		this.operatingSystem = operatingSystem;
	}

	public LicenseType getLicenseType() {
		return licenseType;
	}

	public void setLicenseType(LicenseType licenseType) {
		this.licenseType = licenseType;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public String getTagId() {
		return tagId;
	}

	public void setTagId(String tagId) {
		this.tagId = tagId;
	}

	public String getAssetApprovedBy() {
		return assetApprovedBy;
	}

	public void setAssetApprovedBy(String assetApprovedBy) {
		this.assetApprovedBy = assetApprovedBy;
	}

	public Date getApprovedDate() {
		return approvedDate;
	}

	public void setApprovedDate(Date approvedDate) {
		this.approvedDate = approvedDate;
	}

	public String getAdditionalDetails() {
		return additionalDetails;
	}

	public void setAdditionalDetails(String additionalDetails) {
		this.additionalDetails = additionalDetails;
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

	public String getHiddenComment() {
		return hiddenComment;
	}

	public void setHiddenComment(String hiddenComment) {
		this.hiddenComment = hiddenComment;
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

	/*
	 * public List<AssetAssignment> getAssetAssignmentList() { return
	 * assetAssignmentList; }
	 * 
	 * public void setAssetAssignmentList(List<AssetAssignment> assetAssignmentList)
	 * { this.assetAssignmentList = assetAssignmentList; }
	 */

	public AssetAssignment getAssetAssignment() {
		return assetAssignment;
	}

	public void setAssetAssignment(AssetAssignment assetAssignment) {
		this.assetAssignment = assetAssignment;
	}
	
	

	
	

}
