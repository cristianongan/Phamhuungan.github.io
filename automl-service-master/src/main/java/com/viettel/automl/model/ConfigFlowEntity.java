package com.viettel.automl.model;

import javax.persistence.*;
import java.sql.Time;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "CONFIG_FLOW", schema = "AUTOML", catalog = "")
public class ConfigFlowEntity {
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

	@Id
	@Column(name = "CONFIG_FLOW_ID")
	@SequenceGenerator(name = "CONFIG_FLOW_SEQ", sequenceName = "CONFIG_FLOW_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CONFIG_FLOW_SEQ")
	public Long getConfigFlowId() {
		return configFlowId;
	}

	public void setConfigFlowId(Long configFlowId) {
		this.configFlowId = configFlowId;
	}

	@Basic
	@Column(name = "PROJECT_ID")
	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
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
	@Column(name = "RUN_NOTE")
	public String getRunNote() {
		return runNote;
	}

	public void setRunNote(String runNote) {
		this.runNote = runNote;
	}

	@Basic
	@Column(name = "TASK")
	public String getTask() {
		return task;
	}

	public void setTask(String task) {
		this.task = task;
	}

	@Basic
	@Column(name = "SCHEDULE")
	public String getSchedule() {
		return schedule;
	}

	public void setSchedule(String schedule) {
		this.schedule = schedule;
	}

	@Basic
	@Column(name = "OUTPUT_TABLE_NAME")
	public String getOutputTableName() {
		return outputTableName;
	}

	public void setOutputTableName(String outputTableName) {
		this.outputTableName = outputTableName;
	}

	@Basic
	@Column(name = "OUTPUT_TABLE_MODE")
	public Long getOutputTableMode() {
		return outputTableMode;
	}

	public void setOutputTableMode(Long outputTableMode) {
		this.outputTableMode = outputTableMode;
	}

	@Basic
	@Column(name = "OUTPUT_TABLE_PARTITION")
	public String getOutputTablePartition() {
		return outputTablePartition;
	}

	public void setOutputTablePartition(String outputTablePartition) {
		this.outputTablePartition = outputTablePartition;
	}

	@Basic
	@Column(name = "LOCATION")
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	@Basic
	@Column(name = "CONNECTION_ID")
	public Long getConnectionId() {
		return connectionId;
	}

	public void setConnectionId(Long connectionId) {
		this.connectionId = connectionId;
	}

	@Basic
	@Column(name = "PARAMETER_STRING")
	public String getParameterString() {
		return parameterString;
	}

	public void setParameterString(String parameterString) {
		this.parameterString = parameterString;
	}

	@Basic
	@Column(name = "TRAINING_TABLE")
	public String getTrainingTable() {
		return trainingTable;
	}

	public void setTrainingTable(String trainingTable) {
		this.trainingTable = trainingTable;
	}

	@Basic
	@Column(name = "VALIDATION_TABLE")
	public String getValidationTable() {
		return validationTable;
	}

	public void setValidationTable(String validationTable) {
		this.validationTable = validationTable;
	}

	@Basic
	@Column(name = "TESTING_TABLE")
	public String getTestingTable() {
		return testingTable;
	}

	public void setTestingTable(String testingTable) {
		this.testingTable = testingTable;
	}

	@Basic
	@Column(name = "INFERENCE_TABLE")
	public String getInferenceTable() {
		return inferenceTable;
	}

	public void setInferenceTable(String inferenceTable) {
		this.inferenceTable = inferenceTable;
	}

	@Basic
	@Column(name = "FEATURE_COLUMNS")
	public String getFeatureColumns() {
		return featureColumns;
	}

	public void setFeatureColumns(String featureColumns) {
		this.featureColumns = featureColumns;
	}

	@Basic
	@Column(name = "LABEL_COLUMN")
	public String getLabelColumn() {
		return labelColumn;
	}

	public void setLabelColumn(String labelColumn) {
		this.labelColumn = labelColumn;
	}

	@Basic
	@Column(name = "OUTPUT_COLUMNS")
	public String getOutputColumns() {
		return outputColumns;
	}

	public void setOutputColumns(String outputColumns) {
		this.outputColumns = outputColumns;
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
	@Column(name = "RUN_TYPE")
	public Long getRunType() {
		return runType;
	}

	public void setRunType(Long runType) {
		this.runType = runType;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		ConfigFlowEntity that = (ConfigFlowEntity) o;
		return Objects.equals(configFlowId, that.configFlowId) && Objects.equals(projectId, that.projectId)
				&& Objects.equals(modelId, that.modelId) && Objects.equals(runNote, that.runNote)
				&& Objects.equals(task, that.task) && Objects.equals(schedule, that.schedule)
				&& Objects.equals(outputTableName, that.outputTableName)
				&& Objects.equals(outputTableMode, that.outputTableMode)
				&& Objects.equals(outputTablePartition, that.outputTablePartition)
				&& Objects.equals(location, that.location) && Objects.equals(connectionId, that.connectionId)
				&& Objects.equals(parameterString, that.parameterString)
				&& Objects.equals(trainingTable, that.trainingTable)
				&& Objects.equals(validationTable, that.validationTable)
				&& Objects.equals(testingTable, that.testingTable)
				&& Objects.equals(inferenceTable, that.inferenceTable)
				&& Objects.equals(featureColumns, that.featureColumns) && Objects.equals(labelColumn, that.labelColumn)
				&& Objects.equals(outputColumns, that.outputColumns) && Objects.equals(createTime, that.createTime)
				&& Objects.equals(createUser, that.createUser);
	}

	@Override
	public int hashCode() {
		return Objects.hash(configFlowId, projectId, modelId, runNote, task, schedule, outputTableName, outputTableMode,
				outputTablePartition, location, connectionId, parameterString, trainingTable, validationTable,
				testingTable, inferenceTable, featureColumns, labelColumn, outputColumns, createTime, createUser);
	}
}
