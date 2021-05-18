package com.viettel.automl.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfoRequest {
	private String username;
	private String password;
//    private Long userId;
//    private Long deptId;

	public UserInfoRequest() {
	}

	public UserInfoRequest(String username) {
		this.username = username;
	}
}
