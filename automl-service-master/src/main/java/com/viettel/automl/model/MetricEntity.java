package com.viettel.automl.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "METRIC", schema = "AUTOML", catalog = "")
public class MetricEntity {
	private Long id;
	private String code;
	private String name;
	private Long algorithmType;

	@Id
	@Column(name = "ID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Basic
	@Column(name = "Code")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Basic
	@Column(name = "Name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "ALGORITHM_TYPE")
	public Long getAlgorithmType() {
		return algorithmType;
	}

	public void setAlgorithmType(Long algorithmType) {
		this.algorithmType = algorithmType;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		MetricEntity that = (MetricEntity) o;
		return Objects.equals(id, that.id) && Objects.equals(code, that.code) && Objects.equals(name, that.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, code, name);
	}
}
