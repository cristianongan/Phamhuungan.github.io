package com.viettel.automl.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiError {
	private String object;
	private String fieldError;
	private Object rejectedValue;
}
