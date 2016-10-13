<%@include file="taglib_includes.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title><spring:message code="App.Title"></spring:message></title>
<script type="text/javascript" src="js/contacts.js"></script>
</head>
<body style="font-family: Arial; font-size: smaller;">

	<center>
		<form action="searchCustomer.htm" method="post">
			<table style="border-collapse: collapse;" border="0"
				bordercolor="#006699" width="500">
				<tr>
					<td width="125">Enter Customer Msisdn:</td>
					<td width="80"><input type="text" name="customerNo" /></td>
					<td width="125"><input type="submit" value="Search" /></td>
				</tr>
				<tr>
					<td width="125"></td>
					<td width="80"></td>
					<td width="125"><input type="button" value="New Customer"
						onclick="javascript:go('saveCustomer.htm');" />
				</tr>
			</table>
		</form>

		<table style="border-collapse: collapse;" border="1"
			bordercolor="#006699" width="700">
			<tr bgcolor="lightblue">
				<th>Id</th>
				<th>customerNo</th>
				<th>statusCode</th>
				<th>hashValue</th>
				<th>msisdnAppId</th>
				<th>shortCode</th>
				<th>chargeId</th>
				<th>customerType</th>
				<th>msisdnChargeBillId</th>
				<th>chargePrice</th>
				<th>serviceDescription</th>
				<th>product</th>
				<th>serviceIndicator</th>
				<th>tranactionId</th>
				<th>respValue</th>
				<th>createdDate</th>
				<th>modifiedDate</th>
				<th></th>
			</tr>
			<!-- statusCode, hashValue, msisdnAppId, shortCode, chargeId, customerNo, customerType, 
	    	msisdnChargeBillId, chargePrice, serviceDescription, product, serviceIndicator, tranactionId, respValue, createdDate, modifiedDate -->

			<c:if test="${empty SEARCH_HASHDATA_RESULTS_KEY}">
				<tr>
					<td colspan="4">No Results found</td>
				</tr>
			</c:if>
			<c:if test="${! empty SEARCH_HASHDATA_RESULTS_KEY}">
				<c:forEach var="customer" items="${SEARCH_HASHDATA_RESULTS_KEY}">
					<tr>
						<td><c:out value="${customer.id}"></c:out></td>
						<td><c:out value="${customer.customerNo}"></c:out></td>
						<td><c:out value="${customer.statusCode}"></c:out></td>
						<td><c:out value="${customer.hashValue}"></c:out></td>
						<td><c:out value="${customer.msisdnAppId}"></c:out></td>
						<td><c:out value="${customer.shortCode}"></c:out></td>
						<td><c:out value="${customer.chargeId}"></c:out></td>
						<td><c:out value="${customer.customerType}"></c:out></td>
						<td><c:out value="${customer.msisdnChargeBillId}"></c:out></td>
						<td><c:out value="${customer.chargePrice}"></c:out></td>
						<td><c:out value="${customer.serviceDescription}"></c:out></td>
						<td><c:out value="${customer.product}"></c:out></td>
						<td><c:out value="${customer.serviceIndicator}"></c:out></td>
						<td><c:out value="${customer.tranactionId}"></c:out></td>
						<td><c:out value="${customer.respValue}"></c:out></td>
						<td><c:out value="${customer.createdDate}"></c:out></td>
						<td><c:out value="${customer.modifiedDate}"></c:out></td>
						<td>&nbsp;<a href="updateCustomer.htm?id=${contact.id}">Edit</a>
							&nbsp;&nbsp;<a
							href="javascript:deleteCustomer('deleteCustomer.htm?id=${customer.id}');">Delete</a>
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