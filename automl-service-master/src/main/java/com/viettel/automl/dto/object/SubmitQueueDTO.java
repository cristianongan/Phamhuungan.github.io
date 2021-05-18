package com.viettel.automl.dto.object;

import java.util.List;

public class SubmitQueueDTO {
	String interpreter;
	List<ItemOfInterpreterDTO> item;

	public String getInterpreter() {
		return interpreter;
	}

	public void setInterpreter(String interpreter) {
		this.interpreter = interpreter;
	}

	public List<ItemOfInterpreterDTO> getItem() {
		return item;
	}

	public void setItem(List<ItemOfInterpreterDTO> item) {
		this.item = item;
	}
}
