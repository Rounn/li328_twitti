<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="org.json.JSONObject"%>
<!DOCTYPE HTML>
 <html>
	<head>
		<meta  http-equiv="Content-Type"  content="text/html; charset=UTF-8">
        <title>Twitti - Friends</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
		<script type="text/javascript" src="https://code.jquery.com/jquery-2.1.4.js"></script>
		<script type="text/javascript" src="https://code.jquery.com/ui/1.11.4/jquery-ui.min.js"></script>
		<script type="text/javascript" src="js/outils.js"></script>
		<script type="text/javascript" src="js/friends.js"></script>
		<link rel="stylesheet" type="text/css" href="https://code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
	</head>
	
    <body>
        <%
        	JSONObject comments = (JSONObject) request.getAttribute("data");
        	for (int i=1; i<=comments.length(); i++) {
        		String comment = o.getJSONObject(""+i).getString("comment");
        		String author = o.getJSONObject(""+i).getString("login");
        		String date = o.getJSONObject(""+i).getString("date");
        		out.println("<h4>"+comment+"</h4>");
        		out.println("by"+author);
        		out.println(date);
        		out.println("");
        	}
        	
        %>
    </body>
 </html>