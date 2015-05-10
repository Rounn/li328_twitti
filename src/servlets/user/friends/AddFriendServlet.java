package servlets.user.friends;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;

import services.user.FriendsServices;
import services.user.SessionServices;
import services.user.UserServices;
import tools.db.DBFriendsTools;
import tools.db.DBSessionTools;
import tools.db.DBUserTools;
import tools.json.JSONErrorTools;

public class AddFriendServlet extends HttpServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Map <String, String[]> pars = req.getParameterMap();
		PrintWriter out = resp.getWriter();
		resp.setContentType("text/html");
		out.print("<html><head><title>AddFriendServlet</title></head><body>");
		if (pars.containsKey("ukey") && pars.containsKey("flogin")) {
			String ukey = req.getParameter("ukey");
			String flogin = req.getParameter("flogin");
			boolean verified = true;
			try {
				if (ukey == null) {
					out.print(JSONErrorTools.JSONError("Invalid user login.", 1));
					verified = false;
				}
				if (flogin == null) { 
					out.print(JSONErrorTools.JSONError("Invalid friend login", 4));
					verified = false;
				}
				if (!DBSessionTools.isConnectedWithKey(ukey)) {
					out.print(JSONErrorTools.JSONError("User not connected.", 2));
					verified = false;
				}
				if (!DBUserTools.UserExists(flogin)) {
					out.print(JSONErrorTools.JSONError("Friend User do not exist.", 5));
					verified = false;
				}
				if (SessionServices.isTimeOut(ukey)) {
					DBSessionTools.deleteSession(ukey);
					out.print(JSONErrorTools.JSONError("Connection Time Out", 6));
					verified = false;
				}
				if (DBFriendsTools.onSelfOperation(ukey, flogin)) {
					out.print(JSONErrorTools.JSONError("Cannot add itself", 8));
					verified = false;
				}
				if (DBFriendsTools.friendshipExists(ukey, flogin)) {
					out.print(JSONErrorTools.JSONError("Friendship already exists", 7));
					verified = false;
				}
				if (verified)
					out.print(FriendsServices.addFriend(ukey, flogin));
			} catch (JSONException e) {
				e.printStackTrace();
				out.print(e.getMessage());
			} catch (SQLException e) {
				e.printStackTrace();
				out.print(e.getMessage());
			}
		}
		out.print("");
		out.print("</body></html>");
		
		
	}
}
