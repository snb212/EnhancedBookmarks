package com.bookmarks.servlet.common;

import com.bookmarks.servlet.user.*;
import com.bookmarks.servlet.snapshot.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

/**
 * Servlet implementation class BookmarkService
 */
@WebServlet(urlPatterns = {"/BookmarkService"}, name = "BookmarkService")
public class BookmarkService extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static String IMAGE_PATH = "";
	public static String PHANTOMJSHOME = "";
	public static String PHANTOMRASTERIZE = "";
	public static String IMAGE_RESIZER = "";
	public static String MYSQL_URL = "";
	public static String MYSQL_USER = "";
	public static String MYSQL_PASSWORD = "";

    /**
     * @see HttpServlet#HttpServlet()
     */
    public BookmarkService() {
        super();
        System.out.println("Servlet Started");
        String propertiesFilePath = "/";
        System.out.println("Attempting to load Properties file: config.properties" );
        loadProperties();
    }
    
    public void loadProperties(){
		try {
			String propertiesFile = "config.properties";
			Properties properties = new Properties();
			InputStream in = this.getClass().getResourceAsStream(propertiesFile);
			properties.load(in);
			for(String key : properties.stringPropertyNames()) {
				  String value = properties.getProperty(key);
				  
				  switch(key) {
				  	case "image-path": IMAGE_PATH = value; 
				  		break;
				  	case "phantomjsHome": PHANTOMJSHOME = value; 
			  		break;
				  	case "phantomRasterize": PHANTOMRASTERIZE = value; 
			  		break;
				  	case "image-resizer": IMAGE_RESIZER = value; 
			  		break;
				  	case "mysql-url": MYSQL_URL = value; 
			  		break;
				  	case "mysql-user": MYSQL_USER = value; 
			  		break;
				  	case "mysql-password": MYSQL_PASSWORD = value; 
			  		break;				  
				  }
				  System.out.println(key + " => " + value);
				}
			System.out.println("IMAGE_PATH:" + IMAGE_PATH);
			System.out.println("Properties file: " + propertiesFile + " loaded.");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

		String function = request.getParameter("function");
		//System.out.println("Called function: " + function);
		PrintWriter out = response.getWriter();
		
		if(function.equals("getUsername")){
			Subject currentUser = SecurityUtils.getSubject();
			if(currentUser.getPrincipal() != null){
				out.println(currentUser.getPrincipal());
			}
		}
		else if(function.equals("registerUser")){
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			UserUtils.registerUser(username, password);
		}
		else if(function.equals("loginUser")){
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			String rememberMe = request.getParameter("rememberMe");
			boolean rememberMeBool = (rememberMe.equals("true")) ? true : false;
			String message = UserUtils.loginUser(username, password, rememberMeBool);
			out.println(message);
		}
		else if(function.equals("logout")){
			Subject currentUser = SecurityUtils.getSubject();
			Object user = currentUser.getPrincipal();
			currentUser.logout();
			System.out.println(user + " has been logged out");
			out.println("Logout Successful");
		}
		else if(function.equals("addBookmark")){
			String bookmark= request.getParameter("bookmark");
			Subject currentUser = SecurityUtils.getSubject();
			int bookmarkId = UserUtils.addBookmark(currentUser.getPrincipal(), bookmark);
			String bookmarkName = UserUtils.getBookmarkName(currentUser.getPrincipal(), bookmark); 
			System.out.println("Bookmark " + bookmark + "added successfully.");
			out.println("Bookmark added");
			//String screenPath = WebpageSnapshot.takeScreenshot(bookmark, "C:\\Users\\Seth\\git\\EnahncedBookmarks\\EnhancedBookmarks\\WebContent\\images\\"+bookmarkName+".png");
//			WebpageSnapshot.takeScreenshot(bookmark, "C:\\Compiled Java Resources\\phantomjs-screenshots\\"+bookmarkName+".png");
		} 
		else if(function.equals("getBookmarks")){
			Subject currentUser = SecurityUtils.getSubject();
			String bookmarks = UserUtils.getBookmarks(currentUser.getPrincipal());
			System.out.println("Bookmarks returned successfully: " + bookmarks);
			out.println(bookmarks);
		} else if(function.equals("getBookmarkImage")){
			String bookmark = request.getParameter("bookmark");
			String imgPath = UserUtils.getBookmarkScreenshot(bookmark);
			out.println(imgPath);
		}
		else {
		out.println("No associated commands found for: " + request.getRequestURL());
		}
		out.close();
	}

}
