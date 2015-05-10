package services.admin;

import org.json.JSONException;
import org.json.JSONObject;

import tools.db.Database;

import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class AdminCommentServices {
	
	public static JSONObject showAllComments () throws JSONException {
		DBCollection coll = Database.getMongoCollection("comments");
		DBCursor cursor = coll.find();
		JSONObject o = new JSONObject();
		
		int i = 0;
		while (cursor.hasNext()) {
			i++;
			DBObject searchResult = cursor.next();
			o.put("comment"+i, searchResult);
		}
		return o;
	}

}
