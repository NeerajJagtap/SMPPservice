<%@include file="taglib_includes.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<%@ page import="java.io.*,java.util.*, javax.servlet.*"%>
<!-- <META HTTP-EQUIV="Refresh" CONTENT="1"> -->

<html>
<head>
<title>Smpp Service</title>
</head>
<body>
	<center>
		<h1>SMPP Server is now up and running !!!</h1>
		<br> Today's Date and Time
		<h1></h1>

		<%
			Date date = new Date();
			out.print("<h2 align=\"center\">" + date.toString() + "</h2>");
		%>

		<%-- <%
			// Set refresh, autoload time as 5 seconds
			response.setIntHeader("Refresh", 5);
			// Get current time
			Calendar calendar = new GregorianCalendar();
			String am_pm;
			int hour = calendar.get(Calendar.HOUR);
			int minute = calendar.get(Calendar.MINUTE);
			int second = calendar.get(Calendar.SECOND);
			if (calendar.get(Calendar.AM_PM) == 0)
				am_pm = "AM";
			else
				am_pm = "PM";
			String CT = hour + ":" + minute + ":" + second + " " + am_pm;
			out.println("Crrent Time: " + CT + "\n");
		%> --%>
		<br> <br>
	</center>
</body>
</html>

