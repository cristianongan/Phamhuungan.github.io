package com.viettel.automl.dto.object;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ModelHolderDTO {
	private ModelDTO modelDTO;
	private ConfigFlowDTO configFlowDTO;
	private ParameterInUserDTO parameterInUserDTO;
	private List<SubModelDTO> subModelDTOS;
	private ModelModelTypeMapDTO modelModelTypeMapDTO;
	private ProjectDTO projectDTO;
	private ModelTypeDTO modelTypeDTOS;
	private Long connectionId;
}
