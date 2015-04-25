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
		if (login == null || password == null) 
			return JSONErrorTools.JSONError("Invalid login or password.", 1);
		if (firstname == null || lastname == null) 
			return JSONErrorTools.JSONError("Invalid firstname or lastname.", 1);
		if (DBUserTools.UserExists(login))
			return JSONErrorTools.JSONError("User already exists.", 2);
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
		if (login == null && password == null) 
			return JSONErrorTools.JSONError("Invalid login or password.", 1);
		if (!DBUserTools.UserExists(login))
			return JSONErrorTools.JSONError("User does not exist.", 2);
		if (!DBUserTools.correctPassword(login, password))
			return JSONErrorTools.JSONError("Incorrect password.", 6);
		if (DBSessionTools.isConnectedWithLogin(login)) {
			DBSessionTools.deleteSessionWithLogin(login);
		}
		DBSessionTools.createSession(login, key);
		o.put("key", key);
		return o;
	}
	
	/**
	 * Service de suppression d'un compte utilisateur.
	 * Son Login et Password sont supprim�s de la base de donn�e.
	 * Mais son profile et ses informations sont n�anmoins pr�serv�s dans nos bases de donn�es avec un flag closed, elle sont trop ch�res!
	 * @param key
	 * @return
	 * @throws JSONException
	 * @throws SQLException
	 */
	public static JSONObject delete(String key) throws JSONException, SQLException {
		JSONObject o = new JSONObject();		
		if (key == null) 
			return JSONErrorTools.JSONError("Invalid key", 1);
		if (!DBSessionTools.isConnectedWithKey(key))
			return JSONErrorTools.JSONError("User not connected", 2);
		if (SessionServices.isTimeOut(key)) {
			DBSessionTools.deleteSession(key);
			return JSONErrorTools.JSONError("Connection Time Out", 3);
		}
		DBProfileTools.closeProfile(key);
		DBFriendsTools.removeFriendshipWhenDelete(key);
		DBUserTools.deleteUser(key);
		DBSessionTools.deleteSession(key);
		
		o.put("key", key);
		return o;
	}
}
