package com.bookmarks.servlet.common;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import com.bookmarks.servlet.mysql.JDBCMySQLConnection;

/**
 * Servlet implementation class HelloWorld
 */
@WebServlet("/HelloWorld")
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
		System.out.println("username: " + username + " | bookmark: " + bookmark);
		
		bookmarkService.addTestRow(username, bookmark);
		
		Subject currentUser = SecurityUtils.getSubject();
		
		PrintWriter out = response.getWriter();
		out.println(username);
		out.println(bookmark);
		out.println(currentUser);
		out.close();
	}

}
