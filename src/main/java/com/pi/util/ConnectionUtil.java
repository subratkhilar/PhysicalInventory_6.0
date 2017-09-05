package com.pi.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionUtil {
	private static final String driverClass = "com.mysql.jdbc.Driver";
	private static final String url = "jdbc:mysql://localhost:3306/physical_inventory";
	private static final String username = "root";
	private static final String password = "root";
	private static Connection conn = null;

	private ConnectionUtil() {

	}

	public static Connection getConnection() {
		try {

			Class.forName(driverClass);
			conn = DriverManager.getConnection(url, username, password);
		} catch (Exception e) {

		}
		return conn;
	}

	public static Connection closeConnection() {
		if(conn!=null)
		{
			try {
				conn.close();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return conn;
	}
}
