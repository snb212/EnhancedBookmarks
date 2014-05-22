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
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
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
		//String username = request.getParameter("username");
		//String bookmark = request.getParameter("bookmark");
		String function = request.getParameter("function");
		System.out.println("Called function: " + function);
		PrintWriter out = response.getWriter();
		
//		if(username != null && bookmark != null){
//			String username = request.getParameter("username");
//			String bookmark = request.getParameter("bookmark");
//			System.out.println("username: " + username + " | bookmark: " + bookmark);
//			bookmarkService.addTestRow(username, bookmark);
//			out.println(username);
//			out.println(bookmark);
//		} 
		
		if(function.equals("getUsername")){
			Subject currentUser = SecurityUtils.getSubject();
			if(currentUser.getPrincipal() != null){
				out.println(currentUser.getPrincipal());
			}
		}
		else if(function.equals("registerUser")){
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			registerUser(username, password);
		}
		else if(function.equals("loginUser")){
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			String rememberMe = request.getParameter("rememberMe");
			boolean rememberMeBool = (rememberMe.equals("true")) ? true : false;
			String message = loginUser(username, password, rememberMeBool);
			out.println(message);
		}
		else if(function.equals("logout")){
			Subject currentUser = SecurityUtils.getSubject();
			Object user = currentUser.getPrincipal();
			currentUser.logout();
			System.out.println(user + " has been logged out");
			out.println("Logout Successful");
		}
		else {
		out.println("No associated commands found for: " + request.getRequestURL());
		}
		out.close();
	}

	private String loginUser(String username, String password, boolean rememberMe) {
		System.out.println("attempting to log in " + username);
		Subject currentUser = SecurityUtils.getSubject();
		System.out.println("Authentication: " + currentUser.isAuthenticated());
		if ( !currentUser.isAuthenticated() ) {
		    //collect user principals and credentials in a gui specific manner 
		    //such as username/password html form, X509 certificate, OpenID, etc.
		    //We'll use the username/password example here since it is the most common.
		    UsernamePasswordToken token = new UsernamePasswordToken(username, password);

		    //this is all you have to do to support 'remember me' (no config - built in!):
		    token.setRememberMe(rememberMe);
		    
		    try {
		    	currentUser.login(token);
		    	System.out.println("User "+username+" logged in successfully");
			    return "User "+username+" logged in successfully";
		        //if no exception, that's it, we're done!
		    } catch ( UnknownAccountException uae ) {
		    	System.out.println("User account, "+username+", in not registered. Please Register an account.");
		    	return "User account, "+username+", in not registered. Please Register an account.";
		    } catch ( IncorrectCredentialsException ice ) {
		    	System.out.println("password given does not match user account: " + username + ". Please try again");
		        return "password given does not match user account: " + username + ". Please try again";
		    } catch ( LockedAccountException lae ) {
		    	System.out.println("This account has been locked. Please contact the system administrator");
		        return "This account has been locked. Please contact the system administrator";
		    } catch ( AuthenticationException ae ) {
		    	System.out.println("An unexpected authentication condition occured");
		    	return "An unexpected authentication condition occured";
		        //unexpected condition - error?
		    }
		}
		System.out.println("Something bad happened. Contact System administrator");
		return "Something bad happened. Contact System administrator";
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
				System.out.println("User registration successful, user " + username + " added." );
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
