package com.bookmarks.servlet.common;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.bookmarks.servlet.mysql.JDBCMySQLConnection;

public class BookmarkService_old {
	public BookmarkService_old(){
		ResultSet rs = null;
		Connection connection = null;
		Statement statement = null; 
		
		String query = "SELECT * FROM test";
		System.out.println("Building query: " + query);
		try {			
			connection = JDBCMySQLConnection.getConnection();
			statement = connection.createStatement();
			rs = statement.executeQuery(query);
			
			while (rs.next()) {
				System.out.println(rs.getString("bookmark"));
				System.out.println(rs.getInt(1));
				System.out.println(rs.getString(2));
				System.out.println(rs.getString(3));
				System.out.println(rs.getTimestamp(4));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				try {
					connection.close();
					System.out.println("mysql connection closed");
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

	}
	public void addTestRow(String username, String bookmark){
		int response;
		Connection connection = null;
		Statement statement = null; 
		
		String query = "INSERT INTO test(username, bookmark, timestamp) VALUES ('" + username + "', '" + bookmark +"', + CURRENT_TIMESTAMP);";
		System.out.println("Building query: " + query);
		try {			
			connection = JDBCMySQLConnection.getConnection();
			statement = connection.createStatement();
			response = statement.executeUpdate(query);
			
			if(response == 0){
				System.out.println("Query returned nothing");
			} else {
				System.out.println("Insert successful!");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
