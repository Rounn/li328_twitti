package services.user;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import tools.db.DBSessionTools;
import tools.db.DBUserTools;
import tools.db.Database;
import tools.json.JSONErrorTools;

public class SessionServices {

	/**
	 * 
	 * @param key
	 * @return
	 * @throws JSONException
	 * @throws SQLException
	 */
	public static JSONObject disconnect(String key) throws JSONException, SQLException {
		JSONObject o = new JSONObject();		
		if (key == null) 
			return JSONErrorTools.JSONError("Invalid login.", 1);
		if (!DBSessionTools.isConnectedWithKey(key))
			return JSONErrorTools.JSONError("User is not connected or does not exist.", 2);
		
		if (SessionServices.isTimeOut(key)) {
			DBSessionTools.deleteSession(key);
			return JSONErrorTools.JSONError("User already disconnected.", 2);
		}
		
		DBSessionTools.deleteSession(key);
		o.put("key", key);
		return o;
	}
	
	public static boolean isTimeOut(String key) throws JSONException, SQLException {
		long timestamp = DBSessionTools.getTimestamp(key);
		Date date = new Date();
		if (date.getTime() > (1200000 + timestamp))
			return true;
		return false;
	}
	
	
	
}
