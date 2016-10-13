<%@include file="taglib_includes.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title><spring:message code="App.Title"></spring:message></title>
<script type="text/javascript" src="js/contacts.js"></script>
</head>
<body style="font-family: Arial; font-size: smaller;">

	<center>
		<form action="searchContacts.htm" method="post">
			<table style="border-collapse: collapse;" border="0"
				bordercolor="#006699" width="500">
				<tr>
					<td width="125">Enter Contact Name:</td>
					<td width="80"><input type="text" name="name" /></td>
					<td width="125"><input type="submit" value="Search" /></td>
				</tr>
				<tr>
					<td width="125"></td>
					<td width="80"></td>
					<td width="125"><input type="button" value="New Contact"
						onclick="javascript:go('saveContact.htm');" />
				</tr>
			</table>
		</form>

		<table style="border-collapse: collapse;" border="1"
			bordercolor="#006699" width="700">
			<tr bgcolor="lightblue">
				<th>Id</th>
				<th>Name</th>
				<th>Address</th>
				<th>Mobile</th>
				<th></th>
			</tr>
			<c:if test="${empty SEARCH_CONTACTS_RESULTS_KEY}">
				<tr>
					<td colspan="4">No Results found</td>
				</tr>
			</c:if>
			<c:if test="${! empty SEARCH_CONTACTS_RESULTS_KEY}">
				<c:forEach var="contact" items="${SEARCH_CONTACTS_RESULTS_KEY}">
					<tr>
						<td><c:out value="${contact.id}"></c:out></td>
						<td><c:out value="${contact.name}"></c:out></td>
						<td><c:out value="${contact.address}"></c:out></td>
						<td><c:out value="${contact.mobile}"></c:out></td>
						<td>&nbsp;<a href="updateContact.htm?id=${contact.id}">Edit</a>
							&nbsp;&nbsp;<a
							href="javascript:deleteContact('deleteContact.htm?id=${contact.id}');">Delete</a>
						</td>
					</tr>
				</c:forEach>
			</c:if>
		</table>
		<p>
			<a href="home.htm">home page</a>
		</p>
	</center>

</body>
</html>