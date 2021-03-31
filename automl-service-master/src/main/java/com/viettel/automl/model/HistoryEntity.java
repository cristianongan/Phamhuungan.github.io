package com.viettel.automl.model;

import javax.persistence.*;
import java.sql.Time;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "HISTORY", schema = "AUTOML", catalog = "")
public class HistoryEntity {
	private Long historyId;
	private Long modelId;
	private Long configFlowId;
	private String runNote;
	private String modelScore;
	private String task;
	private String inferTable;
	private String progress;
	private String currentTask;
	private Long currentStatus;
	private String currentRunNote;
	private Time lastUpdateTime;
	private String location;
	private String schedule;
	private String interpreter;
	private Instant createTime;

	@Id
	@Column(name = "HISTORY_ID")
	@SequenceGenerator(name = "HISTORY_SEQ", sequenceName = "HISTORY_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HISTORY_SEQ")
	public Long getHistoryId() {
		return historyId;
	}

	public void setHistoryId(Long historyId) {
		this.historyId = historyId;
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
	@Column(name = "RUN_NOTE")
	public String getRunNote() {
		return runNote;
	}

	public void setRunNote(String runNote) {
		this.runNote = runNote;
	}

	@Basic
	@Column(name = "MODEL_SCORE")
	public String getModelScore() {
		return modelScore;
	}

	public void setModelScore(String modelScore) {
		this.modelScore = modelScore;
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
	@Column(name = "INFER_TABLE")
	public String getInferTable() {
		return inferTable;
	}

	public void setInferTable(String inferTable) {
		this.inferTable = inferTable;
	}

	@Basic
	@Column(name = "PROGRESS")
	public String getProgress() {
		return progress;
	}

	public void setProgress(String progress) {
		this.progress = progress;
	}

	@Basic
	@Column(name = "CURRENT_TASK")
	public String getCurrentTask() {
		return currentTask;
	}

	public void setCurrentTask(String currentTask) {
		this.currentTask = currentTask;
	}

	@Basic
	@Column(name = "CURRENT_STATUS")
	public Long getCurrentStatus() {
		return currentStatus;
	}

	public void setCurrentStatus(Long currentStatus) {
		this.currentStatus = currentStatus;
	}

	@Basic
	@Column(name = "CURRENT_RUN_NOTE")
	public String getCurrentRunNote() {
		return currentRunNote;
	}

	public void setCurrentRunNote(String currentRunNote) {
		this.currentRunNote = currentRunNote;
	}

	@Basic
	@Column(name = "LAST_UPDATE_TIME")
	public Time getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Time lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
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
	@Column(name = "SCHEDULE")
	public String getSchedule() {
		return schedule;
	}

	public void setSchedule(String schedule) {
		this.schedule = schedule;
	}

	@Basic
	@Column(name = "INTERPRETER")
	public String getInterpreter() {
		return interpreter;
	}

	public void setInterpreter(String interpreter) {
		this.interpreter = interpreter;
	}

	@Basic
	@Column(name = "CREATE_TIME")
	public Instant getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Instant createTime) {
		this.createTime = createTime;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		HistoryEntity that = (HistoryEntity) o;
		return Objects.equals(historyId, that.historyId) && Objects.equals(modelId, that.modelId)
				&& Objects.equals(runNote, that.runNote) && Objects.equals(modelScore, that.modelScore)
				&& Objects.equals(task, that.task) && Objects.equals(inferTable, that.inferTable)
				&& Objects.equals(progress, that.progress) && Objects.equals(currentTask, that.currentTask)
				&& Objects.equals(currentStatus, that.currentStatus)
				&& Objects.equals(currentRunNote, that.currentRunNote)
				&& Objects.equals(lastUpdateTime, that.lastUpdateTime) && Objects.equals(location, that.location)
				&& Objects.equals(schedule, that.schedule) && Objects.equals(interpreter, that.interpreter);
	}

	@Override
	public int hashCode() {
		return Objects.hash(historyId, modelId, runNote, modelScore, task, inferTable, progress, currentTask,
				currentStatus, currentRunNote, lastUpdateTime, location, schedule, interpreter);
	}
}
