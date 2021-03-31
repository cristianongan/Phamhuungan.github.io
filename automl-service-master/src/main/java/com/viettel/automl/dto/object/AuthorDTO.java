package com.viettel.automl.dto.object;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthorDTO {
	private String name;
	private String displayName;

	public AuthorDTO(String name, String displayName) {
		this.name = name;
		this.displayName = displayName;
	}
}
