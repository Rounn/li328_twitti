package servlets.user.user;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;

import services.user.UserServices;

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
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Map <String, String[]> pars = req.getParameterMap();
		PrintWriter out = resp.getWriter();
		resp.setContentType("text/html");
		out.print("<html><head><title>ConnectUserServlet</title></head><body>");
		if (pars.containsKey("login") && pars.containsKey("password")) {
			String login = req.getParameter("login");
			String password = req.getParameter("password");
			try {
				out.print(UserServices.connect(login, password));
			} catch (JSONException e) {
				e.printStackTrace();
				out.print(e.getMessage());
			} catch (SQLException e) {
				e.printStackTrace();
				out.print(e.getMessage());
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
				out.print(e.getMessage());
			}
		}
		out.print("");
		out.print("</body></html>");
		
		
	}
	
}
