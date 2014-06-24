package com.bookmarks.servlet.common;

import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.util.Properties;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


//Source http://stackoverflow.com/questions/17160351/create-json-object-by-java-from-data-of-mysql
//Souce http://biercoff.blogspot.com/2013/11/nice-and-simple-converter-of-java.html
public class Utils {
	public static String convertResultSetToJSON(ResultSet resultSet)
			throws Exception {
		JSONArray jsonArray = new JSONArray();
		while (resultSet.next()) {
			int total_rows = resultSet.getMetaData().getColumnCount();
			JSONObject obj = new JSONObject();
			//System.out.println("Total Rows: " + total_rows);
			for (int i = 0; i < total_rows -1; i++) {
				String columnName = resultSet.getMetaData().getColumnName(i + 1);
				//System.out.println("Column Name: " + columnName);
				
				//only checks to see if field is named Timestamp or Date to prevent error "java.sql.SQLException: Value '0000-00-00 00:00:00' can not be represented as java.sql.Timestamp" 
				if (!columnName.equals("Timestamp") && !columnName.equals("Date")) {
					String row = resultSet.getString(i + 1);
					
					String content = (row != null) ? row : "";
					if (content != null) {
						System.out.println("output: " + content);
					}
					obj.put(resultSet.getMetaData().getColumnLabel(i + 1)
							.toLowerCase(), content);
				} else {
					obj.put(resultSet.getMetaData().getColumnLabel(i + 1)
							.toLowerCase(), "");
				}
			}
			jsonArray.add(obj);
		}
		String output = jsonArray.toJSONString();
		return output;
	}
	//String propertiesFile
	public void loadPropertiesFile(){
		try {
			String propertiesFile = "config.properties";
			Properties p = new Properties();
			InputStream in = this.getClass().getResourceAsStream(propertiesFile);
			p.load(in);
			System.out.println("Properties file: " + propertiesFile + " loaded.");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
