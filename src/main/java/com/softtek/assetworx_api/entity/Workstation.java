package com.softtek.assetworx_api.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="workstation")
@EntityListeners(AuditingEntityListener.class)
public class Workstation {
	
	@Id
	@GenericGenerator(name = "uuid",strategy = "uuid2")
	@GeneratedValue(generator = "uuid")
	private String id;
	
	@JsonIgnoreProperties({"createdBy","created","lastUpdated","lastUpdatedBy","description","active", "area"})
	@OneToOne
	@JoinColumn(name="area_id")
	private Area area;
	
    @CreatedBy
    private String createdBy;

    @CreatedDate
    private Date created;

    @LastModifiedBy
    private String lastUpdatedBy;

    @LastModifiedDate
    private Date lastUpdated;
    
    private boolean isActive = true;
    
    private boolean isOccupied = false;
    
    private boolean isShared = false;
    
    @OneToMany(mappedBy = "workstation")
    @Where(clause="is_allocated=1")
    private List<WorkstationAllocation> workstationAllocations;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
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

	public boolean isOccupied() {
		return isOccupied;
	}

	public void setOccupied(boolean isOccupied) {
		this.isOccupied = isOccupied;
	}

	public boolean isShared() {
		return isShared;
	}

	public void setShared(boolean isShared) {
		this.isShared = isShared;
	}

	public List<WorkstationAllocation> getWorkstationAllocations() {
		if(this.workstationAllocations==null) {
			this.workstationAllocations = new ArrayList<WorkstationAllocation>();
		}
		return this.workstationAllocations;//.stream().filter(w -> w.isAllocated()).collect(Collectors.toList());
	}

	public void setWorkstationAllocations(List<WorkstationAllocation> workstationAllocations) {
		if(this.workstationAllocations==null) {
			this.workstationAllocations = new ArrayList<WorkstationAllocation>();
		}
		this.workstationAllocations = workstationAllocations;
	}
	
	public void add(WorkstationAllocation workstationAllocation) {
		if(this.workstationAllocations==null) {
			this.workstationAllocations = new ArrayList<WorkstationAllocation>();
		}
		this.workstationAllocations.add(workstationAllocation);
	}
}
