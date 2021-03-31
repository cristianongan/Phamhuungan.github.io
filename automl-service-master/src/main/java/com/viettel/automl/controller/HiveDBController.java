package com.viettel.automl.controller;

import com.viettel.automl.connections.HiveJdbcClient;
import com.viettel.automl.dto.object.ConnectionDTO;
import com.viettel.automl.dto.object.QueryDTO;
import com.viettel.automl.dto.response.GenericResponse;
import com.viettel.automl.service.impl.HiveDBService;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/hive-db")
public class HiveDBController {

	private final HiveDBService hiveDBService;

	public HiveDBController(HiveDBService hiveDBService) {
		this.hiveDBService = hiveDBService;
	}

	@PostMapping("/test-conn")
	public GenericResponse<?> testConn(@RequestBody ConnectionDTO dto) {
		Boolean result = false;
		Connection conn = HiveJdbcClient.getConnectionHive(dto.getDriverClassName(), dto.getConnectionUrl(),
				dto.getUserName(), dto.getPassWord());
		if (null != conn)
			result = true;
		return GenericResponse.success(result);
	}

	@GetMapping("/table-columns")
	public GenericResponse<?> getTableColumns(@RequestParam String table, @RequestParam Long connectionId) {
		return GenericResponse.success(hiveDBService.getTableColumns("default", table, connectionId));
	}

	@PostMapping("/execute-sql")
	public GenericResponse<?> executeQuery(@RequestBody QueryDTO dto) {
		return GenericResponse
				.success(hiveDBService.executeQuery(dto.getSql(), dto.getParams(), dto.getConnectionId()));
	}

	@PostMapping("/sql-columns")
	public GenericResponse<?> getSqlColumns(@RequestBody QueryDTO dto) {
		return GenericResponse.success(hiveDBService.getColumnsFromTable(dto.getSql(), dto.getConnectionId()));
	}
}
