package servlets.user.comments;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;

import services.user.CommentServices;

public class LikeCommentServlet {

	/**
	 * 
	 */
		private static final long serialVersionUID = 1L;
		
		
		/* (non-Javadoc)
		 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
		 */
		public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException {
			Map <String, String[]> pars = req.getParameterMap();
			PrintWriter out = resp.getWriter();
			resp.setContentType("text/html");
			out.print("<html><head><title>LikeCommentServlet</title></head><body>");
			if (pars.containsKey("key") && pars.containsKey("commentLogin") && pars.containsKey("commentDate")) {
				String likerKey = req.getParameter("likerKey");
				String commentLogin = req.getParameter("commentLogin");
				String commentDate = req.getParameter("commentDate");
				try {
					out.print(CommentServices.likeComment(likerKey, commentLogin, commentDate));
				} catch (JSONException e) {
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
		public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
			Map <String, String[]> pars = req.getParameterMap();
			resp.setContentType("text/html");
			PrintWriter out = resp.getWriter();
			out.print("<html><head><title>LikeCommentServlet</title></head><body>");
			if (pars.containsKey("key") && pars.containsKey("commentLogin") && pars.containsKey("commentDate")) {
				String likerKey = req.getParameter("likerKey");
				String commentLogin = req.getParameter("commentLogin");
				String commentDate = req.getParameter("commentDate");
				try {
					out.print(CommentServices.likeComment(likerKey, commentLogin, commentDate));
				} catch (JSONException e) {
					e.printStackTrace();
					out.print(e.getMessage());
				}
			}
			out.print("");
			out.print("</body></html>");
		}
}
