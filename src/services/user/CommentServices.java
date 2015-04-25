package services.user;

import java.sql.SQLException;

import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import tools.db.DBCommentsTools;
import tools.db.DBFriendsTools;
import tools.db.DBSessionTools;
import tools.json.JSONErrorTools;

public class CommentServices {
	
	public static JSONObject AddComment(String key, String text) throws JSONException, SQLException {
		JSONObject o = new JSONObject();
		
		if (key == null) 
			return JSONErrorTools.JSONError("Invalid user login.", 1);
		if (!DBSessionTools.isConnectedWithKey(key))
			return JSONErrorTools.JSONError("User not connected.", 2);
		if (SessionServices.isTimeOut(key)) {
			DBSessionTools.deleteSession(key);
			return JSONErrorTools.JSONError("Connection Time Out", 6);
		}
		
		DBSessionTools.setTimestamp(key);
		
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
		
		if (key == null) 
			return JSONErrorTools.JSONError("Invalid user login.", 1);
		if (commentLogin == null) 
			return JSONErrorTools.JSONError("Invalid friend login.", 1);
		if (!DBSessionTools.isConnectedWithKey(key))
			return JSONErrorTools.JSONError("User not connected.", 2);
		if (SessionServices.isTimeOut(key)) {
			DBSessionTools.deleteSession(key);
			return JSONErrorTools.JSONError("Connection Time Out", 6);
		}
		if (!DBFriendsTools.friendshipExists(key, commentLogin)) {
			return JSONErrorTools.JSONError("You cannot see this user's comments.", 3);
		}
		
		DBSessionTools.setTimestamp(key);
		
		o.put("key", key);
		o.put("login", commentLogin);
		
		DBCursor cursor = DBCommentsTools.searchCommentByLogin(commentLogin);
		while (cursor.hasNext()) {
			DBObject searchResult = cursor.next();
			o.put("comment", searchResult);
		}
		
		return o;
	}
}
