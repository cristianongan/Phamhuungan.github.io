package com.viettel.automl.service.impl;

import com.viettel.automl.config.ErrorCode;
import com.viettel.automl.connections.HiveJdbcClient;
import com.viettel.automl.exception.ClientException;
import com.viettel.automl.exception.ServerException;
import com.viettel.automl.model.ConnectionEntity;
import com.viettel.automl.repository.ConnectionRepository;
import com.viettel.automl.util.TableNameParser;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class HiveDBService {

	private final ConnectionRepository connectionRepository;

	public HiveDBService(ConnectionRepository connectionRepository) {
		this.connectionRepository = connectionRepository;
	}

	public List<Column> getTableColumns(String schema, String tableName, Long connectionId) {

		List<Column> columns = new ArrayList<>();
//        throw new ClientException(ErrorCode.ALREADY_EXIST);

		String sql = "describe " + tableName;
		ConnectionEntity connectionEntity = connectionRepository.findById(connectionId)
				.orElseThrow(() -> new ServerException(ErrorCode.NOT_FOUND, "Connection"));

		Connection conn = HiveJdbcClient.getConnectionHive(connectionEntity.getDriverClassName(),
				connectionEntity.getConnectionUrl(), connectionEntity.getUserName(), connectionEntity.getPassWord());

		try {
			Statement stmt = conn.createStatement();
			ResultSet res = stmt.executeQuery(sql);

			while (res.next()) {
//                columns.add(res.getString(1));
				columns.add(new Column(res.getString(1), res.getString(2), tableName));
			}
		} catch (SQLException ex) {
			throw new ServerException(ErrorCode.SERVER_ERROR);
		}
		return columns;
	}

	public List<Map<String, Object>> executeQuery(String sql, Map<String, String> params, Long connectionId) {
		List<Map<String, Object>> list = null;
		sql = this.replaceSql(sql, params);
		ConnectionEntity connectionEntity = connectionRepository.findById(connectionId)
				.orElseThrow(() -> new ServerException(ErrorCode.NOT_FOUND, "Connection"));

		DataSource dataSource = new DataSource();
		dataSource.setUrl(connectionEntity.getConnectionUrl());
		dataSource.setDriverClassName(connectionEntity.getDriverClassName());
		dataSource.setUsername(connectionEntity.getUserName() == null ? "" : connectionEntity.getUserName());
		dataSource.setPassword(connectionEntity.getPassWord() == null ? "" : connectionEntity.getPassWord());

		try {
			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			list = jdbcTemplate.queryForList(sql);
		} catch (Exception exception) {
			throw new ClientException(ErrorCode.CLIENT_ERROR, "Execute query fail!");
		}
		return list;
	}

	private String replaceSql(String sql, Map<String, String> params) {
		for (Map.Entry<String, String> entry : params.entrySet()) {
			if (sql.contains("$(" + entry.getKey() + ")")) {
				sql = sql.replace("$(" + entry.getKey() + ")", entry.getValue());
			}
		}
		return sql;
	}

	public List<Column> getColumnsFromTable(String sql, Long connectionId) {
		List<Column> columns = new ArrayList<>();
		TableNameParser tableNameParser = new TableNameParser(sql);
		Collection<String> tables = tableNameParser.tables();
		tables.forEach(table -> {
			columns.addAll(this.getTableColumns("default", table, connectionId));
		});

		return columns.stream().filter(e -> "int".equals(e.getType()) || "double".equals(e.getType())
				|| "float".equals(e.getType()) || "bigint".equals(e.getType())).collect(Collectors.toList());
	}

	@AllArgsConstructor
	@Data
	class Column {
		private String name;
		private String type;
		private String table;
	}
}
