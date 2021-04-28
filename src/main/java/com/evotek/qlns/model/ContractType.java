/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.evotek.qlns.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 *
 * @author My PC
 */
@Entity
@Table(name = "contract_type")

public class ContractType implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "contract_type_id")
	private Long contractTypeId;
	@Column(name = "contract_type_name")
	private String contractTypeName;
	@Column(name = "create_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;
	@Column(name = "description")
	private String description;
	@Column(name = "modified_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifiedDate;
	@Column(name = "month_duration")
	private Long monthDuration;
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "contractType")
	@LazyCollection(LazyCollectionOption.EXTRA)
	private Set<Staff> staffs;
	@Column(name = "user_id")
	private Long userId;
	@Column(name = "user_name")
	private String userName;

	public ContractType() {
	}

	public ContractType(Long contractTypeId) {
		this.contractTypeId = contractTypeId;
	}

	public ContractType(String contractTypeName) {
		this.contractTypeName = contractTypeName;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof ContractType)) {
			return false;
		}
		ContractType other = (ContractType) object;
		if ((this.contractTypeId == null && other.contractTypeId != null)
				|| (this.contractTypeId != null && !this.contractTypeId.equals(other.contractTypeId))) {
			return false;
		}
		return true;
	}

	public Long getContractTypeId() {
		return this.contractTypeId;
	}

	public String getContractTypeName() {
		return this.contractTypeName;
	}

	public Date getCreateDate() {
		return this.createDate;
	}

	public String getDescription() {
		return this.description;
	}

	public Date getModifiedDate() {
		return this.modifiedDate;
	}

	public Long getMonthDuration() {
		return this.monthDuration;
	}

	public Set<Staff> getStaffs() {
		return this.staffs;
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
		hash += (this.contractTypeId != null ? this.contractTypeId.hashCode() : 0);
		return hash;
	}

	public void setContractTypeId(Long contractTypeId) {
		this.contractTypeId = contractTypeId;
	}

	public void setContractTypeName(String contractTypeName) {
		this.contractTypeName = contractTypeName;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public void setMonthDuration(Long monthDuration) {
		this.monthDuration = monthDuration;
	}

	public void setStaffs(Set<Staff> staffs) {
		this.staffs = staffs;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	public String toString() {
		return "com.evotek.qlns.model.ContractType[ contractTypeId=" + this.contractTypeId + " ]";
	}

}
