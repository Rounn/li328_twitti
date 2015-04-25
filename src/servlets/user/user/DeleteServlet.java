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
			try {
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
