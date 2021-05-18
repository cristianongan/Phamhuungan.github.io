package com.viettel.automl.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "USERS", schema = "AUTOML", catalog = "")
public class UsersEntity {
	private Long id;
	private String username;
	private String password;

	@Id
	@Column(name = "ID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Basic
	@Column(name = "USERNAME")
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Basic
	@Column(name = "PASSWORD")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		UsersEntity that = (UsersEntity) o;
		return Objects.equals(id, that.id) && Objects.equals(username, that.username)
				&& Objects.equals(password, that.password);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, username, password);
	}
}
