package com.viettel.automl.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "DATA_TYPE", schema = "AUTOML", catalog = "")
public class DataTypeEntity {
	private Long id;
	private Long type;
	private String value;

	@Id
	@Basic
	@Column(name = "ID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Basic
	@Column(name = "TYPE")
	public Long getType() {
		return type;
	}

	public void setType(Long type) {
		this.type = type;
	}

	@Basic
	@Column(name = "VALUE")
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		DataTypeEntity that = (DataTypeEntity) o;
		return Objects.equals(id, that.id) && Objects.equals(type, that.type) && Objects.equals(value, that.value);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, type, value);
	}
}
