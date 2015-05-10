package servlets.admin.mongodb;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tools.db.Database;

public class CreateCollectionServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	public void doGet(HttpServletRequest req, 
	         HttpServletResponse resp)
	        throws ServletException, IOException
	  {
		Map <String, String[]> pars = req.getParameterMap();
		resp.setContentType("text/html");
		PrintWriter out = resp.getWriter();
		out.print("<html><head><title>CreateCollServlet</title></head><body>");
		if (pars.containsKey("name")) {
			String name = req.getParameter("name");
			Database.createCollection(name);
			out.print("collection created!");
		} else {
			out.print("Name not defined in GET params.");
		}
		out.print("");
		out.print("</body></html>");
	  }

	/**
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 * @throws SQLException 
	 */
	public void doPost(HttpServletRequest request, 
	         HttpServletResponse response)
	        throws ServletException, IOException
	  {
	    response.setContentType("text/html");
	    PrintWriter out = response.getWriter();

	    out.println("<title>Example</title>" +
	       "<body bgcolor=FFFFFF>");

	    out.println("<h2>Button Clicked</h2>");

	    String DATA = request.getParameter("DATA");

	    if(DATA != null){
	      out.println(DATA);
	    } else {
	      out.println("No text entered.");
	    }

	    out.println("<P>Return to Form</A>");
	    out.close();
	  }
	/*public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
		Map <String, String[]> pars = req.getParameterMap();
		resp.setContentType("text/html");
		PrintWriter out = resp.getWriter();
		out.print("<html><head><title>CreateCollServlet</title></head><body>");
		
			Database.createCollection();
			out.print("collection created!");
		out.print("");
		out.print("</body></html>");
	}*/
}
