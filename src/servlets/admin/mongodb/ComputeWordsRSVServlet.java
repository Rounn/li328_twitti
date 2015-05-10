package servlets.admin.mongodb;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;

import tools.db.DBSearchEngineTools;

public class ComputeWordsRSVServlet extends HttpServlet{
	
	private static final long serialVersionUID = 1L;

	public void doGet (HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	  {
		resp.setContentType("text/html");
		PrintWriter out = resp.getWriter();
		out.print("<html><head><title>SearchEngineWordServlet</title></head><body>");
		
		try {
			DBSearchEngineTools.cleanTables();
			out.print(DBSearchEngineTools.mapReduceDFWord(null));
			out.print(DBSearchEngineTools.mapReduceTFWord(null));
			long count = DBSearchEngineTools.countComments();
			out.print(DBSearchEngineTools.computeRSVWord(count));
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		out.print("");
		out.print("</body></html>");
	  }
}
