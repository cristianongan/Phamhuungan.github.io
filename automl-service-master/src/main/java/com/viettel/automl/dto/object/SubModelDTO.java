package com.viettel.automl.dto.object;

import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.util.List;

@Getter
@Setter
public class SubModelDTO {
	private Long subModelId;
	private String subModelName;
	private Long modelTypeId;
	private Time createTime;
	private String createUser;
	private Long modelId;

	private List<ParameterDTO> parameterDTOS;
}
