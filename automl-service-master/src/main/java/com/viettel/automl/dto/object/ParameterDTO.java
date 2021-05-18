package com.viettel.automl.dto.object;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ParameterDTO {
	private Long parameterId;
	private String parameterName;
	private Long parameterType;
	private Long subModelId;
	private Long modelTypeId;
	private Long dataType;

	private Float min;
	private Float max;
	private Long uniform;
	private Float step;
	private String parameterValue;

	public ParameterDTO(Long parameterId, String parameterName, Long parameterType, Long subModelId, Long modelTypeId,
			Long dataType) {
		this.parameterId = parameterId;
		this.parameterName = parameterName;
		this.parameterType = parameterType;
		this.modelTypeId = modelTypeId;
		this.dataType = dataType;
		this.subModelId = subModelId;
	}
}
