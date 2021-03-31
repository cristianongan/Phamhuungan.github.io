package com.viettel.automl.dto.object;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ConnectionDTO {
	private Long connectionId;
	private String connectionName;
	private String connectionUrl;
	private String driverClassName;
	private String userName;
	private String passWord;

	private List<ConfigFlowDTO> configFlowDTOS;
}
