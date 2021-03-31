package com.viettel.automl.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "CONNECTION", schema = "AUTOML", catalog = "")
public class ConnectionEntity {
	private Long connectionId;
	private String connectionName;
	private String connectionUrl;
	private String driverClassName;
	private String userName;
	private String passWord;

	@Id
	@Column(name = "CONNECTION_ID")
	@SequenceGenerator(name = "CONNECTION_SEQ", sequenceName = "CONNECTION_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CONNECTION_SEQ")
	public Long getConnectionId() {
		return connectionId;
	}

	public void setConnectionId(Long connectionId) {
		this.connectionId = connectionId;
	}

	@Basic
	@Column(name = "CONNECTION_NAME")
	public String getConnectionName() {
		return connectionName;
	}

	public void setConnectionName(String connectionName) {
		this.connectionName = connectionName;
	}

	@Basic
	@Column(name = "CONNECTION_URL")
	public String getConnectionUrl() {
		return connectionUrl;
	}

	public void setConnectionUrl(String connectionUrl) {
		this.connectionUrl = connectionUrl;
	}

	@Basic
	@Column(name = "DRIVER_CLASS_NAME")
	public String getDriverClassName() {
		return driverClassName;
	}

	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}

	@Basic
	@Column(name = "USER_NAME")
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Basic
	@Column(name = "PASS_WORD")
	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		ConnectionEntity that = (ConnectionEntity) o;
		return Objects.equals(connectionId, that.connectionId) && Objects.equals(connectionName, that.connectionName)
				&& Objects.equals(connectionUrl, that.connectionUrl)
				&& Objects.equals(driverClassName, that.driverClassName) && Objects.equals(userName, that.userName)
				&& Objects.equals(passWord, that.passWord);
	}

	@Override
	public int hashCode() {
		return Objects.hash(connectionId, connectionName, connectionUrl, driverClassName, userName, passWord);
	}
}
