package servlets.user.user;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import services.user.UserServices;
import tools.db.DBSessionTools;
import tools.db.DBUserTools;
import tools.json.JSONErrorTools;

/**
 * 
 * @author Rani
 *
 */

public class ConnectServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse resp) throws IOException {
		Map <String, String[]> pars = request.getParameterMap();
		PrintWriter out = resp.getWriter();
		JSONObject o = new JSONObject();
		resp.setContentType("text/html");
		out.print("<html><head><title>ConnectUserServlet</title></head><body>");
		if (pars.containsKey("login") && pars.containsKey("password")) {
			String login = request.getParameter("login");
			String password = request.getParameter("password");
			boolean verified = true;
			try {
				if (login == null && password == null) {
					out.print(JSONErrorTools.JSONError("Invalid login or password.", 1));
					verified = false;
				}
				if (!DBUserTools.UserExists(login)) {
					out.print(JSONErrorTools.JSONError("User does not exist.", 2));
					verified = false;
				}
				if (!DBUserTools.correctPassword(login, password)) {
					out.print(JSONErrorTools.JSONError("Incorrect password.", 6));
					verified = false;
				}
				if (DBSessionTools.isConnectedWithLogin(login)) {
					DBSessionTools.deleteSessionWithLogin(login);
				}
				if (verified) {
					o = UserServices.connect(login, password);
					String key = o.getString("key");
					out.print(UserServices.connect(login, password));
					//request.setAttribute("key", key);
					//this.getServletContext().getRequestDispatcher( "/index.jsp" ).forward( request, resp );
				}
			} catch (JSONException e) {
				e.printStackTrace();
				out.print(e.getMessage());
			} catch (SQLException e) {
				e.printStackTrace();
				out.print(e.getMessage());
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
				out.print(e.getMessage());
			} /*catch (ServletException e) {
				e.printStackTrace();
				out.print(e.getMessage());
			}*/
		}
		out.print("");
		out.print("</body></html>");
		
		
	}
	
}
