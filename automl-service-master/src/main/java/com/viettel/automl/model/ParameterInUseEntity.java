package com.viettel.automl.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "PARAMETER_IN_USE", schema = "AUTOML", catalog = "")
public class ParameterInUseEntity {
	private Long parameterInUseId;
	private Long parameterId;
	private String parameterName;
	private Long parameterType;
	private Long modelId;
	private Long configFlowId;
	private Long subModelId;
	private Long modelTypeId;
	private Long dataType;
	private String parameterValue;
	private Float min;
	private Float max;
	private Long uniform;
	private Float step;

	@Id
	@Column(name = "PARAMETER_IN_USE_ID")
	@SequenceGenerator(name = "PARAMETER_IN_USE_SEQ", sequenceName = "PARAMETER_IN_USE_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PARAMETER_IN_USE_SEQ")
	public Long getParameterInUseId() {
		return parameterInUseId;
	}

	public void setParameterInUseId(Long parameterInUseId) {
		this.parameterInUseId = parameterInUseId;
	}

	@Basic
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
	@Column(name = "MODEL_ID")
	public Long getModelId() {
		return modelId;
	}

	public void setModelId(Long modelId) {
		this.modelId = modelId;
	}

	@Basic
	@Column(name = "CONFIG_FLOW_ID")
	public Long getConfigFlowId() {
		return configFlowId;
	}

	public void setConfigFlowId(Long configFlowId) {
		this.configFlowId = configFlowId;
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
	@Column(name = "MODEL_TYPE_ID")
	public Long getModelTypeId() {
		return modelTypeId;
	}

	public void setModelTypeId(Long modelTypeId) {
		this.modelTypeId = modelTypeId;
	}

	@Basic
	@Column(name = "DATA_TYPE")
	public Long getDataType() {
		return dataType;
	}

	public void setDataType(Long dataType) {
		this.dataType = dataType;
	}

	@Basic
	@Column(name = "PARAMETER_VALUE")
	public String getParameterValue() {
		return parameterValue;
	}

	public void setParameterValue(String parameterValue) {
		this.parameterValue = parameterValue;
	}

	@Basic
	@Column(name = "MIN")
	public Float getMin() {
		return min;
	}

	public void setMin(Float min) {
		this.min = min;
	}

	@Basic
	@Column(name = "MAX")
	public Float getMax() {
		return max;
	}

	public void setMax(Float max) {
		this.max = max;
	}

	@Basic
	@Column(name = "UNIFORM")
	public Long getUniform() {
		return uniform;
	}

	public void setUniform(Long uniform) {
		this.uniform = uniform;
	}

	@Basic
	@Column(name = "STEP")
	public Float getStep() {
		return step;
	}

	public void setStep(Float step) {
		this.step = step;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		ParameterInUseEntity that = (ParameterInUseEntity) o;
		return Objects.equals(parameterInUseId, that.parameterInUseId) && Objects.equals(parameterId, that.parameterId)
				&& Objects.equals(parameterName, that.parameterName)
				&& Objects.equals(parameterType, that.parameterType) && Objects.equals(modelId, that.modelId)
				&& Objects.equals(subModelId, that.subModelId) && Objects.equals(modelTypeId, that.modelTypeId)
				&& Objects.equals(dataType, that.dataType) && Objects.equals(parameterValue, that.parameterValue)
				&& Objects.equals(min, that.min) && Objects.equals(max, that.max)
				&& Objects.equals(uniform, that.uniform) && Objects.equals(step, that.step);
	}

	@Override
	public int hashCode() {
		return Objects.hash(parameterInUseId, parameterId, parameterName, parameterType, modelId, subModelId,
				modelTypeId, dataType, parameterValue, min, max, uniform, step);
	}
}
