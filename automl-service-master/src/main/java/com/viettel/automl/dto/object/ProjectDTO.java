package com.viettel.automl.dto.object;

import com.viettel.automl.dto.base.BaseDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ProjectDTO extends BaseDTO {
	private Long projectId;
	private String projectName;
	private String description;
	private Instant createTime;
	private String createUser;

	// search model
	private String modelName;
	private String bestModelType;
	private Long modelMode;
	private String task;
	private Long modelType;

	private List<ConfigFlowDTO> configFlowDTOS;

//    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private String createFrom;
//    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private String createTo;

	public ProjectDTO(Long projectId, String projectName, String description, Instant createTime, String createUser) {
		this.projectId = projectId;
		this.projectName = projectName;
		this.description = description;
		this.createTime = createTime;
		this.createUser = createUser;
	}
}
