package com.phamhuungan.thymeleafPractices.Entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="user")
public class User implements Person{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	@Column(name="username",length = 50,nullable = false,unique = true)
	private String userName;
	@Column(name="password",nullable = false)
	private String password;
	@Column(name="standar")
	private boolean grantStandar = true;
	@Column(name="writer")
	private boolean grantEmployee =true;
	@Column(name="admin",columnDefinition = "boolean default false")
	private boolean grantAdmin =false;
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="user_id")
	UserInfo userInfo =new UserInfo();
	@Override
	public String getUserName() {
		// TODO Auto-generated method stub
		return this.userName;
	}
	@Override
	public boolean isGrantStandar() {
		// TODO Auto-generated method stub
		return this.grantStandar;
	}
	@Override
	public boolean isGrantEmployee() {
		// TODO Auto-generated method stub
		return this.grantEmployee;
	}
	@Override
	public boolean isGrantAdmin() {
		// TODO Auto-generated method stub
		return this.grantAdmin;
	}



}
