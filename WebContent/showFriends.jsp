<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.util.ArrayList"%>
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
		
		<!--script type="text/javascript">
			function go() {
				<%	
					String key = request.getParameter("key");
					if (key != null) {
						out.println("main(" + key + ");");
					} else {
						out.println("main();");
					}
				%>
			};
			$(go);
		</script-->
	</head>
	
    <body>
        <%
        	ArrayList <String> friends = (ArrayList<String>) request.getAttribute("data");
        	for (int i=0; i<friends.size();i++) {
				out.println("<h5>"+friends.get(i)+"</h5>");
			}			
        %>
    </body>
 </html>