package com.softtek.assetworx_api.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name="projectWbsId")
@EntityListeners(AuditingEntityListener.class)
public class ProjectWbsId {

	@Id
	@GenericGenerator(name = "uuid",strategy = "uuid2")
	@GeneratedValue(generator = "uuid")
	private String id;

	/*
	 * @NotBlank(message="Name is required")
	 * 
	 * @Column(nullable=false, unique=true) private String name;
	 */
	
	@NotBlank(message="Project WBS Id is required")
	@Column(nullable=false, unique=true)
	private String wbsId;
	
	@Column(columnDefinition = "varchar(1000)")
	private String description;
	
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

	/*
	 * public String getName() { return name; }
	 * 
	 * public void setName(String name) { this.name = name; }
	 */

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public String getWbsId() {
		return wbsId;
	}

	public void setWbsId(String wbsId) {
		this.wbsId = wbsId;
	}

}
