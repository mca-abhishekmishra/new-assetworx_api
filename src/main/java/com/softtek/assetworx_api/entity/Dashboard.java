package com.softtek.assetworx_api.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="dashboard")
@EntityListeners(AuditingEntityListener.class)
public class Dashboard {

	@Id
	@GenericGenerator(name = "uuid",strategy = "uuid2")
	@GeneratedValue(generator = "uuid")
	private String id;

	@NotBlank(message = "Dashboard name is required")
	private String name;

	@Column(name="description",columnDefinition = "varchar(1000)")
	private String description;
	
	@CreatedBy
	private String createdBy; 

	@CreatedDate
	private Date created;

	@LastModifiedBy
	private String lastUpdatedBy;

	@LastModifiedDate
	private Date lastUpdated;
	
	
	//@ManyToMany(mappedBy = "sharedDashBoard" , fetch = FetchType.LAZY)
		//@JsonIgnore
	//	private Set<Employee> sharedToEmployees;
	
	@ManyToMany
	@JoinTable(name="dashboard_report",
			joinColumns=@JoinColumn(name="dashboard_id"),
			inverseJoinColumns=@JoinColumn(name="report_id"))
	List<Report> reportList;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JsonIgnore
	@JoinTable(name = "user_shared_dashboards",  joinColumns  = @JoinColumn(name = "dashboard_id"), inverseJoinColumns  = @JoinColumn(name = "employee_id"))
	private Set<AssetworxUser> users;

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

	public List<Report> getReportList() {
		if(this.reportList == null) {
			this.reportList = new ArrayList<Report>();
		}
		return reportList;
	}

	public void setReportList(List<Report> reportList) {
		this.reportList = reportList;
	}
	
	public void addReport(Report report) {
		if(this.reportList == null) {
			this.reportList = new ArrayList<Report>();
		}
		this.reportList.add(report);
	}
	
	

	public Set<AssetworxUser> getUsers() {
		
		if (this.users == null) {
			return new HashSet<AssetworxUser>();
		}
		return users;

	}

	public void setUsers(Set<AssetworxUser> users) {
		
		if (this.users == null) {
			this.users = new HashSet<AssetworxUser>();
		}
		this.users = users;
	}

	@Override
	public String toString() {
		return "Dashboard [id=" + id + ", name=" + name + ", description=" + description + ", createdBy=" + createdBy
				+ ", created=" + created + ", lastUpdatedBy=" + lastUpdatedBy + ", lastUpdated=" + lastUpdated + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Dashboard other = (Dashboard) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}