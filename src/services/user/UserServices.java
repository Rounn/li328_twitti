package services.user;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import tools.TwittiKeyGenerator;
import tools.db.DBFriendsTools;
import tools.db.DBProfileTools;
import tools.db.DBSessionTools;
import tools.db.DBUserTools;
import tools.json.JSONErrorTools;

/**
 * 
 * @author Rani
 *
 */

public class UserServices {
	
	/**
	 * @param login
	 * @param password
	 * @return
	 * @throws JSONException
	 * @throws SQLException
	 */
	public static JSONObject create(String login, String password, String firstname, String lastname) throws JSONException, SQLException {
		JSONObject o = new JSONObject();		
		DBUserTools.createUser(login, password);
		DBProfileTools.createProfile(login, firstname, lastname);
		
		o.put("login", login);
		o.put("password", password);
		return o;
	}
	
	/**
	 * @param login
	 * @param password
	 * @return
	 * @throws JSONException
	 * @throws SQLException 
	 * @throws UnsupportedEncodingException 
	 * @throws NoSuchAlgorithmException 
	 */
	public static JSONObject connect(String login, String password) throws JSONException, SQLException, NoSuchAlgorithmException, UnsupportedEncodingException {
		JSONObject o = new JSONObject();	
		String key =TwittiKeyGenerator.generateKey(password);
		DBSessionTools.createSession(login, key);
		
		o.put("key", key);
		return o;
	}
	
	/**
	 * Service de suppression d'un compte utilisateur.
	 * Son Login et Password sont supprimés de la base de donnée.
	 * Mais son profile et ses informations sont néanmoins préservés dans nos bases de données avec un flag closed, elle sont trop chères!
	 * @param key
	 * @return
	 * @throws JSONException
	 * @throws SQLException
	 */
	public static JSONObject delete(String key) throws JSONException, SQLException {
		JSONObject o = new JSONObject();		
		DBProfileTools.closeProfile(key);
		DBFriendsTools.removeFriendshipWhenDelete(key);
		DBUserTools.deleteUser(key);
		DBSessionTools.deleteSession(key);
		
		o.put("key", key);
		return o;
	}
}
