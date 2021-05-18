package com.viettel.automl.dto.object;

import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ConfigFlowDTO {
	private Long configFlowId;
	private Long projectId;
	private Long modelId;
	private String runNote;
	private String task;
	private String schedule;
	private String outputTableName;
	private Long outputTableMode;
	private String outputTablePartition;
	private String location;
	private Long connectionId;
	private String parameterString;
	private String trainingTable;
	private String validationTable;
	private String testingTable;
	private String inferenceTable;
	private String featureColumns;
	private String labelColumn;
	private String outputColumns;
	private Instant createTime;
	private String createUser;
	private Long runType;

	private List<ParameterDTO> parameterDTOS;
	private ModelDTO modelDTO;
	private List<String> featureColumnArr;
	private List<String> outputColumnArr;
}
