package servlets.admin.mongodb;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import services.admin.AdminCommentServices;

public class ShowAllCommentsServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Map <String, String[]> pars = req.getParameterMap();
		PrintWriter out = resp.getWriter();
		resp.setContentType("text/html");
		out.print("<html><head><title>ShowAllCommentServlet</title></head><body>");
		
		try {
			JSONObject o = AdminCommentServices.showAllComments();
			out.print(o);
		} catch (JSONException e) {
			e.printStackTrace();
			out.print(e.getMessage());
		}
		
		

		out.print("");
		out.print("</body></html>");
	}
}
