package com.bookmarks.servlet.user;

import com.bookmarks.servlet.common.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.bookmarks.servlet.mysql.JDBCMySQLConnection;
import com.bookmarks.servlet.snapshot.WebpageSnapshot;
import com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException;

public class UserUtils {
	public static String loginUser(String username, String password,
			boolean rememberMe) {
		System.out.println("attempting to log in " + username);
		Subject currentUser = SecurityUtils.getSubject();
		System.out.println("Authentication: " + currentUser.isAuthenticated());
		if (!currentUser.isAuthenticated()) {
			// collect user principals and credentials in a gui specific manner
			// such as username/password html form, X509 certificate, OpenID,
			// etc.
			// We'll use the username/password example here since it is the most
			// common.
			UsernamePasswordToken token = new UsernamePasswordToken(username,
					password);

			// this is all you have to do to support 'remember me' (no config -
			// built in!):
			token.setRememberMe(rememberMe);

			try {
				currentUser.login(token);
				System.out.println("User " + username
						+ " logged in successfully");
				return "User " + username + " logged in successfully";
				// if no exception, that's it, we're done!
			} catch (UnknownAccountException uae) {
				System.out.println("User account, " + username
						+ ", in not registered. Please Register an account.");
				uae.printStackTrace();
				return "User account, " + username
						+ ", in not registered. Please Register an account.";
			} catch (IncorrectCredentialsException ice) {
				System.out
						.println("password given does not match user account: "
								+ username + ". Please try again");
				ice.printStackTrace();
				return "password given does not match user account: "
						+ username + ". Please try again";
			} catch (LockedAccountException lae) {
				System.out
						.println("This account has been locked. Please contact the system administrator");
				lae.printStackTrace();
				return "This account has been locked. Please contact the system administrator";
			} catch (AuthenticationException ae) {
				System.out
						.println("An unexpected authentication condition occured");
				ae.printStackTrace();
				return "An unexpected authentication condition occured";
				// unexpected condition - error?
			}
		}
		System.out
				.println("Something bad happened. Contact System administrator");
		return "Something bad happened. Contact System administrator";
	}

