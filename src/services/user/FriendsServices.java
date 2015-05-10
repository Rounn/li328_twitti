package services.user;

import java.sql.SQLException;

import org.json.JSONException;
import org.json.JSONObject;

import tools.db.DBFriendsTools;
import tools.db.DBSessionTools;
import tools.db.DBUserTools;
import tools.json.JSONErrorTools;

/**
 * 
 * @author Rani
 *
 */
public class FriendsServices {
	
	/**
	 * @param login
	 * @return
	 * @throws JSONException
	 * @throws SQLException
	 */
	public static JSONObject addFriend(String ukey, String flogin) throws JSONException, SQLException {
		JSONObject o = new JSONObject();	
		
		DBSessionTools.setTimestamp(ukey);
		DBFriendsTools.addFriendship(ukey, flogin);
		o.put("ukey", ukey);
		o.put("flogin", flogin);
		return o;
	}
	
	/**
	 * @param ukey
	 * @param flogin
	 * @return
	 * @throws JSONException
	 * @throws SQLException
	 */
	public static JSONObject removeFriend(String ukey, String flogin) throws JSONException, SQLException {
		JSONObject o = new JSONObject();		
		if (ukey == null) 
			return JSONErrorTools.JSONError("Invalid user login.", 1);
		if (flogin == null) 
			return JSONErrorTools.JSONError("Invalid friend login", 4);
		if (!DBSessionTools.isConnectedWithKey(ukey))
			return JSONErrorTools.JSONError("User does not exist.", 2);
		if (!DBUserTools.UserExists(flogin))
			return JSONErrorTools.JSONError("Friend User do not exist.", 5);
		if (SessionServices.isTimeOut(ukey)) {
			DBSessionTools.deleteSession(ukey);
			return JSONErrorTools.JSONError("Connection Time Out", 3);
		}
		if (DBFriendsTools.onSelfOperation(ukey, flogin))
			return JSONErrorTools.JSONError("Cannot remove itself", 6);
		if (!DBFriendsTools.friendshipExists(ukey, flogin))
			return JSONErrorTools.JSONError("Friendship does not exist", 6);
		DBSessionTools.setTimestamp(ukey);
		DBFriendsTools.removeFriendship(ukey, flogin);
		o.put("ukey", ukey);
		o.put("flogin", flogin);
		return o;
	}
}
