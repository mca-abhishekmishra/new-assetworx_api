package com.softtek.assetworx_api.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "user_authority")
public class UserAuthority implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@Column(name = "authority_id")
	private String authorityId;
	
	@Column(name = "authority_name" , unique = true , nullable = false)
	private String name;
	
	@Column(columnDefinition = "varchar(1000)")
	private String description;
	
	private boolean isActive = true;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "user_authority_privileges_mapping", joinColumns = {
			@JoinColumn(name = "authority_id", referencedColumnName = "authority_id") }, inverseJoinColumns = {
					@JoinColumn(name = "privilege_id", referencedColumnName = "privilege_id") })
	private Set<UserAuthorityPrivilege> privileges = new HashSet<>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	

	public UserAuthority(String name, Set<UserAuthorityPrivilege> privileges) {
		super();
		this.name = name;
		this.privileges = privileges;
	}

	

	@Override
	public String toString() {
		return "UserAuthority [authorityId=" + authorityId + ", name=" + name + ", privileges=" + privileges + "]";
	}

	public Set<UserAuthorityPrivilege> getPrivileges() {
		if(this.privileges == null) {
			return new HashSet<UserAuthorityPrivilege>();
		}
		return privileges;
	}

	public void setPrivileges(Set<UserAuthorityPrivilege> privileges) {
		if(this.privileges == null) {
			this.privileges = new HashSet<UserAuthorityPrivilege>();
		}
		this.privileges = privileges;
	}

	public UserAuthority(String name) {
		this.name = name;
	}

	public UserAuthority() {
		super();
	}

	public String getAuthorityId() {
		return authorityId;
	}

	public void setAuthorityId(String authorityId) {
		this.authorityId = authorityId;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((authorityId == null) ? 0 : authorityId.hashCode());
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
		UserAuthority other = (UserAuthority) obj;
		if (authorityId == null) {
			if (other.authorityId != null)
				return false;
		} else if (!authorityId.equals(other.authorityId))
			return false;
		return true;
	}
	
	
	
}
