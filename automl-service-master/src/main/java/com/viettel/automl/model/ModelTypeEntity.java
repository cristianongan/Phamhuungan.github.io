package com.viettel.automl.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "MODEL_TYPE", schema = "AUTOML", catalog = "")
public class ModelTypeEntity {
	private Long modelTypeId;
	private String modelTypeName;
	private Long algorithmType;

	@Id
	@Column(name = "MODEL_TYPE_ID")
	public Long getModelTypeId() {
		return modelTypeId;
	}

	public void setModelTypeId(Long modelTypeId) {
		this.modelTypeId = modelTypeId;
	}

	@Basic
	@Column(name = "MODEL_TYPE_NAME")
	public String getModelTypeName() {
		return modelTypeName;
	}

	public void setModelTypeName(String modelTypeName) {
		this.modelTypeName = modelTypeName;
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
		ModelTypeEntity that = (ModelTypeEntity) o;
		return Objects.equals(modelTypeId, that.modelTypeId) && Objects.equals(modelTypeName, that.modelTypeName);
	}

	@Override
	public int hashCode() {
		return Objects.hash(modelTypeId, modelTypeName);
	}
}
