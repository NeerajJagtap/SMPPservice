<%@include file="taglib_includes.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<%@ page import="java.io.*,java.util.*, javax.servlet.*"%>

<html>
<head>
<title><spring:message code="App.Title"></spring:message></title>
</head>
<body>
	<center>
		<h1>SMPP Server is up !!!</h1>
		<br> Today's Date and Time
		<h1></h1>

		<%
			Date date = new Date();
			out.print("<h2 align=\"center\">" + date.toString() + "</h2>");
		%>

		<p>
			<!-- <a href="viewAllCustomer.htm">Get Customer</a> -->
			<!-- <a href="viewAllCustomer.htm">Get AOC Hash Response</a> -->
		</p>
		<br> <br>
	</center>
</body>
</html>