	public static boolean registerUser(String username, String password) {
		int response;
		Connection connection = null;
		Statement statement = null;

		String query = "INSERT INTO users(username, password) VALUES ('"
				+ username + "', '" + password + "');";
		System.out.println("Building query: " + query);
		try {
			connection = JDBCMySQLConnection.getConnection();
			statement = connection.createStatement();
			response = statement.executeUpdate(query);

			if (response == 0) {
				System.out.println("Query returned nothing");
			} else {
				System.out.println("User registration successful, user "
						+ username + " added.");
				createBookmarkDatabase(username, connection);
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

	private static void createBookmarkDatabase(String username,
			Connection connection) throws SQLException {
		int response;
		Statement statement = null;
		String query = "CREATE TABLE IF NOT EXISTS userdb_"
				+ username
				+ " ( id INTEGER UNSIGNED NOT NULL, bookmarkname VARCHAR(255) NOT NULL, url VARCHAR(2083) NOT NULL, timesVisited varchar(255), lastVist TIMESTAMP DEFAULT CURRENT_TIMESTAMP, tags VARCHAR(2083))ENGINE=InnoDB DEFAULT CHARSET=latin1;";

		statement = connection.createStatement();
		response = statement.executeUpdate(query);

		System.out.println("Table for " + username + " created successfully");
	}

	public static int addBookmark(Object username, String bookmark) {
		int response;
		Connection connection = null;
		Statement statement = null;

		String query = "INSERT INTO bookmark_index" + " (url) VALUES ('"
					+ bookmark + "');";
	
		System.out.println("Building query: " + query);
		try {
			connection = JDBCMySQLConnection.getConnection();
			statement = connection.createStatement();
			response = statement.executeUpdate(query);

			if (response == 0) {
				System.out.println("Insert of " + bookmark + " for "
						+ username.toString() + " ran into an issue");
			} else {
				System.out.println("Successful addition of " + bookmark
						+ " for " + username.toString());
				int id = getBookmarkId(bookmark, statement);
				addUserBookmark(id, username.toString(), bookmark, statement);
				connection.close();
				return id;
			}

		} catch (MySQLSyntaxErrorException e) {
			e.printStackTrace();
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
		return -1;

	}
	
	public static String getBookmarkName(Object username, String bookmark) {
		Connection connection = null;
		Statement statement = null;

		String query = "SELECT * FROM userdb_" + username.toString() + " WHERE url='" + bookmark + "';";
	
		System.out.println("Building query: " + query);
		try {
			connection = JDBCMySQLConnection.getConnection();
			statement = connection.createStatement();
			ResultSet responseSet = statement.executeQuery(query);
			String bookmarkName = "";
			
			if (responseSet.next()) {
				bookmarkName = responseSet.getString(2);
				System.out.println("Returning bookmark name " + bookmarkName + " for "
						+ username.toString());
				return bookmarkName;
			} else {
				System.out.println("Unable to get bookmark name for " + bookmark);
				return "";
			}

		} catch (MySQLSyntaxErrorException e) {
			e.printStackTrace();
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
		return "";
	}

	private static int addUserBookmark(int id, String username,
			String bookmark, Statement statement) throws SQLException {
		URL page;
		String pageName = "";
		String query;
		try {
			page = new URL(bookmark);
			pageName = page.getHost();
			query = "INSERT INTO userdb_" + username
					+ " (id, bookmarkname, url) VALUES ('" + id + "', '"
					+ pageName + "', '" + bookmark + "');";
		} catch (MalformedURLException e) {
			e.printStackTrace();
			query = "INSERT INTO userdb_" + username
					+ " (id, bookmarkname, url) VALUES ('" + id + "', '"
					+ bookmark + "', '" + bookmark + "');";
		}

		System.out.println("Building query: " + query);

		int responseSet = statement.executeUpdate(query);

		if (responseSet == 1) {
			System.out.println("Successfully add bookmark " + bookmark
					+ " for user " + username);
			return responseSet;
		} else {
			System.out.println("Unable to add bookmark " + bookmark
					+ " to userdb for " + username);
			return responseSet;
		}

	}

	private static int getBookmarkId(String bookmark, Statement statement)
			throws SQLException {
		String query = "SELECT * FROM bookmark_index where `url`='" + bookmark
				+ "';";
		System.out.println("Building query: " + query);
		ResultSet responseSet = statement.executeQuery(query);

		if (responseSet.next()) {
			int id = responseSet.getInt(1);
			System.out.println("Successfully returned bookmarks for id " + id);
			return id;
		} else {
			System.out.println("No bookmark found in userdb_");
			return -1;
		}

	}

	public static String getBookmarks(Object username) {
		Connection connection = null;
		Statement statement = null;

		String query = "SELECT * FROM userdb_"
				+ username.toString() + ";";
		System.out.println("Building query: " + query);
		try {
			connection = JDBCMySQLConnection.getConnection();
			statement = connection.createStatement();
			ResultSet responseSet = statement.executeQuery(query);

			if (responseSet == null) {
				System.out.println("Returning bookmarks for user "
						+ username.toString() + " failed.");
			} else {
				/*
				 * while (responseSet.next()) { int id =
				 * responseSet.getInt("id"); String bookmark =
				 * responseSet.getString("bookmarkname"); String url =
				 * responseSet.getString("url"); //Date dateCreated =
				 * responseSet.getDate("Datetime");
				 * 
				 * System.out.format("%s, %s, %s \n", id, bookmark, url); }
				 */
				System.out.println("Successfully returned bookmarks for "
						+ username.toString());
				String jsonResults = Utils.convertResultSetToJSON(responseSet);
				System.out.println("Returning " + jsonResults);
				responseSet.close();
				statement.close();
				connection.close();
				return jsonResults;
			}

		} catch (MySQLSyntaxErrorException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
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
		return null;

	}

	public static String getBookmarkScreenshot(String bookmark) {
		Connection connection = null;
		Statement statement = null;

		String query = "SELECT * FROM bookmark_index WHERE url='" + bookmark + "';";
		System.out.println("Building query: " + query);
		try {
			connection = JDBCMySQLConnection.getConnection();
			statement = connection.createStatement();
			ResultSet responseSet = statement.executeQuery(query);
			String imgPath = "";
			
			if (responseSet.next()) {
				imgPath = responseSet.getString(4);
				responseSet.close();
			} 
			if(imgPath == null || imgPath.isEmpty()){
				int bookmarkId = UserUtils.getBookmarkId(bookmark, statement);
				imgPath = WebpageSnapshot.takeScreenshot(bookmark, "C:\\Users\\Seth\\git\\EnahncedBookmarks\\EnhancedBookmarks\\WebContent\\images\\"+ bookmarkId +".png");
			}
			return imgPath;

		} catch (MySQLSyntaxErrorException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
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
		return "";
	}

}
