package servlets.user.user;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;

import services.user.SessionServices;
import services.user.UserServices;
import tools.db.DBSessionTools;
import tools.json.JSONErrorTools;

public class DeleteServlet extends HttpServlet{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public DeleteServlet() {
		super();
	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Map <String, String[]> pars = req.getParameterMap();
		PrintWriter out = resp.getWriter();
		resp.setContentType("text/html");
		out.print("<html><head><title>DeleteUserServlet</title></head><body>");
		if (pars.containsKey("key")) {
			String key = req.getParameter("key");
			boolean verified = true;
			try {
				if (key == null) {
					out.print(JSONErrorTools.JSONError("Invalid key", 1));
					verified = false;
				}
				if (!DBSessionTools.isConnectedWithKey(key)) {
					out.print(JSONErrorTools.JSONError("User not connected", 2));
					verified = false;
				}
				if (SessionServices.isTimeOut(key)) {
					DBSessionTools.deleteSession(key);
					out.print(JSONErrorTools.JSONError("Connection Time Out", 3));
					verified = false;
				}
				if (verified)
					out.print(UserServices.delete(key));
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
