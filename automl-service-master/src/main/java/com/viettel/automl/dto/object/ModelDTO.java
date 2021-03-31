package com.viettel.automl.dto.object;

import com.viettel.automl.dto.base.BaseDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Time;
import java.time.Instant;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ModelDTO extends BaseDTO {
	private Long modelId;
	private String modelName;
	private Long modelMode;
	private Long algorithmType;
	private Long metrics;
	private Float ratio;
	private boolean biggerIsBetter;
	private Long autoTurningType;
	private Long numberOfFolds;
	private Long maxTrialTime;
	private Long checkpointStep;
	private String optimizationAlgorithm;
	private Float dataSubsamplingRatio;
	private boolean allowPersist;
	private String bestModelType;
	private String description;
	private String bestParameters;
	private Instant createTime;
	private String createUser;
	private Float maxMissingRatioAllow;
	private Long maxDistinctValues;
	private boolean multiclass;

	private List<HistoryDTO> historyDTOS;
	private List<SubModelDTO> subModelDTOS;
	private List<ModelModelTypeMapDTO> modelModelTypeMapDTOS;
	private ProjectDTO projectDTO;
	private ConfigFlowDTO configFlowDTO;
	private ConnectionDTO connectionDTO;

	// dto only
	private Long projectId;
	private String projectName;
	private String task;
	private int[] tasks;
	private String modelTypeName;
	private String modelCore;

	public ModelDTO(Long projectId, String projectName, Long modelId, String modelName, Instant createTime,
			String createUser, String bestModelType, String modelCore) {
		this.modelId = modelId;
		this.modelName = modelName;
		this.projectId = projectId;
		this.projectName = projectName;
		this.task = task;
		this.createTime = createTime;
		this.createUser = createUser;
		this.bestModelType = bestModelType;
//        this.modelTypeName = modelTypeName;
		this.modelCore = modelCore;
	}

	public ModelDTO(Long projectId, String projectName, Long modelId, String modelName, String task,
			Instant createTime) {
		this.modelId = modelId;
		this.modelName = modelName;
		this.projectId = projectId;
		this.projectName = projectName;
		this.task = task;
		this.createTime = createTime;
	}

	public ModelDTO(Long modelId, String modelName, Instant createTime, String createUser, String bestModelType,
			String modelTypeName, String modelCore, String projectName) {
		this.modelId = modelId;
		this.modelName = modelName;
		this.bestModelType = bestModelType;
		this.createTime = createTime;
		this.createUser = createUser;
		this.modelTypeName = modelTypeName;
		this.modelCore = modelCore;
		this.projectName = projectName;
	}
}
