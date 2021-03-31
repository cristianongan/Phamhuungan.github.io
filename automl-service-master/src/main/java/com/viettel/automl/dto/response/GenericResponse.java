package com.viettel.automl.dto.response;

import com.viettel.automl.config.ErrorCode;
import com.viettel.automl.dto.base.BaseResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GenericResponse<T> extends BaseResponse {
	private T data;

	public GenericResponse() {
	}

	public GenericResponse(T data) {
		super(ErrorCode.OK);
		this.data = data;
	}

	public static <T> GenericResponse<T> success(T data) {
		return new GenericResponse<>(data);
	}
}
