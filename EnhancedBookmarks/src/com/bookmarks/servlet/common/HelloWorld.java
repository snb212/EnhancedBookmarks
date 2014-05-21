package com.bookmarks.servlet.common;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;

import com.bookmarks.servlet.mysql.JDBCMySQLConnection;

/**
 * Servlet implementation class HelloWorld
 */
@WebServlet(urlPatterns = {"/HelloWorld"}, name = "HelloWorld")
public class HelloWorld extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private BookmarkService bookmarkService = new BookmarkService();
    /**
     * @see HttpServlet#HttpServlet()
     */
    public HelloWorld() {
        super();
        System.out.println("Servlet Started");
        Connection mysqlConnection = JDBCMySQLConnection.getConnection();
        //Connection attemptingConnection = mysqlConnection;
        
        //create bookmark service object
        
        //authentication 
        /*Factory<org.apache.shiro.mgt.SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
        System.out.println("Factory:" + factory.toString());
        SecurityManager securityManager = (SecurityManager) factory.getInstance();
        SecurityUtils.setSecurityManager((org.apache.shiro.mgt.SecurityManager) securityManager);*/
        
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter out = response.getWriter();
    	out.println("Hello World");
    	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String username = request.getParameter("username");
		String bookmark = request.getParameter("bookmark");
		String function = request.getParameter("function");
		PrintWriter out = response.getWriter();
		
		if(username != null && bookmark != null){
			System.out.println("username: " + username + " | bookmark: " + bookmark);
			bookmarkService.addTestRow(username, bookmark);
			out.println(username);
			out.println(bookmark);
		}
		
		if(function.equals("getUsername")){
			Subject currentUser = SecurityUtils.getSubject();
			out.println(currentUser.getSession());	
		}
		if(function.equals("registerUser")){
			//String username = request.getParameter("username");
			String password = request.getParameter("password");
			registerUser(username, password);
		}
		
		out.close();
	}

	private boolean registerUser(String username, String password) {
		int response;
		Connection connection = null;
		Statement statement = null; 
		
		String query = "INSERT INTO users(username, password) VALUES ('" + username + "', '" + password +"');";
		System.out.println("Building query: " + query);
		try {			
			connection = JDBCMySQLConnection.getConnection();
			statement = connection.createStatement();
			response = statement.executeUpdate(query);
			
			if(response == 0){
				System.out.println("Query returned nothing");
			} else {
				System.out.println("User registration sucessful, user " + username + " added." );
				connection.close();
				return true;
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
		return false;
		
	}

}
