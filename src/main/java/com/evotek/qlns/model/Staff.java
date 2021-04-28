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
	@Column(name = "child_number")
	private Long childNumber;
	@Column(name = "college")
	private String college;
	@Column(name = "contract_from_date")
	@Temporal(TemporalType.DATE)
	private Date contractFromDate;
	@Column(name = "contract_number")
	private String contractNumber;
	@Column(name = "contract_to_date")
	@Temporal(TemporalType.DATE)
	private Date contractToDate;
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "contract_type_id")
	private ContractType contractType;
	@Column(name = "create_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;
	@Column(name = "current_residence")
	private String currentResidence;
	@Column(name = "date_of_birth")
	@Temporal(TemporalType.DATE)
	private Date dateOfBirth;
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "dept_id")
	private Department department;
	@Column(name = "email")
	private String email;
	@Column(name = "gender")
	private String gender;
	@Column(name = "grant_date")
	@Temporal(TemporalType.DATE)
	private Date grantDate;
	@Column(name = "grant_place")
	private String grantPlace;
	@Column(name = "home_phone")
	private String homePhone;
	@Column(name = "identity_card")
	private String identityCard;
	@Column(name = "insurance_book_number")
	private String insuranceBookNumber;
	@Column(name = "insurance_paid_date")
	@Temporal(TemporalType.DATE)
	private Date insurancePaidDate;
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "job_id")
	private Job job;
	@Column(name = "levels")
	private String levels;
	@Column(name = "majors")
	private String majors;
	@Column(name = "married")
	private String married;
	@Column(name = "mobile")
	private String mobile;
	@Column(name = "modified_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifiedDate;
	@Lob
	@Column(name = "note")
	private String note;
	@Column(name = "paid_place")
	private String paidPlace;
	@Column(name = "permanent_residence")
	private String permanentResidence;
	@Column(name = "salary_basic")
	private Long salaryBasic;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "staff_id", unique = true, nullable = false)
	private Long staffId;
	@Column(name = "staff_name")
	private String staffName;
	@Column(name = "status")
	private Long status;
	@Column(name = "tax_code")
	private String taxCode;
	@Column(name = "user_id")
	private Long userId;
	@Column(name = "user_name")
	private String userName;
	@Column(name = "work_date")
	@Temporal(TemporalType.DATE)
	private Date workDate;

	public Staff() {
	}

	public Staff(Long staffId) {
		this.staffId = staffId;
	}

	@Override
	public int compareTo(Staff o) {
		if (o == null) {
			return -1;
		}

		if (o.staffId == null) {
			return 1;
		}

		return this.staffId.compareTo(o.staffId);
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

		if (this.staffId != other.staffId && (this.staffId == null || !this.staffId.equals(other.staffId))) {
			return false;
		}

		return true;
	}

	public Long getChildNumber() {
		return this.childNumber;
	}

	public String getCollege() {
		return this.college;
	}

	public Date getContractFromDate() {
		return this.contractFromDate;
	}

	public String getContractNumber() {
		return this.contractNumber;
	}

	public Date getContractToDate() {
		return this.contractToDate;
	}

	public ContractType getContractType() {
		if (this.contractType == null) {
			return new ContractType();
		}

		return this.contractType;
	}

	public Date getCreateDate() {
		return this.createDate;
	}

	public String getCurrentResidence() {
		return this.currentResidence;
	}

	public Date getDateOfBirth() {
		return this.dateOfBirth;
	}

	public Department getDepartment() {
		if (this.department == null) {
			return new Department();
		}

		return this.department;
	}

	public String getEmail() {
		return this.email;
	}

	public String getGender() {
		return this.gender;
	}

	public Date getGrantDate() {
		return this.grantDate;
	}

	public String getGrantPlace() {
		return this.grantPlace;
	}

	public String getHomePhone() {
		return this.homePhone;
	}

	public String getIdentityCard() {
		return this.identityCard;
	}

	public String getInsuranceBookNumber() {
		return this.insuranceBookNumber;
	}

	public Date getInsurancePaidDate() {
		return this.insurancePaidDate;
	}

	public Job getJob() {
		if (this.job == null) {
			return new Job();
		}

		return this.job;
	}

	public String getLevels() {
		return this.levels;
	}

	public String getMajors() {
		return this.majors;
	}

	public String getMarried() {
		return this.married;
	}

	public String getMobile() {
		return this.mobile;
	}

	public Date getModifiedDate() {
		return this.modifiedDate;
	}

	public String getNote() {
		return this.note;
	}

	public String getPaidPlace() {
		return this.paidPlace;
	}

	public String getPermanentResidence() {
		return this.permanentResidence;
	}

	public Long getSalaryBasic() {
		return this.salaryBasic;
	}

	public Long getStaffId() {
		return this.staffId;
	}

	public String getStaffName() {
		return this.staffName;
	}

	public Long getStatus() {
		return this.status;
	}

	public String getTaxCode() {
		return this.taxCode;
	}

	public Long getUserId() {
		return this.userId;
	}

	public String getUserName() {
		return this.userName;
	}

	public Date getWorkDate() {
		return this.workDate;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (this.staffId != null ? this.staffId.hashCode() : 0);
		return hash;
	}

	public void setChildNumber(Long childNumber) {
		this.childNumber = childNumber;
	}

	public void setCollege(String college) {
		this.college = college;
	}

	public void setContractFromDate(Date contractFromDate) {
		this.contractFromDate = contractFromDate;
	}

	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}

	public void setContractToDate(Date contractToDate) {
		this.contractToDate = contractToDate;
	}

	public void setContractType(ContractType contractType) {
		this.contractType = contractType;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public void setCurrentResidence(String currentResidence) {
		this.currentResidence = currentResidence;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public void setGrantDate(Date grantDate) {
		this.grantDate = grantDate;
	}

	public void setGrantPlace(String grantPlace) {
		this.grantPlace = grantPlace;
	}

	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
	}

	public void setIdentityCard(String identityCard) {
		this.identityCard = identityCard;
	}

	public void setInsuranceBookNumber(String insuranceBookNumber) {
		this.insuranceBookNumber = insuranceBookNumber;
	}

	public void setInsurancePaidDate(Date insurancePaidDate) {
		this.insurancePaidDate = insurancePaidDate;
	}

	public void setJob(Job job) {
		this.job = job;
	}

	public void setLevels(String levels) {
		this.levels = levels;
	}

	public void setMajors(String majors) {
		this.majors = majors;
	}

	public void setMarried(String married) {
		this.married = married;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public void setPaidPlace(String paidPlace) {
		this.paidPlace = paidPlace;
	}

	public void setPermanentResidence(String permanentResidence) {
		this.permanentResidence = permanentResidence;
	}

	public void setSalaryBasic(Long salaryBasic) {
		this.salaryBasic = salaryBasic;
	}

	public void setStaffId(Long staffId) {
		this.staffId = staffId;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	public void setTaxCode(String taxCode) {
		this.taxCode = taxCode;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setWorkDate(Date workDate) {
		this.workDate = workDate;
	}

	@Override
	public String toString() {
		return "com.evotek.qlns.model.Staff[ staffId=" + this.staffId + " ]";
	}

}
