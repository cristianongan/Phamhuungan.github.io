/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.evotek.qlns.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author My PC
 */
@Entity
@Table(name = "department")
public class Department implements Serializable {
	private static final long serialVersionUID = 1L;
	@Column(name = "create_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "dept_id")
	private Long deptId;
	@Column(name = "dept_name")
	private String deptName;
	@Column(name = "description")
	private String description;
	@Column(name = "icon")
	private String icon;
	@Column(name = "modified_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifiedDate;
	@Column(name = "ordinal")
	private Long ordinal;
	@Column(name = "parent_id")
	private Long parentId;
	@Column(name = "user_id")
	private Long userId;
	@Column(name = "user_name")
	private String userName;

	public Department() {
	}

	public Department(Long deptId) {
		this.deptId = deptId;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof Department)) {
			return false;
		}
		Department other = (Department) object;
		if ((this.deptId == null && other.deptId != null)
				|| (this.deptId != null && !this.deptId.equals(other.deptId))) {
			return false;
		}
		return true;
	}

	public Date getCreateDate() {
		return this.createDate;
	}

	public Long getDeptId() {
		return this.deptId;
	}

	public String getDeptName() {
		return this.deptName;
	}

	public String getDescription() {
		return this.description;
	}

	public String getIcon() {
		return this.icon;
	}

	public Date getModifiedDate() {
		return this.modifiedDate;
	}

	public Long getOrdinal() {
		return this.ordinal;
	}

	public Long getParentId() {
		return this.parentId;
	}

	public Long getUserId() {
		return this.userId;
	}

	public String getUserName() {
		return this.userName;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (this.deptId != null ? this.deptId.hashCode() : 0);
		return hash;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public void setOrdinal(Long ordinal) {
		this.ordinal = ordinal;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	public String toString() {
		return "com.evotek.qlns.model.Department[ deptId=" + this.deptId + " ]";
	}

}
