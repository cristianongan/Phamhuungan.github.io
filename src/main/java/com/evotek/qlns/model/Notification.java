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
@Table(name = "notification")
public class Notification implements Serializable {
	private static final long serialVersionUID = 1L;
	@Column(name = "class_name")
	private String className;
	@Column(name = "class_pk")
	private Long classPk;
	@Column(name = "create_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;
	@Column(name = "event_date")
	@Temporal(TemporalType.DATE)
	private Date eventDate;
	@Column(name = "expired_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date expiredDate;
	@Column(name = "message")
	private String message;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "notification_id")
	private Long notificationId;
	@Column(name = "notification_type")
	private Long notificationType;
	@Column(name = "status")
	private Long status;

	public Notification() {
	}

	public Notification(Long notificationId) {
		this.notificationId = notificationId;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof Notification)) {
			return false;
		}
		Notification other = (Notification) object;
		if ((this.notificationId == null && other.notificationId != null)
				|| (this.notificationId != null && !this.notificationId.equals(other.notificationId))) {
			return false;
		}
		return true;
	}

	public String getClassName() {
		return this.className;
	}

	public Long getClassPk() {
		return this.classPk;
	}

	public Date getCreateDate() {
		return this.createDate;
	}

	public Date getEventDate() {
		return this.eventDate;
	}

	public Date getExpiredDate() {
		return this.expiredDate;
	}

	public String getMessage() {
		return this.message;
	}

	public Long getNotificationId() {
		return this.notificationId;
	}

	public Long getNotificationType() {
		return this.notificationType;
	}

	public Long getStatus() {
		return this.status;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (this.notificationId != null ? this.notificationId.hashCode() : 0);
		return hash;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public void setClassPk(Long classPk) {
		this.classPk = classPk;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
	}

	public void setExpiredDate(Date expiredDate) {
		this.expiredDate = expiredDate;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setNotificationId(Long notificationId) {
		this.notificationId = notificationId;
	}

	public void setNotificationType(Long notificationType) {
		this.notificationType = notificationType;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "com.evotek.qlns.model.Notification[ notificationId=" + this.notificationId + " ]";
	}

}
