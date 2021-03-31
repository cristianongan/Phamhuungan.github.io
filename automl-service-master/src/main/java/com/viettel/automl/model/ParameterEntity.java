package com.viettel.automl.model;

import com.viettel.automl.dto.object.ModelDTO;
import com.viettel.automl.dto.object.ParameterDTO;
import com.viettel.automl.dto.object.ProjectDTO;

import javax.persistence.*;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "PARAMETER", schema = "AUTOML", catalog = "")
@SqlResultSetMappings(value = { @SqlResultSetMapping(name = "searchParamMapping", classes = {
		@ConstructorResult(targetClass = ParameterDTO.class, columns = {
				@ColumnResult(name = "parameterId", type = Long.class),
				@ColumnResult(name = "parameterName", type = String.class),
				@ColumnResult(name = "parameterType", type = Long.class),
				@ColumnResult(name = "subModelId", type = Long.class),
				@ColumnResult(name = "modelTypeId", type = Long.class),
				@ColumnResult(name = "dataType", type = Long.class) }) }) })
public class ParameterEntity {
	private Long parameterId;
	private String parameterName;
	private Long parameterType;
	private Long subModelId;
	private Long modelTypeId;
	private Long dataType;

	@Id
	@Column(name = "PARAMETER_ID")
	public Long getParameterId() {
		return parameterId;
	}

	public void setParameterId(Long parameterId) {
		this.parameterId = parameterId;
	}

	@Basic
	@Column(name = "PARAMETER_NAME")
	public String getParameterName() {
		return parameterName;
	}

	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}

	@Basic
	@Column(name = "PARAMETER_TYPE")
	public Long getParameterType() {
		return parameterType;
	}

	public void setParameterType(Long parameterType) {
		this.parameterType = parameterType;
	}

	@Basic
	@Column(name = "MODEL_TYPE_ID")
	public Long getModelTypeId() {
		return modelTypeId;
	}

	public void setModelTypeId(Long modelTypeId) {
		this.modelTypeId = modelTypeId;
	}

	@Basic
	@Column(name = "SUB_MODEL_ID")
	public Long getSubModelId() {
		return subModelId;
	}

	public void setSubModelId(Long subModelId) {
		this.subModelId = subModelId;
	}

	@Basic
	@Column(name = "DATA_TYPE")
	public Long getDataType() {
		return dataType;
	}

	public void setDataType(Long dataType) {
		this.dataType = dataType;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		ParameterEntity that = (ParameterEntity) o;
		return Objects.equals(parameterId, that.parameterId) && Objects.equals(parameterName, that.parameterName)
				&& Objects.equals(parameterType, that.parameterType) && Objects.equals(modelTypeId, that.modelTypeId)
				&& Objects.equals(dataType, that.dataType);
	}

	@Override
	public int hashCode() {
		return Objects.hash(parameterId, parameterName, parameterType, modelTypeId, dataType);
	}
}
