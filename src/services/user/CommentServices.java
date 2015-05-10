package services.user;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bson.types.ObjectId;
import org.json.JSONException;
import org.json.JSONObject;

import tools.db.DBCommentsTools;
import tools.db.DBSessionTools;
import tools.db.Database;
import tools.json.JSONErrorTools;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoException;

public class CommentServices {
	
	public static JSONObject addComment(String key, String text) throws JSONException, SQLException {
		JSONObject o = new JSONObject();
		DBCommentsTools.addComment(key, text);
		
		o.put("key", key);
		o.put("text", text);
		return o;
	}
	
	public static JSONObject likeComment(String likerKey, String commentLogin, String commentDate) throws JSONException, SQLException {
		JSONObject o = new JSONObject();
		
		if (likerKey == null) 
			return JSONErrorTools.JSONError("Invalid user login.", 1);
		if (!DBSessionTools.isConnectedWithKey(likerKey))
			return JSONErrorTools.JSONError("User not connected.", 2);
		if (SessionServices.isTimeOut(likerKey)) {
			DBSessionTools.deleteSession(likerKey);
			return JSONErrorTools.JSONError("Connection Time Out", 6);
		}
		
		DBSessionTools.setTimestamp(likerKey);
		
		DBObject searchItem = DBCommentsTools.searchCommentByLoginAndDate(commentLogin, commentDate);
		if (DBCommentsTools.isLikedByLiker(searchItem, likerKey)) 
			return JSONErrorTools.JSONError("Comment already liked by user", 7);
		
		DBCommentsTools.likeComment(searchItem, likerKey);
		
		o.put("key", likerKey);
		o.put("login", commentLogin);
		return o;
	}
	
	public static JSONObject searchComment (String key, String commentLogin) throws SQLException, JSONException {
		JSONObject o = new JSONObject();		
		DBSessionTools.setTimestamp(key);
		
		o.put("key", key);
		o.put("login", commentLogin);
		
		DBCursor cursor = DBCommentsTools.searchCommentByLogin(commentLogin);
		int i = 0;
		while (cursor.hasNext()) {
			i++;
			DBObject searchResult = cursor.next();
			o.put("comment"+i, searchResult);
		}
		
		return o;
	}
	
	/**
	 * Service permettant de rechercher les commentaires grâce à l'indice de pertinence du mot recherché.
	 * Les scores de pertinences proviennent de la table stockée dans la base SQL: word_rsv
	 * @param query
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws SQLException
	 * @throws MongoException
	 * @throws JSONException
	 */
	public static JSONObject searchEnginForComments (String query) throws InstantiationException, IllegalAccessException, SQLException, MongoException, JSONException {
		JSONObject o = new JSONObject();
		Connection c = Database.getMySQLConnection();
		Statement s = c.createStatement();
		String q = "select * from word_RSV where word = '" + query + "' ORDER BY rsv DESC;";
		s.executeQuery(q);
		ResultSet rs = s.getResultSet();
		
		DB dbs = Database.getMongoDB();
		DBCollection comments = Database.getMongoCollection("comments");
		
		int i = 0;
		while (rs.next()) {
			i++;
			String id = rs.getString("doc");
			ObjectId oId = new ObjectId(id);
			BasicDBObject searchItem = new BasicDBObject();
			searchItem.put("_id", oId);
			DBCursor cursor = comments.find(searchItem);
			o.put(""+i, cursor.next());
		}
		rs.close();
		s.close();
		c.close();
		
		return o;
	}
	
}
