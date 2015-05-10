package tools.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoException;

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
	/**
	 * Renvoie tous les comment des friends de l'utilisateur dans un objet JSON.
	 * @param key
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws SQLException
	 * @throws JSONException 
	 * @throws MongoException 
	 */
	public static ArrayList<DBCursor> showFriendsComments (String key) throws InstantiationException, IllegalAccessException, SQLException, MongoException, JSONException {
		//JSONObject o = new JSONObject();
		BasicDBObject o = new BasicDBObject ();
		JSONArray a = new JSONArray();
		ArrayList <DBCursor> array = new ArrayList<DBCursor> ();
		
		DB dbs = Database.getMongoDB();
		DBCollection comments = Database.getMongoCollection("comments");
		BasicDBObject searchItem = new BasicDBObject();
		
		Connection c = Database.getMySQLConnection();
		Statement s = c.createStatement();
		String ulogin = DBCommentsTools.LoginFromKey(key);
		String q = "select * from friendships where ulogin = '" + ulogin + "'" ;
		s.executeQuery(q);
		ResultSet rs = s.getResultSet();
		
		while (rs.next()) {
			String login = rs.getString("flogin");
			searchItem.put("login", login);
			DBCursor cursor = comments.find(searchItem);
			//int i = 0;
			array.add(cursor);
			
			
			/*while (cursor.hasNext()) {
				i++;
				o.put(i, cursor.)
				//o.put(""+i, cursor.next());
			}*/
		}
		rs.close();
		s.close();
		c.close();
		
		return array;		
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
