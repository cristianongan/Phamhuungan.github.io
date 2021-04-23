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
 * @author linhlh2
 */
@Entity
@Table(name = "salary_landmark")
public class SalaryLandmark implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "salary_landmark_id")
    private Long salaryLandmarkId;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "user_name")
    private String userName;
    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "modified_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDate;
    @Column(name = "staff_id")
    private Long staffId;
    @Column(name = "from_date")
    @Temporal(TemporalType.DATE)
    private Date fromDate;
    @Column(name = "to_date")
    @Temporal(TemporalType.DATE)
    private Date toDate;
    @Column(name = "salary")
    private Long salary;
    @Column(name = "description")
    private String description;

    public SalaryLandmark() {
    }

    public SalaryLandmark(Long salaryLandmarkId) {
        this.salaryLandmarkId = salaryLandmarkId;
    }

    public Long getSalaryLandmarkId() {
        return this.salaryLandmarkId;
    }

    public void setSalaryLandmarkId(Long salaryLandmarkId) {
        this.salaryLandmarkId = salaryLandmarkId;
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getModifiedDate() {
        return this.modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Long getStaffId() {
        return this.staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public Date getFromDate() {
        return this.fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return this.toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public Long getSalary() {
        return this.salary;
    }

    public void setSalary(Long salary) {
        this.salary = salary;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (this.salaryLandmarkId != null ? this.salaryLandmarkId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SalaryLandmark)) {
            return false;
        }
        SalaryLandmark other = (SalaryLandmark) object;
        if ((this.salaryLandmarkId == null 
                && other.salaryLandmarkId != null) 
                || (this.salaryLandmarkId != null 
                && !this.salaryLandmarkId.equals(other.salaryLandmarkId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.evotek.qlns.model.SalaryLandmark[ salaryLandmarkId=" + this.salaryLandmarkId + " ]";
    }
    
}
