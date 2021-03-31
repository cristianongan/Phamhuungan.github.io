package com.viettel.automl.dto.object;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class NotebookLogDTO {
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

	private String projectName;
	private String modelName;
	private Float executeTime;
}
