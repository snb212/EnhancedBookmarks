package com.bookmarks.servlet.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.bookmarks.servlet.common.BookmarkService;

public class JDBCMySQLConnection {
	//static reference to itself
	private static JDBCMySQLConnection instance = new JDBCMySQLConnection();
	public static final String URL = BookmarkService.MYSQL_URL;
	public static final String USER = BookmarkService.MYSQL_USER;
	public static final String PASSWORD = BookmarkService.MYSQL_PASSWORD;
	public static final String DRIVER_CLASS = "com.mysql.jdbc.Driver"; 
	
	//private constructor
	private JDBCMySQLConnection() {
		try {
			//Load MySQL Java driver
			Class.forName(DRIVER_CLASS);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static Connection createConnection() {

		Connection connection = null;
		try {
			//Establish Java MySQL connection
			connection = DriverManager.getConnection(URL, USER, PASSWORD);
		} catch (SQLException e) {
			System.out.println("ERROR: Unable to Connect to Database.");
		}
		return connection;
	}	
	
	public static Connection getConnection() {
		return instance.createConnection();
	}
}

