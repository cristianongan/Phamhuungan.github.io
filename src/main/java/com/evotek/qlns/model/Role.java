package com.evotek.qlns.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name = "role")
public class Role implements Serializable {

	private Date createDate;
	private String description;
	private Set<Group> groups = new HashSet<Group>(0);
	private Long immune;
	private Date modifiedDate;
	private Long roleId;
	private String roleName;
	private Boolean shareable;
	private Long status;
	private Long userId;
	private String userName;
	private Set<User> users = new HashSet<User>(0);

	public Role() {
	}

	public Role(Long roleId) {
		this.roleId = roleId;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (getClass() != obj.getClass()) {
			return false;
		}

		final Role other = (Role) obj;

		if (this.roleId != other.roleId && (this.roleId == null || !this.roleId.equals(other.roleId))) {
			return false;
		}

		return true;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_date", length = 7)
	public Date getCreateDate() {
		return this.createDate;
	}

	@Column(name = "description", length = 1000)
	public String getDescription() {
		return this.description;
	}

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "role_group", joinColumns = {
			@JoinColumn(name = "role_id", nullable = false, updatable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "group_id", nullable = false, updatable = false) })
//    @LazyCollection(LazyCollectionOption.EXTRA)
	@Fetch(FetchMode.SELECT)
	public Set<Group> getGroups() {
		return this.groups;
	}

	@Column(name = "immune", precision = 22, scale = 0)
	public Long getImmune() {
		return this.immune;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modified_date", length = 7)
	public Date getModifiedDate() {
		return this.modifiedDate;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "role_id", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getRoleId() {
		return this.roleId;
	}

	@Column(name = "role_name", length = 200)
	public String getRoleName() {
		return this.roleName;
	}

	@Column(name = "shareable", precision = 1, scale = 0)
	public Boolean getShareable() {
		return this.shareable;
	}

	@Column(name = "status", precision = 22, scale = 0)
	public Long getStatus() {
		return this.status;
	}

	@Column(name = "user_id", precision = 22, scale = 0)
	public Long getUserId() {
		return this.userId;
	}

	@Column(name = "user_name", length = 200)
	public String getUserName() {
		return this.userName;
	}

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "roles")
	public Set<User> getUsers() {
		return this.users;
	}

	@Override
	public int hashCode() {
		int hash = 5;
		return hash;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setGroups(Set<Group> groups) {
		this.groups = groups;
	}

	public void setImmune(Long immune) {
		this.immune = immune;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public void setShareable(Boolean shareable) {
		this.shareable = shareable;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}
}
