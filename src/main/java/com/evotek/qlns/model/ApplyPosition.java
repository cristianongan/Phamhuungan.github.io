package com.evotek.qlns.model;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="apply_position")
public class ApplyPosition implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;
	private int id;
	private String name;
	private String exp;
	private String technology;
	private String foreign_language_skill;
	
	ApplyPosition(){}
	
	public ApplyPosition(int id, String name, String exp, String technology, String foreign_language_skill) {
		super();
		this.id = id;
		this.name = name;
		this.exp = exp;
		this.technology = technology;
		this.foreign_language_skill = foreign_language_skill;
	}
	
	public ApplyPosition(int id, String name) {
		this.id = id;
		this.name = name;
	}

	@Id
	@Basic
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	public int getId() {
		return id;
	}
	@Basic
	@Column(name="name",nullable = false,length=255)
	public String getName() {
		return name;
	}
	@Basic
	@Column(name="exp")
	public String getExp() {
		return exp;
	}
	@Basic
	@Column(name="technology")
	public String getTechnology() {
		return technology;
	}
	@Basic
	@Column(name="foreign_language_skill")
	public String getForeign_language_skill() {
		return foreign_language_skill;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setExp(String exp) {
		this.exp = exp;
	}

	public void setTechnology(String technology) {
		this.technology = technology;
	}

	public void setForeign_language_skill(String foreign_language_skill) {
		this.foreign_language_skill = foreign_language_skill;
	}
	@Override
	public int hashCode() {
		return id*name.hashCode();
	}
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof ApplyPosition) {
			ApplyPosition applyPosition = (ApplyPosition) obj;
			if(applyPosition.getId()==applyPosition.getId())
				return true;
		}
		return false;
	}

}
