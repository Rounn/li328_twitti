package tools.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 
 * @author Rani
 *
 */

public class DBUserTools {
	
	/**
	 * @param login
	 * @param password
	 * @throws SQLException
	 */
	public static void createUser (String login, String password) throws SQLException {
		try {
			Connection c = Database.getMySQLConnection();
			Statement s = c.createStatement();
			String q = "insert into users(login, passwrd) values ('" + login + "', '" + password + "');";
			s.executeUpdate(q);
			
			s.close();
			c.close();
		} catch (SQLException e) {
			throw new SQLException("SQLException : " + e.getMessage());
			
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @param login
	 * @throws SQLException
	 */
	public static void deleteUser(String key) throws SQLException{
		try {
			String login = DBUserTools.LoginFromKey(key);
			Connection c = Database.getMySQLConnection();
			Statement s = c.createStatement();
			String q = "delete from users where login = '" + login + "'";
			s.executeUpdate(q);
			
			s.close();
			c.close();
		} catch (SQLException e) {
			throw new SQLException("Unable to delete user : " + e.getMessage());
			
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @param login
	 * @return
	 * @throws SQLException 
	 */
	public static boolean UserExists (String login) throws SQLException {
		try {
			Connection c = Database.getMySQLConnection();
			Statement s = c.createStatement();
			String q = "select * from users u where u.login = '" + login + "';";
			s.executeQuery(q);
			ResultSet rs = s.getResultSet();
			boolean bool = rs.next();
			
			rs.close();
			s.close();
			c.close();
			return bool;
		} catch (SQLException e) {
			throw new SQLException("user does not exist" + e.getMessage());
			
		} catch (InstantiationException e) {
			e.printStackTrace();
			return false;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean correctPassword (String login, String password) throws SQLException {
		try {
			Connection c = Database.getMySQLConnection();
			Statement s = c.createStatement();
			String q = "select * from users u where u.login = '" + login + "' AND u.passwrd = '" + password + "';";
			s.executeQuery(q);
			ResultSet rs = s.getResultSet();
			boolean bool = rs.next();
			
			rs.close();
			s.close();
			c.close();
			return bool;
		} catch (SQLException e) {
			throw new SQLException("user not exist" + e.getMessage());
			
		} catch (InstantiationException e) {
			e.printStackTrace();
			return false;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private static String LoginFromKey(String key) throws SQLException {
		try {
			Connection c = Database.getMySQLConnection();
			Statement s = c.createStatement();
			String q = "select login from sessions where clef = '" + key + "';";
			s.executeQuery(q);
			ResultSet rs = s.getResultSet();
			rs.next();
			String login = rs.getString("login");
			
			rs.close();
			s.close();
			c.close();
			return login;
		} catch (SQLException e) {
			throw new SQLException("key already exists" + e.getMessage());
			
		} catch (InstantiationException e) {
			e.printStackTrace();
			return null;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 
	 * @param login
	 * @return
	 * @throws SQLException
	 */
	public static void disconnect (String login) throws SQLException {
		try {
			Connection c = Database.getMySQLConnection();
			Statement s = c.createStatement();
			//String q = "select * from connected c where c.login = '" + login + "';";
			String q = "";
			//TODO Faire un remove row
			s.executeUpdate(q);
			
			s.close();
			s.close();
			c.close();
		} catch (SQLException e) {
			throw new SQLException("User not connected" + e.getMessage());
			
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
}
