package tools.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

public class DBCommentsTools {
	
	/**
	 * @param key
	 * @param text
	 * @throws SQLException
	 */
	public static void addComment (String key, String text) throws SQLException {
		DB dbs = Database.getMongoDB();
		DBCollection comments = Database.getMongoCollection("comments");
		BasicDBObject comment = new BasicDBObject();
		String login = DBCommentsTools.LoginFromKey(key);
		
		comment.put("login", login);
		comment.put("comment", text);
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		comment.put("date", dateFormat.format(date));
		BasicDBObject likes = new BasicDBObject();
		comment.put("likes", likes);

		comments.insert(comment);
	}
	
	public static DBObject searchCommentByLoginAndDate (String login, String date) {
		DB dbs = Database.getMongoDB();
		DBCollection comments = Database.getMongoCollection("comments");
		
		BasicDBObject searchItem = new BasicDBObject();
		searchItem.put("login", login);
		searchItem.put("date", date);
		DBObject comment = comments.findOne(searchItem);
		
		return comment;
	}
	
	public static DBCursor searchCommentByLogin (String login) {
		DB dbs = Database.getMongoDB();
		DBCollection comments = Database.getMongoCollection("comments");
		
		BasicDBObject searchItem = new BasicDBObject();
		searchItem.put("login", login);
		
		DBCursor cursor = comments.find(searchItem);
		
		return cursor;
	}
	
	public static boolean isLikedByLiker (DBObject searchItem, String likerKey) throws SQLException {
		String likerLogin = DBCommentsTools.LoginFromKey(likerKey);
		
		DB dbs = Database.getMongoDB();
		DBCollection comments = Database.getMongoCollection("comments");
		searchItem.put("likes.login", likerLogin);
		
		DBCursor cursor = comments.find(searchItem);
		
		if (cursor.hasNext())
			return true;
		return false;
	}
	
	public static void likeComment (DBObject searchItem, String likerKey) throws SQLException {
		String likerLogin = DBCommentsTools.LoginFromKey(likerKey);
		
		DB dbs = Database.getMongoDB();
		DBCollection comments = Database.getMongoCollection("comments");
		
		BasicDBObject updateItem = new BasicDBObject();
		BasicDBObject like = new BasicDBObject();
		like.put("login", likerLogin);
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date likeDate = new Date();
		like.put("date", dateFormat.format(likeDate));
		
		updateItem.put("$set", "{likes:" + like +"}");
		
		comments.update(searchItem, updateItem, true, false);
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
