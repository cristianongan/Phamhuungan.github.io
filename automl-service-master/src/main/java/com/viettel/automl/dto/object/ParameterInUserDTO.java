package com.viettel.automl.dto.object;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ParameterInUserDTO {
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

	private String modelTypeName;
}
