package tools.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBProfileTools {
	
	public static void createProfile (String login, String firstname, String lastname) throws SQLException {
		try {
			Connection c = Database.getMySQLConnection();
			Statement s = c.createStatement();
			String q = "insert into user_profile(login, firstname, lastname) values ('" + login + "','" + firstname + "', '" + lastname + "');";
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
	
	public static void closeProfile (String key) throws SQLException {
		try {
			String login = DBProfileTools.LoginFromKey(key);
			Connection c = Database.getMySQLConnection();
			Statement s = c.createStatement();
			String q = "update user_profile set closed = 1 where login = '" + login + "';";
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
}
