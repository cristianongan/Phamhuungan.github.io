package com.viettel.automl.connections;

import org.springframework.util.StringUtils;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;

public class HiveJdbcClient {
	private static String driverName = "org.apache.hive.jdbc.HiveDriver";

	/**
	 * @param
	 * @throws SQLException
	 */
	public static Connection getConnectionHive(String driver, String url, String user, String password) {
		Connection conn = null;

		try {
			driver = StringUtils.isEmpty(driver) ? driverName : driver;
			Class.forName(driver);
//            conn = DriverManager.getConnection("jdbc:hive2://128.199.239.168:10001/default", "", "");
			conn = DriverManager.getConnection(url, user, password);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return conn;
	}
}
