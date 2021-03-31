package com.viettel.automl.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParaResponse {
	@JsonIgnore
	private int statusCode;
	private String status;
	private String message;
	private BodyPara body;
}
