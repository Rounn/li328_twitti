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

public class DisconnectServlet extends HttpServlet{
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
		out.print("<html><head><title>DisconnectUserServlet</title></head><body>");
		if (pars.containsKey("key")) {
			String key = req.getParameter("key");
			boolean verified = true;
			try {
				if (key == null) { 
					out.print(JSONErrorTools.JSONError("Invalid login.", 1));
					verified = false;
				}
				if (!DBSessionTools.isConnectedWithKey(key)) {
					out.print(JSONErrorTools.JSONError("User is not connected or does not exist.", 2));
					verified = false;
				}
				if (SessionServices.isTimeOut(key)) {
					DBSessionTools.deleteSession(key);
					out.print(JSONErrorTools.JSONError("User already disconnected.", 2));
					verified = false;
				}
				if (verified)
					out.print(SessionServices.disconnect(key));
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
