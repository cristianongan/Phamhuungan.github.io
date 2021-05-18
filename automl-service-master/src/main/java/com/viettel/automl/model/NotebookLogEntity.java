package com.viettel.automl.model;

import javax.persistence.*;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "NOTEBOOK_LOG", schema = "AUTOML", catalog = "")
public class NotebookLogEntity {
	private Long logId;
	private Long projectId;
	private Long modelId;
	private Long historyId;
	private String username;
	private String notebookId;
	private String paragraphId;
	private String logOutput;
	private String status;
	private Instant startTime;
	private Instant endTime;
	private Instant createTime;

	@Id
	@Column(name = "LOG_ID")
	@SequenceGenerator(name = "NOTEBOOK_LOG_SEQ", sequenceName = "NOTEBOOK_LOG_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NOTEBOOK_LOG_SEQ")
	public Long getLogId() {
		return logId;
	}

	public void setLogId(Long logId) {
		this.logId = logId;
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
	@Column(name = "HISTORY_ID")
	public Long getHistoryId() {
		return historyId;
	}

	public void setHistoryId(Long historyId) {
		this.historyId = historyId;
	}

	@Basic
	@Column(name = "USERNAME")
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Basic
	@Column(name = "NOTEBOOK_ID")
	public String getNotebookId() {
		return notebookId;
	}

	public void setNotebookId(String notebookId) {
		this.notebookId = notebookId;
	}

	@Basic
	@Column(name = "PARAGRAPH_ID")
	public String getParagraphId() {
		return paragraphId;
	}

	public void setParagraphId(String paragraphId) {
		this.paragraphId = paragraphId;
	}

	@Basic
	@Column(name = "LOG_OUTPUT")
	public String getLogOutput() {
		return logOutput;
	}

	public void setLogOutput(String logOutput) {
		this.logOutput = logOutput;
	}

	@Basic
	@Column(name = "STATUS")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Basic
	@Column(name = "START_TIME")
	public Instant getStartTime() {
		return startTime;
	}

	public void setStartTime(Instant startTime) {
		this.startTime = startTime;
	}

	@Basic
	@Column(name = "END_TIME")
	public Instant getEndTime() {
		return endTime;
	}

	public void setEndTime(Instant endTime) {
		this.endTime = endTime;
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
		NotebookLogEntity that = (NotebookLogEntity) o;
		return Objects.equals(logId, that.logId) && Objects.equals(projectId, that.projectId)
				&& Objects.equals(modelId, that.modelId) && Objects.equals(historyId, that.historyId)
				&& Objects.equals(username, that.username) && Objects.equals(notebookId, that.notebookId)
				&& Objects.equals(paragraphId, that.paragraphId) && Objects.equals(logOutput, that.logOutput)
				&& Objects.equals(status, that.status) && Objects.equals(startTime, that.startTime)
				&& Objects.equals(endTime, that.endTime) && Objects.equals(createTime, that.createTime);
	}

	@Override
	public int hashCode() {
		return Objects.hash(logId, projectId, modelId, historyId, username, notebookId, paragraphId, logOutput, status,
				startTime, endTime, createTime);
	}
}
