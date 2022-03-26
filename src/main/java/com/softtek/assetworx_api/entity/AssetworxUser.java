package com.softtek.assetworx_api.entity;

import java.io.Serializable;
import java.util.HashSet;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "user")
@EntityListeners(AuditingEntityListener.class)
public class AssetworxUser implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@Column(name = "user_id")
	private String userId;

	@Column(name = "user_name", nullable = false, unique = true)
	private String userName;

	@Column(name = "full_name", nullable = false)
	private String fullName;

	@Column(name = "user_email", nullable = false, unique = true)
	private String email;

	private boolean isActive;
	
	private String password;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "user_authority_mapping", joinColumns = {
			@JoinColumn(name = "user_id", referencedColumnName = "user_id") }, inverseJoinColumns = {
					@JoinColumn(name = "authority_id", referencedColumnName = "authority_id") })
	private Set<UserAuthority> authorties = new HashSet<>();

	@ManyToMany(fetch = FetchType.LAZY)
	@JsonIgnore
	@JoinTable(name = "user_dashboards", inverseJoinColumns = @JoinColumn(name = "dashboard_id"), joinColumns = @JoinColumn(name = "employee_id"))
	private Set<Dashboard> userDashBoards;

	@ManyToMany(fetch = FetchType.LAZY)
	@JsonIgnore
	@JoinTable(name = "user_shared_dashboards", inverseJoinColumns = @JoinColumn(name = "dashboard_id"), joinColumns = @JoinColumn(name = "employee_id"))
	private Set<Dashboard> sharedDashBoards;

	@JsonIgnore
	@OneToOne
	private Dashboard defaultDashBoard;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JsonIgnore
	@JoinTable(name = "user_reports", inverseJoinColumns = @JoinColumn(name = "report_id"), 
	joinColumns = @JoinColumn(name = "employee_id"))
	private Set<Report> userReports;

	@ManyToMany(fetch = FetchType.LAZY)
	@JsonIgnore
	@JoinTable(name = "user_shared_reports", 
	inverseJoinColumns = @JoinColumn(name = "report_id"),
	joinColumns = @JoinColumn(name = "employee_id"))
	private Set<Report> sharedReports;

	public AssetworxUser(String userId) {
		super();
		this.userId = userId;
	}

	public AssetworxUser() {
		super();
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<UserAuthority> getAuthorties() {
		if (this.authorties == null) {
			return new HashSet<UserAuthority>();
		}
		return authorties;
	}

	public void setAuthorties(Set<UserAuthority> authorties) {
		if (this.authorties == null) {
			this.authorties = new HashSet<UserAuthority>();
		}
		this.authorties = authorties;
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", userName=" + userName + ", fullName=" + fullName + ", email=" + email
				+ ", isActive=" + isActive + ", authorties=" + authorties + "]";
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public Set<Dashboard> getUserDashBoards() {
		return userDashBoards;
	}

	public void setUserDashBoards(Set<Dashboard> userDashBoards) {
		this.userDashBoards = userDashBoards;
	}

	public Set<Dashboard> getSharedDashBoards() {
		return sharedDashBoards;
	}

	public void setSharedDashBoards(Set<Dashboard> sharedDashBoards) {
		this.sharedDashBoards = sharedDashBoards;
	}

	public Dashboard getDefaultDashBoard() {
		return defaultDashBoard;
	}

	public void setDefaultDashBoard(Dashboard defaultDashBoard) {
		this.defaultDashBoard = defaultDashBoard;
	}

	public Set<Report> getUserReports() {
		return userReports;
	}

	public void setUserReports(Set<Report> userReports) {
		this.userReports = userReports;
	}

	public Set<Report> getSharedReports() {
		return sharedReports;
	}

	public void setSharedReports(Set<Report> sharedReports) {
		this.sharedReports = sharedReports;
	}

}
