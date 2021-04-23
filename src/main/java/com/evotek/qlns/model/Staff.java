/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evotek.qlns.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author My PC
 */
@Entity
@Table(name = "staff")
public class Staff implements Serializable, Comparable<Staff> {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "staff_id", unique = true, nullable = false)
    private Long staffId;
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
    @Column(name = "staff_name")
    private String staffName;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "dept_id")
    private Department department;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "job_id")
    private Job job;
    @Column(name = "work_date")
    @Temporal(TemporalType.DATE)
    private Date workDate;
    @Column(name = "date_of_birth")
    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;
    @Column(name = "permanent_residence")
    private String permanentResidence;
    @Column(name = "current_residence")
    private String currentResidence;
    @Column(name = "status")
    private Long status;
    @Lob
    @Column(name = "note")
    private String note;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "contract_type_id")
    private ContractType contractType;
    @Column(name = "contract_from_date")
    @Temporal(TemporalType.DATE)
    private Date contractFromDate;
    @Column(name = "contract_to_date")
    @Temporal(TemporalType.DATE)
    private Date contractToDate;
    @Column(name = "contract_number")
    private String contractNumber;
    @Column(name = "tax_code")
    private String taxCode;
    @Column(name = "salary_basic")
    private Long salaryBasic;
    @Column(name = "insurance_paid_date")
    @Temporal(TemporalType.DATE)
    private Date insurancePaidDate;
    @Column(name = "insurance_book_number")
    private String insuranceBookNumber;
    @Column(name = "paid_place")
    private String paidPlace;
    @Column(name = "levels")
    private String levels;
    @Column(name = "majors")
    private String majors;
    @Column(name = "college")
    private String college;
    @Column(name = "identity_card")
    private String identityCard;
    @Column(name = "grant_date")
    @Temporal(TemporalType.DATE)
    private Date grantDate;
    @Column(name = "grant_place")
    private String grantPlace;
    @Column(name = "mobile")
    private String mobile;
    @Column(name = "home_phone")
    private String homePhone;
    @Column(name = "email")
    private String email;
    @Column(name = "gender")
    private String gender;
    @Column(name = "married")
    private String married;
    @Column(name = "child_number")
    private Long childNumber;

    public Staff() {
    }

    public Staff(Long staffId) {
        this.staffId = staffId;
    }

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public Department getDepartment() {
        if(department == null){
            return new Department();
        }
        
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Job getJob() {
        if(job == null){
            return new Job();
        }
        
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public Date getWorkDate() {
        return workDate;
    }

    public void setWorkDate(Date workDate) {
        this.workDate = workDate;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPermanentResidence() {
        return permanentResidence;
    }

    public void setPermanentResidence(String permanentResidence) {
        this.permanentResidence = permanentResidence;
    }

    public String getCurrentResidence() {
        return currentResidence;
    }

    public void setCurrentResidence(String currentResidence) {
        this.currentResidence = currentResidence;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }
    
    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public ContractType getContractType() {
        if(contractType == null){
            return new ContractType();
        }
        
        return contractType;
    }

    public void setContractType(ContractType contractType) {
        this.contractType = contractType;
    }

    public Date getContractFromDate() {
        return contractFromDate;
    }

    public void setContractFromDate(Date contractFromDate) {
        this.contractFromDate = contractFromDate;
    }

    public Date getContractToDate() {
        return contractToDate;
    }

    public void setContractToDate(Date contractToDate) {
        this.contractToDate = contractToDate;
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public Long getSalaryBasic() {
        return salaryBasic;
    }

    public void setSalaryBasic(Long salaryBasic) {
        this.salaryBasic = salaryBasic;
    }

    public Date getInsurancePaidDate() {
        return insurancePaidDate;
    }

    public void setInsurancePaidDate(Date insurancePaidDate) {
        this.insurancePaidDate = insurancePaidDate;
    }

    public String getInsuranceBookNumber() {
        return insuranceBookNumber;
    }

    public void setInsuranceBookNumber(String insuranceBookNumber) {
        this.insuranceBookNumber = insuranceBookNumber;
    }

    public String getPaidPlace() {
        return paidPlace;
    }

    public void setPaidPlace(String paidPlace) {
        this.paidPlace = paidPlace;
    }

    public String getLevels() {
        return levels;
    }

    public void setLevels(String levels) {
        this.levels = levels;
    }

    public String getMajors() {
        return majors;
    }

    public void setMajors(String majors) {
        this.majors = majors;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getIdentityCard() {
        return identityCard;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
    }

    public Date getGrantDate() {
        return grantDate;
    }

    public void setGrantDate(Date grantDate) {
        this.grantDate = grantDate;
    }

    public String getGrantPlace() {
        return grantPlace;
    }

    public void setGrantPlace(String grantPlace) {
        this.grantPlace = grantPlace;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getHomePhone() {
        return homePhone;
    }

    public void setHomePhone(String homePhone) {
        this.homePhone = homePhone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMarried() {
        return married;
    }

    public void setMarried(String married) {
        this.married = married;
    }

    public Long getChildNumber() {
        return childNumber;
    }

    public void setChildNumber(Long childNumber) {
        this.childNumber = childNumber;
    }

    public int compareTo(Staff o) {
        if (o == null) {
            return -1;
        }

        if (o.staffId == null) {
            return 1;
        }

        return staffId.compareTo(o.staffId);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (staffId != null ? staffId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        final Staff other = (Staff) obj;

        if (this.staffId != other.staffId
                && (this.staffId == null
                || !this.staffId.equals(other.staffId))) {
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return "com.evotek.qlns.model.Staff[ staffId=" + staffId + " ]";
    }

}
