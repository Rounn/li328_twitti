package servlets.user.comments;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import services.user.SessionServices;
import tools.db.DBCommentsTools;
import tools.db.DBSessionTools;
import tools.json.JSONErrorTools;

import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoException;

public class ShowFriendsCommentsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse resp) throws IOException {
		Map <String, String[]> pars = request.getParameterMap();
		PrintWriter out = resp.getWriter();
		JSONObject o = new JSONObject();
		JSONArray a = new JSONArray();
		ArrayList <DBCursor> array = new ArrayList<DBCursor>();
		resp.setContentType("text/html");
		out.print("<html><head><title>SearchCommentServlet</title></head><body>");
		if (pars.containsKey("key")) {
			String key = request.getParameter("key");
			boolean verified = true;
			try {
				if (key == null) {
					out.print(JSONErrorTools.JSONError("Invalid user login.", 1));
					verified = false;
				}
				if (!DBSessionTools.isConnectedWithKey(key)) {
					out.print(JSONErrorTools.JSONError("User not connected.", 2));
					verified = false;
				}
				if (SessionServices.isTimeOut(key)) {
					DBSessionTools.deleteSession(key);
					out.print(JSONErrorTools.JSONError("Connection Time Out", 6));
					verified = false;
				}
				if (verified) {
					ArrayList<String> comments = new ArrayList<String> ();
					array = DBCommentsTools.showFriendsComments(key);
					//for (int i = 1; i <= o.length(); i++) {
						//comments.add(o.getString(""+i));
					//}
					//request.setAttribute("data", o);
					for (DBCursor cursor : array) {
						while (cursor.hasNext()) {
							DBObject dbo = cursor.next();
							String comment = dbo.get("comment");//.getString("comment");
							String author = o.getString("login");
			        		String date = o.getString("date");
			        		out.println("<h4>"+comment+"</h4>");
			        		out.println("by"+author);
			        		out.println(date);
			        		out.println("");							
						}
						
					}
					
					/*for (int i=1; i<=a.length(); i++) {
						o = a.get;
						String comment = o.getString("comment");
		        		String author = o.getString("login");
		        		String date = o.getString("date");
		        		out.println("<h4>"+comment+"</h4>");
		        		out.println("by"+author);
		        		out.println(date);
		        		out.println("");
					}*/
						
					/*for (int i=1; i<=o.length(); i++) {
		        		String comment = o.get
		        				//getString(""+i).getString("comment");
		        		String author = o.getJSONObject(""+i).getString("login");
		        		String date = o.getJSONObject(""+i).getString("date");
		        		out.println("<h4>"+comment+"</h4>");
		        		out.println("by"+author);
		        		out.println(date);
		        		out.println("");
		        	}*/
					//this.getServletContext().getRequestDispatcher( "/showFriendsComments.jsp" ).forward( request, resp );
					//out.print(DBCommentsTools.showFriendsComments(key));
					
				}
			} catch (JSONException | SQLException | InstantiationException | IllegalAccessException | MongoException e) {
				e.printStackTrace();
				out.print(e.getMessage());
			} /*catch (ServletException e) {
				e.printStackTrace();
			} */
		}
		out.print("");
		out.print("</body></html>");
	}
}
