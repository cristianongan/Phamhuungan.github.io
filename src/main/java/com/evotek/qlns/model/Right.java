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
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "right_")
public class Right implements Serializable {

	private Long categoryId;
	private Date createDate;
	private String description;
	private Set<Group> groups = new HashSet<Group>(0);
	private Date modifiedDate;
	private Long rightId;
	private String rightName;
	private Long rightType;
	private Long status;
	private Long userId;
	private String userName;

	public Right() {
	}

	public Right(Long rightId) {
		this.rightId = rightId;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (getClass() != obj.getClass()) {
			return false;
		}

		final Right other = (Right) obj;

		if (this.rightId != other.rightId && (this.rightId == null || !this.rightId.equals(other.rightId))) {
			return false;
		}

		return true;
	}

	@Column(name = "category_id", precision = 22, scale = 0)
	public Long getCategoryId() {
		return this.categoryId;
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

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "rights")
	public Set<Group> getGroups() {
		return this.groups;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modified_date", length = 7)
	public Date getModifiedDate() {
		return this.modifiedDate;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "right_id", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getRightId() {
		return this.rightId;
	}

	@Column(name = "right_name", length = 200)
	public String getRightName() {
		return this.rightName;
	}

	@Column(name = "right_type", precision = 22, scale = 0)
	public Long getRightType() {
		return this.rightType;
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

	@Override
	public int hashCode() {
		int hash = 5;
		return hash;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
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

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public void setRightId(Long rightId) {
		this.rightId = rightId;
	}

	public void setRightName(String rightName) {
		this.rightName = rightName;
	}

	public void setRightType(Long rightType) {
		this.rightType = rightType;
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
}
