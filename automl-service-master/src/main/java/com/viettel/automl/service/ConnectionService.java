package com.viettel.automl.service;

import com.viettel.automl.dto.object.ConnectionDTO;

import java.util.List;

public interface ConnectionService {
	ConnectionDTO create(ConnectionDTO dto);

	List<ConnectionDTO> getAll();
}
