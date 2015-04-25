package tools.json;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONOk {
	public static JSONObject JSONOkMessage (String message) {
		try {
			JSONObject o = new JSONObject ();
			o.put("User Created.", message);
			return o;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
}
