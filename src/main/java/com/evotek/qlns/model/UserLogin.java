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

    private Boolean success;
    private String userName;
    private String ip;
    private Long userLoginId;
    private Date loginTime;

    @Column(name = "success", length = 1)
    public Boolean getSuccess() {
        return this.success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    @Column(name = "user_name", length = 75)
    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Column(name = "ip", length = 75)
    public String getIp() {
        return this.ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_login_id", unique = true, nullable = false, precision = 22, scale = 0)
    public Long getUserLoginId() {
        return this.userLoginId;
    }

    public void setUserLoginId(Long userLoginId) {
        this.userLoginId = userLoginId;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "login_time", length = 7)
    public Date getLoginTime() {
        return this.loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public UserLogin() {
    }

    public UserLogin(Long userLoginId) {
        this.userLoginId = userLoginId;
    }

    public UserLogin(String userName, String ip) {
        this.userName = userName;
        this.ip = ip;
    }
    
}
