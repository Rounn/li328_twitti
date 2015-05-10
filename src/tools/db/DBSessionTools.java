package tools.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

public class DBSessionTools {

	public static void createSession(String login, String clef) throws SQLException{
		try {
			Connection c = Database.getMySQLConnection();
			Statement s = c.createStatement();
			//TODO change null to date
			Date date = new Date();
			long time_stamp = date.getTime();
			String q = "insert into sessions(login, clef, sessiontimestamp) values ('" + login + "', '" + clef + "', '" + time_stamp + "');";
			s.executeUpdate(q);
			
			s.close();
			c.close();
		} catch (SQLException e) {
			throw new SQLException("Unable to create session" + e.getMessage());
			
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	public static void deleteSession(String key) throws SQLException{
		try {
			Connection c = Database.getMySQLConnection();
			Statement s = c.createStatement();
			String q = "delete from sessions where clef = '" + key + "'";
			s.executeUpdate(q);
			
			s.close();
			c.close();
		} catch (SQLException e) {
			throw new SQLException("Unable to delete session : " + e.getMessage());
			
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	public static void deleteSessionWithLogin(String login) throws SQLException{
		try {
			Connection c = Database.getMySQLConnection();
			Statement s = c.createStatement();
			String q = "delete from sessions where login = '" + login + "'";
			s.executeUpdate(q);
			
			s.close();
			c.close();
		} catch (SQLException e) {
			throw new SQLException("Unable to delete session : " + e.getMessage());
			
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 * @throws SQLException
	 */
	public static boolean isConnectedWithKey (String key) throws SQLException {
		try {
			Connection c = Database.getMySQLConnection();
			Statement s = c.createStatement();
			String q = "select * from sessions where clef = '" + key + "';";
			s.executeQuery(q);
			ResultSet rs = s.getResultSet();
			boolean bool = rs.next();
			
			rs.close();
			s.close();
			c.close();
			return bool;
		} catch (SQLException e) {
			throw new SQLException("User not connected : " + e.getMessage());
		} catch (InstantiationException e) {
			e.printStackTrace();
			return false;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean isConnectedWithLogin (String login) throws SQLException {
		try {
			Connection c = Database.getMySQLConnection();
			Statement s = c.createStatement();
			String q = "select * from sessions s where s.login = '" + login + "';";
			s.executeQuery(q);
			ResultSet rs = s.getResultSet();
			boolean bool = rs.next();
			
			rs.close();
			s.close();
			c.close();
			return bool;
		} catch (SQLException e) {
			throw new SQLException("User not connected : " + e.getMessage());
		} catch (InstantiationException e) {
			e.printStackTrace();
			return false;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static long getTimestamp(String key) throws SQLException {
		try {
			Connection c = Database.getMySQLConnection();
			Statement s = c.createStatement();
			String q = "select * from sessions where clef = '" + key + "';";
			s.executeQuery(q);
			ResultSet rs = s.getResultSet();
			rs.next();
			long timestamp = rs.getLong("sessiontimestamp");
			
			rs.close();
			s.close();
			c.close();
			return timestamp;
		} catch (SQLException e) {
			throw new SQLException("Session TimeStamp not found: " + e.getMessage());
			
		} catch (InstantiationException e) {
			e.printStackTrace();
			return 0;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	public static void setTimestamp(String key) throws SQLException {
		try {
			Connection c = Database.getMySQLConnection();
			Statement s = c.createStatement();
			Date date = new Date();
			long timestamp = date.getTime();
			String q = "update sessions set sessiontimestamp = " + timestamp + " where clef = '" + key + "';";
			s.executeUpdate(q);
			
			s.close();
			c.close();
		} catch (SQLException e) {
			throw new SQLException("Session TimeStamp not found: " + e.getMessage());
			
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	public static String KeyFromLogin(String login) throws SQLException {
		try {
			Connection c = Database.getMySQLConnection();
			Statement s = c.createStatement();
			String q = "select s.clef from sessions s where s.login = '" + login + "';";
			s.executeQuery(q);
			ResultSet rs = s.getResultSet();
			String key = rs.getString(1);
			//boolean bool = rs.next();
			
			rs.close();
			s.close();
			c.close();
			return key;
		} catch (SQLException e) {
			throw new SQLException("key not found" + e.getMessage());
			
		} catch (InstantiationException e) {
			e.printStackTrace();
			return null;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}
	}
}
