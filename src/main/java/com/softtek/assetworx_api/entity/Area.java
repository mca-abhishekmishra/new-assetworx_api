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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="area")
@EntityListeners(AuditingEntityListener.class)
public class Area {

	@Id
	@GenericGenerator(name = "uuid",strategy = "uuid2")
	@GeneratedValue(generator = "uuid")
	private String id;

	@NotBlank(message="Area name is required")
	@Column(nullable=false)
	private String name;
	
	@NotNull(message="Floor is required.")
	@ManyToOne
	@JoinColumn(name="floor_id", nullable=false)
	@JsonIgnoreProperties({"createdBy","created","lastUpdated","lastUpdatedBy","description","active"})
	private Floor floor;
	
	@NotNull(message="Area Type is required.")
	@ManyToOne
	@JoinColumn(name="area_type_id", nullable=false)
	@JsonIgnoreProperties({"createdBy","created","lastUpdated","lastUpdatedBy","description","active"})
	private AreaType areaType;
	
	@JsonIgnoreProperties({"createdBy","created","lastUpdated","lastUpdatedBy","description","active", "parentArea", "floor", "areaType"})
	@ManyToOne
	@JoinColumn(name="parent_area_id")
	private Area parentArea;
	
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

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

	public Floor getFloor() {
		return floor;
	}

	public void setFloor(Floor floor) {
		this.floor = floor;
	}

	public AreaType getAreaType() {
		return areaType;
	}

	public void setAreaType(AreaType areaType) {
		this.areaType = areaType;
	}

	public Area getParentArea() {
		return parentArea;
	}

	public void setParentArea(Area parentArea) {
		this.parentArea = parentArea;
	}

	@Override
	public String toString() {
		return "Area [id=" + id + ", name=" + name + ", floor=" + floor + ", description=" + description
				+ ", createdBy=" + createdBy + ", created=" + created + ", lastUpdatedBy=" + lastUpdatedBy
				+ ", lastUpdated=" + lastUpdated + ", isActive=" + isActive + "]";
	}

    
    

}
