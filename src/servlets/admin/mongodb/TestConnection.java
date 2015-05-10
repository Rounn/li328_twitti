package servlets.admin.mongodb;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;

import tools.db.Database;

import com.mongodb.DB;
import com.mongodb.Mongo;

public class TestConnection extends HttpServlet {
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Map <String, String[]> pars = req.getParameterMap();
		PrintWriter out = resp.getWriter();
		resp.setContentType("text/html");
		out.print("<html><head><title>ConnectUserServlet</title></head><body>");
		
				DB m = Database.getMongoDB();
				out.print("Ok! ");
				out.print(m.toString());
		out.print("");
		out.print("</body></html>");
		
		
	}

}
