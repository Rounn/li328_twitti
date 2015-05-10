package tools.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.JSONException;
import org.json.JSONObject;

public class DBFriendsTools {
	
	public static void addFriendship (String ukey, String flogin) throws SQLException {
		try {
			String ulogin = DBFriendsTools.LoginFromKey(ukey);
			Connection c = Database.getMySQLConnection();
			Statement s = c.createStatement();
			String q = "insert into friendships(ulogin, flogin) values ('" + ulogin + "', '" + flogin + "');";
			s.executeUpdate(q);
			
			s.close();
			c.close();
		} catch (SQLException e) {
			throw new SQLException("friendship failed : " + e.getMessage());
			
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	public static JSONObject showFriends (String key) throws SQLException, InstantiationException, IllegalAccessException, JSONException {
		JSONObject o = new JSONObject();
		String ulogin = DBFriendsTools.LoginFromKey(key);
		Connection c = Database.getMySQLConnection();
		Statement s = c.createStatement();
		String q = "select * from friendships where ulogin = '" + ulogin + "';";
		s.executeQuery(q);
		
		ResultSet rs = s.getResultSet();
		int i = 0;
		while (rs.next()) {
			i++;
			o.put(""+i, rs.getString("flogin"));
		}
		return o;
	}
	
	public static boolean onSelfOperation (String ukey, String flogin) throws SQLException {
		if (flogin == DBFriendsTools.LoginFromKey(ukey))
			return true;
		else
			return false;
	}
	
	public static boolean friendshipExists (String ukey, String flogin) throws SQLException {
		try {
			String ulogin = DBFriendsTools.LoginFromKey(ukey);
			Connection c = Database.getMySQLConnection();
			Statement s = c.createStatement();
			String q = "Select * from friendships where ulogin ='" + ulogin + "' AND flogin = '" + flogin + "';";
			s.executeQuery(q);
			ResultSet rs = s.getResultSet();
			boolean bool = rs.next();
			
			rs.close();
			s.close();
			c.close();
			return bool;
		} catch (SQLException e) {
			throw new SQLException("friendship failed : " + e.getMessage());
			
		} catch (InstantiationException e) {
			e.printStackTrace();
			return false;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static void removeFriendship (String ukey, String flogin) throws SQLException {
		try {
			String ulogin = DBFriendsTools.LoginFromKey(ukey);
			Connection c = Database.getMySQLConnection();
			Statement s = c.createStatement();
			String q = "delete from friendships where ulogin = '" + ulogin + "' AND flogin = '" + flogin + "';";
			s.executeUpdate(q);
			
			s.close();
			c.close();
		} catch (SQLException e) {
			throw new SQLException("removing friendship failed : " + e.getMessage());
			
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	public static void removeFriendshipWhenDelete (String key) throws SQLException {
		try {
			String ulogin = DBFriendsTools.LoginFromKey(key);
			Connection c = Database.getMySQLConnection();
			Statement s = c.createStatement();
			String q = "delete from friendships where ulogin = '" + ulogin + "' OR flogin = '" + ulogin + "';";
			s.executeUpdate(q);
			
			s.close();
			c.close();
		} catch (SQLException e) {
			throw new SQLException("removing friendship failed : " + e.getMessage());
			
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
