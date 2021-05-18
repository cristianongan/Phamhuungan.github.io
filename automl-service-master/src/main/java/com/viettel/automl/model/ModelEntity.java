package com.viettel.automl.model;

import javax.persistence.*;
import java.sql.Time;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "MODEL", schema = "AUTOML", catalog = "")
public class ModelEntity {
	private Long modelId;
	private String modelName;
	private Long modelMode;
	private Long algorithmType;
	private Long metrics;
	private Long biggerIsBetter;
	private Long autoTurningType;
	private Long numberOfFolds;
	private Long maxTrialTime;
	private Long checkpointStep;
	private String optimizationAlgorithm;
	private Float dataSubsamplingRatio;
	private Long allowPersist;
	private String bestModelType;
	private String description;
	private String bestParameters;
	private Instant createTime;
	private String createUser;
	private Float ratio;
	private Float maxMissingRatioAllow;
	private Long maxDistinctValues;
	private Long multiclass;

	@Id
	@Column(name = "MODEL_ID")
	@SequenceGenerator(name = "MODEL_SEQ", sequenceName = "MODEL_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MODEL_SEQ")
	public Long getModelId() {
		return modelId;
	}

	public void setModelId(Long modelId) {
		this.modelId = modelId;
	}

	@Basic
	@Column(name = "MODEL_NAME")
	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	@Basic
	@Column(name = "MODEL_MODE")
	public Long getModelMode() {
		return modelMode;
	}

	public void setModelMode(Long modelMode) {
		this.modelMode = modelMode;
	}

	@Basic
	@Column(name = "ALGORITHM_TYPE")
	public Long getAlgorithmType() {
		return algorithmType;
	}

	public void setAlgorithmType(Long algorithmType) {
		this.algorithmType = algorithmType;
	}

	@Basic
	@Column(name = "METRICS")
	public Long getMetrics() {
		return metrics;
	}

	public void setMetrics(Long metrics) {
		this.metrics = metrics;
	}

	@Basic
	@Column(name = "BIGGER_IS_BETTER")
	public Long getBiggerIsBetter() {
		return biggerIsBetter;
	}

	public void setBiggerIsBetter(Long biggerIsBetter) {
		this.biggerIsBetter = biggerIsBetter;
	}

	@Basic
	@Column(name = "AUTO_TURNING_TYPE")
	public Long getAutoTurningType() {
		return autoTurningType;
	}

	public void setAutoTurningType(Long autoTurningType) {
		this.autoTurningType = autoTurningType;
	}

	@Basic
	@Column(name = "NUMBER_OF_FOLDS")
	public Long getNumberOfFolds() {
		return numberOfFolds;
	}

	public void setNumberOfFolds(Long numberOfFolds) {
		this.numberOfFolds = numberOfFolds;
	}

	@Basic
	@Column(name = "MAX_TRIAL_TIME")
	public Long getMaxTrialTime() {
		return maxTrialTime;
	}

	public void setMaxTrialTime(Long maxTrialTime) {
		this.maxTrialTime = maxTrialTime;
	}

	@Basic
	@Column(name = "CHECKPOINT_STEP")
	public Long getCheckpointStep() {
		return checkpointStep;
	}

	public void setCheckpointStep(Long checkpointStep) {
		this.checkpointStep = checkpointStep;
	}

	@Basic
	@Column(name = "OPTIMIZATION_ALGORITHM")
	public String getOptimizationAlgorithm() {
		return optimizationAlgorithm;
	}

	public void setOptimizationAlgorithm(String optimizationAlgorithm) {
		this.optimizationAlgorithm = optimizationAlgorithm;
	}

	@Basic
	@Column(name = "DATA_SUBSAMPLING_RATIO")
	public Float getDataSubsamplingRatio() {
		return dataSubsamplingRatio;
	}

	public void setDataSubsamplingRatio(Float dataSubsamplingRatio) {
		this.dataSubsamplingRatio = dataSubsamplingRatio;
	}

	@Basic
	@Column(name = "ALLOW_PERSIST")
	public Long getAllowPersist() {
		return allowPersist;
	}

	public void setAllowPersist(Long allowPersist) {
		this.allowPersist = allowPersist;
	}

	@Basic
	@Column(name = "BEST_MODEL_TYPE")
	public String getBestModelType() {
		return bestModelType;
	}

	public void setBestModelType(String bestModelType) {
		this.bestModelType = bestModelType;
	}

	@Basic
	@Column(name = "DESCRIPTION")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Basic
	@Column(name = "BEST_PARAMETERS")
	public String getBestParameters() {
		return bestParameters;
	}

	public void setBestParameters(String bestParameters) {
		this.bestParameters = bestParameters;
	}

	@Basic
	@Column(name = "CREATE_TIME")
	public Instant getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Instant createTime) {
		this.createTime = createTime;
	}

	@Basic
	@Column(name = "CREATE_USER")
	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	@Basic
	@Column(name = "RATIO")
	public Float getRatio() {
		return ratio;
	}

	public void setRatio(Float ratio) {
		this.ratio = ratio;
	}

	@Basic
	@Column(name = "MAX_MISSING_RATIO_ALLOW")
	public Float getMaxMissingRatioAllow() {
		return maxMissingRatioAllow;
	}

	public void setMaxMissingRatioAllow(Float maxMissingRatioAllow) {
		this.maxMissingRatioAllow = maxMissingRatioAllow;
	}

	@Basic
	@Column(name = "MAX_DISTINCT_VALUES")
	public Long getMaxDistinctValues() {
		return maxDistinctValues;
	}

	public void setMaxDistinctValues(Long maxDistinctValues) {
		this.maxDistinctValues = maxDistinctValues;
	}

	@Basic
	@Column(name = "MULTICLASS")
	public Long getMulticlass() {
		return multiclass;
	}

	public void setMulticlass(Long multiclass) {
		this.multiclass = multiclass;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		ModelEntity that = (ModelEntity) o;
		return Objects.equals(modelId, that.modelId) && Objects.equals(modelName, that.modelName)
				&& Objects.equals(modelMode, that.modelMode) && Objects.equals(algorithmType, that.algorithmType)
				&& Objects.equals(metrics, that.metrics) && Objects.equals(biggerIsBetter, that.biggerIsBetter)
				&& Objects.equals(autoTurningType, that.autoTurningType)
				&& Objects.equals(numberOfFolds, that.numberOfFolds) && Objects.equals(maxTrialTime, that.maxTrialTime)
				&& Objects.equals(checkpointStep, that.checkpointStep)
				&& Objects.equals(optimizationAlgorithm, that.optimizationAlgorithm)
				&& Objects.equals(dataSubsamplingRatio, that.dataSubsamplingRatio)
				&& Objects.equals(allowPersist, that.allowPersist) && Objects.equals(bestModelType, that.bestModelType)
				&& Objects.equals(description, that.description) && Objects.equals(bestParameters, that.bestParameters)
				&& Objects.equals(createTime, that.createTime) && Objects.equals(createUser, that.createUser)
				&& Objects.equals(ratio, that.ratio) && Objects.equals(maxMissingRatioAllow, that.maxMissingRatioAllow)
				&& Objects.equals(maxDistinctValues, that.maxDistinctValues);
	}

	@Override
	public int hashCode() {
		return Objects.hash(modelId, modelName, modelMode, algorithmType, metrics, biggerIsBetter, autoTurningType,
				numberOfFolds, maxTrialTime, checkpointStep, optimizationAlgorithm, dataSubsamplingRatio, allowPersist,
				bestModelType, description, bestParameters, createTime, createUser, ratio, maxMissingRatioAllow,
				maxDistinctValues);
	}
}
