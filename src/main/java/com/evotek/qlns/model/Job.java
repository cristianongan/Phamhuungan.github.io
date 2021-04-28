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
@Table(name = "job")
public class Job implements Serializable {
	private static final long serialVersionUID = 1L;
	@Column(name = "create_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;
	@Column(name = "description")
	private String description;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "job_id")
	private Long jobId;
	@Column(name = "job_title")
	private String jobTitle;
	@Column(name = "modified_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifiedDate;
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "job")
	@LazyCollection(LazyCollectionOption.EXTRA)
	private Set<Staff> staffs;
	@Column(name = "user_id")
	private Long userId;
	@Column(name = "user_name")
	private String userName;

	public Job() {
	}

	public Job(Long jobId) {
		this.jobId = jobId;
	}

	public Job(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof Job)) {
			return false;
		}
		Job other = (Job) object;
		if ((this.jobId == null && other.jobId != null) || (this.jobId != null && !this.jobId.equals(other.jobId))) {
			return false;
		}
		return true;
	}

	public Date getCreateDate() {
		return this.createDate;
	}

	public String getDescription() {
		return this.description;
	}

	public Long getJobId() {
		return this.jobId;
	}

	public String getJobTitle() {
		return this.jobTitle;
	}

	public Date getModifiedDate() {
		return this.modifiedDate;
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
		hash += (this.jobId != null ? this.jobId.hashCode() : 0);
		return hash;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
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
		return "com.evotek.qlns.model.Job[ jobId=" + this.jobId + " ]";
	}

}
