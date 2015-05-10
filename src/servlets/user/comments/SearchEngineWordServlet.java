package servlets.user.comments;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;

import com.mongodb.MongoException;

import services.user.CommentServices;
import services.user.SessionServices;
import tools.db.DBSearchEngineTools;
import tools.db.DBSessionTools;
import tools.db.Database;
import tools.json.JSONErrorTools;

/**
 * S
 * @author Rani
 *
 */
public class SearchEngineWordServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	  {
		Map <String, String[]> pars = req.getParameterMap();
		resp.setContentType("text/html");
		PrintWriter out = resp.getWriter();
		out.print("<html><head><title>SearchEngineWordServlet</title></head><body>");
		
		if (pars.containsKey("key") && pars.containsKey("text")) {
			String key = req.getParameter("key");
			String text = req.getParameter("text");
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
					DBSessionTools.setTimestamp(key);
					out.print(CommentServices.searchEnginForComments(text));
				}
			} catch (JSONException | SQLException | InstantiationException | IllegalAccessException | MongoException e) {
				e.printStackTrace();
				out.print(e.getMessage());
			}
		}
		out.print("");
		out.print("</body></html>");
	  }

}
