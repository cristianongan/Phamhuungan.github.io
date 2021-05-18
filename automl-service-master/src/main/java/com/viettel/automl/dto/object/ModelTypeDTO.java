package com.viettel.automl.dto.object;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ModelTypeDTO {
	private Long modelTypeId;
	private String modelTypeName;
	private Long algorithmType;
	private List<List<ParameterDTO>> parameterDTOS;
}
