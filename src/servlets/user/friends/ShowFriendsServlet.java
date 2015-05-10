package servlets.user.friends;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import services.user.SessionServices;
import tools.db.DBFriendsTools;
import tools.db.DBSessionTools;
import tools.json.JSONErrorTools;

public class ShowFriendsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse resp) throws IOException {
		Map <String, String[]> pars = request.getParameterMap();
		PrintWriter out = resp.getWriter();
		JSONObject o = new JSONObject();
		resp.setContentType("text/html");
		out.print("<html><head><title>AddFriendServlet</title></head><body>");
		if (pars.containsKey("key")) {
			String key = request.getParameter("key");
			boolean verified = true;
			try {
				if (key == null) {
					out.print(JSONErrorTools.JSONError("Invalid user login.", 1));
					verified = false;
				}
				if (!DBSessionTools.isConnectedWithKey(key)) {
					out.print(JSONErrorTools.JSONError("User not connected.", 2));
					verified = false;
				}
				if (SessionServices.isTimeOut(key)) {
					DBSessionTools.deleteSession(key);
					out.print(JSONErrorTools.JSONError("Connection Time Out", 6));
					verified = false;
				}
				if (verified) {
					ArrayList<String> friends = new ArrayList<String> ();
					o = DBFriendsTools.showFriends(key);
					for (int i = 1; i <= o.length(); i++) {
						friends.add(o.getString(""+i));
					}
					request.setAttribute("data", friends);
					//request.setAttribute("name", "rani");
					this.getServletContext().getRequestDispatcher( "/showFriends.jsp" ).forward( request, resp );
					//out.print(DBFriendsTools.showFriends(key));
				}
				
			} catch (JSONException e) {
				e.printStackTrace();
				out.print(e.getMessage());
			} catch (SQLException e) {
				e.printStackTrace();
				out.print(e.getMessage());
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (ServletException e) {
				e.printStackTrace();
			}
		}
		out.print("");
		out.print("</body></html>");	
	}
}
