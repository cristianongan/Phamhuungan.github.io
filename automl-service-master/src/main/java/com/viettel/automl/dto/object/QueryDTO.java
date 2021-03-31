package com.viettel.automl.dto.object;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class QueryDTO {
	private String sql;
	private Map<String, String> params;
	private Long connectionId;
}
