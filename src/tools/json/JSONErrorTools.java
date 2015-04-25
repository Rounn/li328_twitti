package tools.json;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 
 * @author Rani
 *
 */

public class JSONErrorTools {
	/**
	 * 
	 * @param message
	 * @param code
	 * @return
	 */
	public static JSONObject JSONError (String message, int code) {
		try {
			JSONObject o = new JSONObject ();
			o.put("error", message);
			o.put("error_code", code);
			return o;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
}
