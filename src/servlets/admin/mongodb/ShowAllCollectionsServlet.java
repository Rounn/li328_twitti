package servlets.admin.mongodb;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tools.db.Database;

public class ShowAllCollectionsServlet extends HttpServlet {
	
	
private static final long serialVersionUID = 1L;
	
	public void doGet(HttpServletRequest req, 
	         HttpServletResponse resp)
	        throws ServletException, IOException
	  {
		Map <String, String[]> pars = req.getParameterMap();
		resp.setContentType("text/html");
		PrintWriter out = resp.getWriter();
		out.print("<html><head><title>CreateCollServlet</title></head><body>");
		
			Set<String> colls = Database.getAllCollections();
			for (String s : colls) {
				out.print(s);
			}
		out.print("");
		out.print("</body></html>");
	  }
	

}
