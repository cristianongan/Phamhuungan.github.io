package com.viettel.automl.dto.object;

import com.viettel.automl.dto.base.BaseDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Time;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class HistoryDTO extends BaseDTO {
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
	private Instant lastUpdateTime;
	private String location;
	private String schedule;
	private String interpreter;
	private Instant createTime;

	// dto only
	private ModelDTO modelDTO;
	private ConfigFlowDTO configFlowDTO;
	private ConnectionDTO connectionDTO;
	private int[] tasks;
	private List<List<ParameterInUserDTO>> searchSpace;

	private List<String> columnsFeature;
	private List<String> columnsOutput;

	// dto submited queues
	List<ProjectDTO> projectDTOS;
}
