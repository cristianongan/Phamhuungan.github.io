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
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name = "user_")
public class User implements Serializable {

	private static final long serialVersionUID = -5124221618445933395L;
	
	private String address;
	private String birthPlace;
	private Date createDate;
	private Date dateOfBirth;
	private Long deptId;
	private String deptName;
	private String description;
	private String email;
	private String firstName;
	private String fullName;
	private Long gender;
	private String lastName;
	private String middleName;
	private String mobile;
	private Date modifiedDate;
	private String password;
	private String phone;
	private Set<Role> roles = new HashSet<Role>(0);
	private Long status;
	private Long userId;
	private String userName;
	private String verificationCode;

	public User() {
	}

	public User(Long userId) {
		this.userId = userId;
	}

	public User(Long userId, String userName) {
		this.userId = userId;
		this.userName = userName;
	}

	public User(String userName) {
		this.userName = userName;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (getClass() != obj.getClass()) {
			return false;
		}

		final User other = (User) obj;

		if (this.userId != other.userId && (this.userId == null || !this.userId.equals(other.userId))) {
			return false;
		}

		return true;
	}

	@Column(name = "address", length = 200)
	public String getAddress() {
		return this.address;
	}

	@Column(name = "birth_place", length = 200)
	public String getBirthPlace() {
		return this.birthPlace;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_date", length = 7)
	public Date getCreateDate() {
		return this.createDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_of_birth", length = 7)
	public Date getDateOfBirth() {
		return this.dateOfBirth;
	}

	@Column(name = "dept_id", precision = 22, scale = 0)
	public Long getDeptId() {
		return this.deptId;
	}

	@Transient
	public String getDeptName() {
		return this.deptName;
	}

	@Column(name = "description", length = 1000)
	public String getDescription() {
		return this.description;
	}

	@Column(name = "email", length = 100)
	public String getEmail() {
		return this.email;
	}

	@Column(name = "first_name", length = 200)
	public String getFirstName() {
		return this.firstName;
	}

	@Transient
	public String getFullName() {
		if (this.firstName != null) {
			this.fullName = this.firstName + " ";
		}

		if (this.middleName != null) {
			this.fullName = this.fullName + this.middleName + " ";
		}

		if (this.lastName != null) {
			this.fullName = this.fullName + this.lastName;
		}

		if (this.fullName != null) {
			this.fullName = this.fullName.trim();
		}

		return this.fullName;
	}

	@Column(name = "gender", precision = 22, scale = 0)
	public Long getGender() {
		return this.gender;
	}

	@Column(name = "last_name", length = 200)
	public String getLastName() {
		return this.lastName;
	}

	@Column(name = "middle_name", length = 200)
	public String getMiddleName() {
		return this.middleName;
	}

	@Column(name = "mobile", length = 20)
	public String getMobile() {
		return this.mobile;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modified_date", length = 7)
	public Date getModifiedDate() {
		return this.modifiedDate;
	}

	@Column(name = "password", length = 255)
	public String getPassword() {
		return this.password;
	}

	@Column(name = "phone", length = 20)
	public String getPhone() {
		return this.phone;
	}

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "user_role", joinColumns = {
			@JoinColumn(name = "user_id", nullable = false, updatable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "role_id", nullable = false, updatable = false) })
//    @LazyCollection(LazyCollectionOption.EXTRA)
	@Fetch(FetchMode.SELECT)
	public Set<Role> getRoles() {
		return this.roles;
	}

	@Column(name = "status", nullable = false, precision = 22, scale = 0)
	public Long getStatus() {
		return this.status;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getUserId() {
		return this.userId;
	}

	@Column(name = "user_name", unique = true, length = 200)
	public String getUserName() {
		return this.userName;
	}

	@Column(name = "verification_code", length = 255)
	public String getVerificationCode() {
		return this.verificationCode;
	}

	@Override
	public int hashCode() {
		int hash = 5;
		return hash;
	}

	@Transient
	public boolean isActive() {
		if (this.status == null) {
			return false;
		}

		return !this.status.equals(0L);
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setBirthPlace(String birthPlace) {
		this.birthPlace = birthPlace;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
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

	public void setEmail(String email) {
		this.email = email;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public void setGender(Long gender) {
		this.gender = gender;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
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

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}
}
