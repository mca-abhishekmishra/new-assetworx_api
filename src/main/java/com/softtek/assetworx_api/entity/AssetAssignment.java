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
@Table(name = "asset_assignment")
@EntityListeners(AuditingEntityListener.class)
public class AssetAssignment {
	
	@Id
	@GenericGenerator(name="uuid",strategy="uuid2")
	@GeneratedValue(generator="uuid")  
	@Column(name = "id")
	private String id;
	
	@NotNull(message="Valid asset id required")
	@JsonIgnoreProperties({"createdBy","created","lastUpdated","lastUpdatedBy","description","active", "assetAssignment"})
	@ManyToOne
	@JoinColumn(name = "asset_id",nullable=false)
	private Asset asset;
	
	@NotNull(message="Valid cost center required")
	@JsonIgnoreProperties({"createdBy","created","lastUpdated","lastUpdatedBy","description","active"})
	@ManyToOne
	@JoinColumn(name = "cost_center_id",nullable=false)
	private CostCenter costCenter;
	
	@NotNull(message="Asset Assignment Type is required")
	@ManyToOne
	@JoinColumn(name="asset_assignment_type_id",nullable=false)
	@JsonIgnoreProperties({"createdBy","created","lastUpdated","lastUpdatedBy","description","active"})
	private AssetAssignmentType assetAssignmentType;
	
	@JsonIgnoreProperties({"createdBy","created","lastUpdated","lastUpdatedBy","description","active"})
	@ManyToOne
	@JoinColumn(name = "employee_id")
	private Employee employee;
	
	@JsonIgnoreProperties({"createdBy","created","lastUpdated","lastUpdatedBy","description","active"})
	@ManyToOne
	@JoinColumn(name = "department_id")
	private Department department;
	
	@NotNull(message="Valid project wbs id required.")
	@JsonIgnoreProperties({"createdBy","created","lastUpdated","lastUpdatedBy","description","active"})
	@ManyToOne
	@JoinColumn(name = "projectWbsId_id",nullable=false)
	private ProjectWbsId projectWbsId;
	
	@JsonIgnoreProperties({"createdBy","created","lastUpdated","lastUpdatedBy","description","active"})
	@ManyToOne
	@JoinColumn(name = "area_id")
	private Area area;
	
	@JsonIgnoreProperties({"createdBy","created","lastUpdated","lastUpdatedBy","description","active"})
	@ManyToOne
	@JoinColumn(name = "project_id")
	private Project project;
	
	/*
	 * @Column(name = "delivery_date",nullable=false)
	 * 
	 * @JsonFormat(pattern="yyyy-MM-dd")
	 * 
	 * @Temporal(TemporalType.TIMESTAMP) private Date deliveryDate;
	 * 
	 * @Column(name = "return_date",nullable=false)
	 * 
	 * @JsonFormat(pattern="yyyy-MM-dd")
	 * 
	 * @Temporal(TemporalType.TIMESTAMP) private Date returnDate;
	 */
	
	@Column(name = "location")
	private String location;
	
	@Column(name = "country")
	private String country;
	
	@JoinColumn(name = "asset_unassigned_by")
	private String assetUnassignedBy;
	
	@JoinColumn(name = "asset_assigned_by")
	private String assetAssignedBy;
	
	@NotNull(message="Assignment date  is required")
	@Column(name = "assigment_date")
    @JsonFormat(pattern="yyyy-MM-dd")
	@Temporal(TemporalType.TIMESTAMP)
	private Date assignmentDate;
	
	@Column(name = "unassigment_date")
    @JsonFormat(pattern="yyyy-MM-dd")
	@Temporal(TemporalType.TIMESTAMP)
	private Date unassignmentDate;
	
	@NotBlank(message="Please add additional/configuration detials, else mention NA for no details.")
	@Column(name="additionalDetails",columnDefinition = "varchar(1000)")
	private String additionalDetails;
	
	@Column(name="informTo",columnDefinition = "varchar(1000)")
	private String informTo;
	
	@Column(name="description",columnDefinition = "varchar(1000)")
	private String description;
	
	@Column(name="comment",columnDefinition = "varchar(1000)")
	private String comment;
	
	@Column(name="hidden_comment",columnDefinition = "varchar(1000)")
	private String hiddenComment;
	
	
	@CreatedBy
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

	public Asset getAsset() {
		return asset;
	}

	public void setAsset(Asset asset) {
		this.asset = asset;
	}

	public CostCenter getCostCenter() {
		return costCenter;
	}

	public void setCostCenter(CostCenter costCenter) {
		this.costCenter = costCenter;
	}

	public AssetAssignmentType getAssetAssignmentType() {
		return assetAssignmentType;
	}

	public void setAssetAssignmentType(AssetAssignmentType assetAssignmentType) {
		this.assetAssignmentType = assetAssignmentType;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getAssetUnassignedBy() {
		return assetUnassignedBy;
	}

	public void setAssetUnassignedBy(String assetUnassignedBy) {
		this.assetUnassignedBy = assetUnassignedBy;
	}

	public String getAssetAssignedBy() {
		return assetAssignedBy;
	}

	public void setAssetAssignedBy(String assetAssignedBy) {
		this.assetAssignedBy = assetAssignedBy;
	}

	public Date getAssignmentDate() {
		return assignmentDate;
	}

	public void setAssignmentDate(Date assignmentDate) {
		this.assignmentDate = assignmentDate;
	}

	public Date getUnassignmentDate() {
		return unassignmentDate;
	}

	public void setUnassignmentDate(Date unassignmentDate) {
		this.unassignmentDate = unassignmentDate;
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

	public String getInformTo() {
		return informTo;
	}

	public void setInformTo(String informTo) {
		this.informTo = informTo;
	}

	public ProjectWbsId getProjectWbsId() {
		return projectWbsId;
	}

	public void setProjectWbsId(ProjectWbsId projectWbsId) {
		this.projectWbsId = projectWbsId;
	}

	
	
	
	
	
    
	
	
    

}
