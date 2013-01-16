<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>


<%@page import="com.fasterxml.jackson.core.JsonFactory"%>
<%@page import="com.fasterxml.jackson.core.JsonParser"%>
<%@page import="com.fasterxml.jackson.databind.JsonNode"%>
<%@page import="com.fasterxml.jackson.databind.ObjectMapper"%>
<%@page import="cloudfoundry.test.model.service.*"%>
<%@page import="com.mongodb.DBCollection"%>
<%@page import="com.mongodb.BasicDBObject"%>
<%@page import="com.mongodb.DBObject"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=BIG5">
<title>Insert title here</title>
<script
	src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"
	type="text/javascript"></script>
</head>
<body>
	Hello CloudFoundry
	<input id="get" type="button" value="get" />
	<div id="msg"></div>
</body>
<%
	DBCollection connection = null;

	// establish connection to Mongodb Service
	ServiceManager services = ServiceManager.INSTANCE;
	connection = services.getInstance(CloudFoundryServices.Mongodb);

	if (connection != null) {
		out.println("<p>Successfully connected to Mongodb service</p>");
		BasicDBObject doc = new BasicDBObject("name", "MongoDB")
				.append("type", "database")
				.append("count", 1)
				.append("info",
						new BasicDBObject("x", 203).append("y", 102));
		connection.insert(doc);
		DBObject myDoc = connection.findOne();
		System.out.println(myDoc);
	} else {
		System.out.println("connection error");
	}
%>
<%
	System.out.println("Hello from " + System.getenv("VCAP_APP_HOST")
			+ ":" + System.getenv("VCAP_APP_PORT"));
%>
<script type="text/javascript">
	$('#get').click(function() {
		$.get("getHostandPost.do", {}, function(Result) {
			$('#msg').append(Result);
		});
	});
</script>
</html>