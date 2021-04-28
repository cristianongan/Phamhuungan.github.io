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

@Entity
@Table(name = "user_login")
public class UserLogin implements Serializable {

	private String ip;
	private Date loginTime;
	private Boolean success;
	private Long userLoginId;
	private String userName;

	public UserLogin() {
	}

	public UserLogin(Long userLoginId) {
		this.userLoginId = userLoginId;
	}

	public UserLogin(String userName, String ip) {
		this.userName = userName;
		this.ip = ip;
	}

	@Column(name = "ip", length = 75)
	public String getIp() {
		return this.ip;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "login_time", length = 7)
	public Date getLoginTime() {
		return this.loginTime;
	}

	@Column(name = "success", length = 1)
	public Boolean getSuccess() {
		return this.success;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_login_id", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getUserLoginId() {
		return this.userLoginId;
	}

	@Column(name = "user_name", length = 75)
	public String getUserName() {
		return this.userName;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public void setUserLoginId(Long userLoginId) {
		this.userLoginId = userLoginId;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
