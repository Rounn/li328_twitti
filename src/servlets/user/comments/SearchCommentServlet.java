package servlets.user.comments;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;

import services.user.CommentServices;
import services.user.SessionServices;
import tools.db.DBFriendsTools;
import tools.db.DBSessionTools;
import tools.json.JSONErrorTools;

public class SearchCommentServlet extends HttpServlet{
	/**
	 * 
	 */
		private static final long serialVersionUID = 1L;
		
		
		/* (non-Javadoc)
		 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
		 */
		public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
			Map <String, String[]> pars = req.getParameterMap();
			PrintWriter out = resp.getWriter();
			resp.setContentType("text/html");
			out.print("<html><head><title>SearchCommentServlet</title></head><body>");
			if (pars.containsKey("key") && pars.containsKey("commentLogin")) {
				String key = req.getParameter("key");
				String commentLogin = req.getParameter("commentLogin");
				boolean verified = true;
				try {
					if (key == null) {
						out.print(JSONErrorTools.JSONError("Invalid user login.", 1));
						verified = false;
					}
					if (commentLogin == null) {
						out.print(JSONErrorTools.JSONError("Invalid friend login.", 1));
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
					if (!DBFriendsTools.friendshipExists(key, commentLogin)) {
						out.print(JSONErrorTools.JSONError("You cannot see this user's comments.", 3));
						verified = false;
					}
					if (verified)
						out.print(CommentServices.searchComment(key, commentLogin));
				} catch (JSONException | SQLException e) {
					e.printStackTrace();
					out.print(e.getMessage());
				} 
			}
			out.print("");
			out.print("</body></html>");
		}
		
		/**
		 * @param req
		 * @param resp
		 * @throws ServletException
		 * @throws IOException
		 * @throws SQLException 
		 */
		/*public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
			Map <String, String[]> pars = req.getParameterMap();
			resp.setContentType("text/html");
			PrintWriter out = resp.getWriter();
			out.print("<html><head><title>SearchCommentServlet</title></head><body>");
			if (pars.containsKey("key") && pars.containsKey("commentLogin")) {
				String key = req.getParameter("key");
				String commentLogin = req.getParameter("commentLogin");
				try {
					out.print(CommentServices.searchComment(key, commentLogin));
				} catch (JSONException e) {
					e.printStackTrace();
					out.print(e.getMessage());
				}
			}
			out.print("");
			out.print("</body></html>");
		}*/
}
