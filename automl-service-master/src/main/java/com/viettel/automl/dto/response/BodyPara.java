package com.viettel.automl.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BodyPara {
	private String title;
	private String text;

	// dto
	private String notebookId;
	private String paraId;
}
