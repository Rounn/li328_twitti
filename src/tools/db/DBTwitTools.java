package tools.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DBTwitTools {
	public static void createTwit (String ulogin, String message) throws SQLException {
		try {
			Connection c = Database.getMySQLConnection();
			Statement s = c.createStatement();
			String q = "insert into twits(ulogin, message) values ('" + ulogin + "', '" + message + "');";
			s.executeUpdate(q);
			
			s.close();
			c.close();
		} catch (SQLException e) {
			throw new SQLException("Twit failed :(" + e.getMessage());
			
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

}
