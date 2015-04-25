package servlets.user.user;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;

import services.user.UserServices;

public class CreateServlet extends HttpServlet{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public CreateServlet() {
		super();
	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Map <String, String[]> pars = req.getParameterMap();
		PrintWriter out = resp.getWriter();
		resp.setContentType("text/html");
		out.print("<html><head><title>CreateUserServlet</title></head><body>");
		if (pars.containsKey("login") && pars.containsKey("password") && pars.containsKey("firstname") && pars.containsKey("lastname")) {
			String login = req.getParameter("login");
			String password = req.getParameter("password");
			String firstname = req.getParameter("firstname");
			String lastname = req.getParameter("lastname");
			try {
				out.print(UserServices.create(login, password, firstname, lastname));
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
