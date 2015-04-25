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

public class RemoveFriendServlet extends HttpServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Map <String, String[]> pars = req.getParameterMap();
		PrintWriter out = resp.getWriter();
		resp.setContentType("text/html");
		out.print("<html><head><title>RemoveFriendServlet</title></head><body>");
		if (pars.containsKey("ukey") && pars.containsKey("flogin")) {
			String ukey = req.getParameter("ukey");
			String flogin = req.getParameter("flogin");
			try {
				out.print(FriendsServices.removeFriend(ukey, flogin));
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