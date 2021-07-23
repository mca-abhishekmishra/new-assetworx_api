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
@Table(name = "contract")
@EntityListeners(AuditingEntityListener.class)
public class Contract {
	
	@Id
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@GeneratedValue(generator = "uuid")
	private String id;
	
	@NotBlank(message="Contract number is required")
	@Column(nullable=false,unique = true)
	private String contractNo;
	
	@NotBlank(message="PO number is required")
	@Column(nullable=false)
	private String poNo;
	
	@Column(name = "contract_name")
	private String contractName;
	
	@ManyToOne
	@JoinColumn(name="contract_status_id",nullable=false)
	@JsonIgnoreProperties({"createdBy","created","lastUpdated","lastUpdatedBy","description","active"})
	private Status contractStatus;
	
	@NotNull(message = "Vendor is required")
	@ManyToOne
	@JoinColumn(name = "vendor_id", nullable = false)
	@JsonIgnoreProperties({"createdBy","created","lastUpdated","lastUpdatedBy","description","active","vendorAddress",
		"email","mobileNo","primaryContactName","primaryContactEmail","primaryContactMobileNo","secondaryContactName",
		"secondaryContactEmail","secondaryContactMobileNo"})
	private Vendor vendor;
	
	@NotNull(message = "Cost Center is required")
	@ManyToOne
	@JoinColumn(name = "cost_center_id", nullable = false)
	@JsonIgnoreProperties({"createdBy","created","lastUpdated","lastUpdatedBy","description","active"})
	private CostCenter costCenter;
	
	@NotNull(message="Contract type is required")
	@ManyToOne
	@JoinColumn(name="contract_type_id",nullable=false)
	@JsonIgnoreProperties({"createdBy","created","lastUpdated","lastUpdatedBy","description","active"})
	private ContractType contractType; 
	
	@NotNull(message="Category is required")
	@ManyToOne
	@JoinColumn(name="category_id")
	@JsonIgnoreProperties({"createdBy","created","lastUpdated","lastUpdatedBy","description","active","assetType"})
    private Category category;
	
	@NotNull(message="Sub Category is required")
	@ManyToOne
	@JoinColumn(name="sub_category_id")
	@JsonIgnoreProperties({"createdBy","created","lastUpdated","lastUpdatedBy","description","active","category"})
    private SubCategory subCategory;
	
	@NotNull(message = "AMC start date is required")
	@Column(name = "amc_start_date", nullable = false)
	@JsonFormat(pattern = "yyyy-MM-dd")
	@Temporal(TemporalType.TIMESTAMP)
	private Date amcStartDate;

	@NotNull(message = "AMC end date is required")
	@Column(name = "amc_end_date", nullable = false)
	@JsonFormat(pattern = "yyyy-MM-dd")
	@Temporal(TemporalType.TIMESTAMP)
	private Date amcEndDate;
	
	@Min(value = 1, message = "AMC cost is required")
	@Column(name = "amc_cost", nullable = false)
	private double amcCost;
	
	@NotNull(message = "Valid payment type is required.")
	@ManyToOne
	@JoinColumn(name = "payment_type_id", nullable = false)
	@JsonIgnoreProperties({"createdBy","created","lastUpdated","lastUpdatedBy","description","active"})
	private PaymentType paymentType;
	
	@JoinColumn(name = "approved_by")
	private String approvedBy;
	
	@Column(name = "approved_date")
	@JsonFormat(pattern="yyyy-MM-dd")
	@Temporal(TemporalType.TIMESTAMP)
	private Date approvedDate;

	@CreatedBy
	private String createdBy;

	@CreatedDate
	private Date created;

	@LastModifiedBy
	private String lastUpdatedBy;

	@LastModifiedDate
	private Date lastUpdated;
	
    @Column(name="description",columnDefinition = "varchar(1000)")
	private String description;
    
    @Column(name="comment",columnDefinition = "varchar(1000)")
	private String comment;
    
    @Column(name="hidden_comment",columnDefinition = "varchar(1000)")
   	private String hiddenComment;
    
    @Column(name="is_active")
	boolean isActive=true;
    
    public Contract() {
		super();
	}

	public Contract(String contractNo) {
		super();
		this.contractNo = contractNo;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContractNo() {
		return contractNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	public String getContractName() {
		return contractName;
	}

	public void setContractName(String contractName) {
		this.contractName = contractName;
	}

	public Status getContractStatus() {
		return contractStatus;
	}

	public void setContractStatus(Status contractStatus) {
		this.contractStatus = contractStatus;
	}

	public Vendor getVendor() {
		return vendor;
	}

	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}

	public CostCenter getCostCenter() {
		return costCenter;
	}

	public void setCostCenter(CostCenter costCenter) {
		this.costCenter = costCenter;
	}

	public Date getAmcStartDate() {
		return amcStartDate;
	}

	public void setAmcStartDate(Date amcStartDate) {
		this.amcStartDate = amcStartDate;
	}

	public Date getAmcEndDate() {
		return amcEndDate;
	}

	public void setAmcEndDate(Date amcEndDate) {
		this.amcEndDate = amcEndDate;
	}

	public double getAmcCost() {
		return amcCost;
	}

	public void setAmcCost(double amcCost) {
		this.amcCost = amcCost;
	}

	public PaymentType getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(PaymentType paymentType) {
		this.paymentType = paymentType;
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

	public String getHiddenComment() {
		return hiddenComment;
	}

	public void setHiddenComment(String hiddenComment) {
		this.hiddenComment = hiddenComment;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public String getPoNo() {
		return poNo;
	}

	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}

	public ContractType getContractType() {
		return contractType;
	}

	public void setContractType(ContractType contractType) {
		this.contractType = contractType;
	}

	
	
	
    
    
}
