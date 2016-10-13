<%@include file="taglib_includes.jsp" %>

<html>
<head>
	<script type="text/javascript" src="js/customer.js"></script>
	<title><spring:message code="App.Title"></spring:message> </title>
</head>
<body style="font-family: Arial; font-size:smaller;">

<table  bgcolor="lightblue" width="750" height="500" align="center" style="border-collapse: collapse;" border="1" bordercolor="#006699" >
	<tr>
		<td align="center"><h3>Edit Contact Form</h3></td>
	</tr>
	<tr valign="top" align="center">
    <td align="center">
 		<form:form action="saveCustomer.htm" method="post" commandName="newCustomer">
 		
	    	<!-- statusCode, hashValue, msisdnAppId, shortCode, chargeId, customerNo, customerType, 
	    	msisdnChargeBillId, chargePrice, serviceDescription, product, serviceIndicator, tranactionId, respValue, createdDate, modifiedDate -->
	    	
				<table width="500" style="border-collapse: collapse;" border="0" bordercolor="#006699" cellspacing="2" cellpadding="2">	
					<tr>
						<td width="100" align="right">statusCode</td>
						<td width="150">
						<form:input path="statusCode"/></td>
						<td align="left">
						<form:errors path="statusCode" cssStyle="color:red"></form:errors> 
						</td>
					</tr>
					
					<tr>
						<td width="100" align="right">hashValue</td>
						<td><form:input path="hashValue"/></td>
						<td align="left"><form:errors path="hashValue" cssStyle="color:red"></form:errors>  </td>
					</tr>
					<%-- <tr>
						<td width="100" align="right">Gender</td>
						<td>						
							<form:select path="gender">
					            <form:option value="M" label="Male"/>
					            <form:option value="F" label="Female"/>
					        </form:select>						
						</td>
						<td>
						</td>						
					</tr> --%>
					<tr>
						<td width="100" align="right">msisdnAppId</td>
						<td><form:input path="msisdnAppId"/></td>
						<td align="left">
						<form:errors path="msisdnAppId" cssStyle="color:red"></form:errors>  </td>
					</tr>
					<tr>
						<td width="100" align="right">shortCode</td>
						<td><form:input path="shortCode"/></td>
						<td align="left"><form:errors path="shortCode" cssStyle="color:red"></form:errors>  </td>
					</tr>
					<tr>
						<td width="100" align="right">chargeId</td>
						<td><form:input path="chargeId"/></td>
						<td align="left">
						<form:errors path="chargeId" cssStyle="color:red"></form:errors>  </td>
					</tr>
					<tr>
						<td width="100" align="right">customerNo</td>
						<td><form:input path="customerNo"/></td>
						<td align="left">
						<form:errors path="customerNo" cssStyle="color:red"></form:errors>  </td>
					</tr>
					<tr>
						<td width="100" align="right">customerType</td>
						<td><form:input path="customerType"/></td>
						<td align="left">
						<form:errors path="customerType" cssStyle="color:red"></form:errors>  </td>
					</tr>
					<tr>
						<td width="100" align="right">msisdnChargeBillId</td>
						<td><form:input path="msisdnChargeBillId"/></td>
						<td align="left">
						<form:errors path="msisdnChargeBillId" cssStyle="color:red"></form:errors>  </td>
					</tr>
					<tr>
						<td width="100" align="right">chargePrice</td>
						<td><form:input path="chargePrice"/></td>
						<td align="left">
						<form:errors path="chargePrice" cssStyle="color:red"></form:errors>  </td>
					</tr>
					<tr>
						<td width="100" align="right">serviceDescription</td>
						<td><form:input path="serviceDescription"/></td>
						<td align="left">
						<form:errors path="serviceDescription" cssStyle="color:red"></form:errors>  </td>
					</tr>
					<tr>
						<td width="100" align="right">product</td>
						<td><form:input path="product"/></td>
						<td align="left">
						<form:errors path="product" cssStyle="color:red"></form:errors>  </td>
					</tr>
					<tr>
						<td width="100" align="right">serviceIndicator</td>
						<td><form:input path="serviceIndicator"/></td>
						<td align="left">
						<form:errors path="serviceIndicator" cssStyle="color:red"></form:errors>  </td>
					</tr>
					<tr>
						<td width="100" align="right">tranactionId</td>
						<td><form:input path="tranactionId"/></td>
						<td align="left">
						<form:errors path="tranactionId" cssStyle="color:red"></form:errors>  </td>
					</tr>
					<tr>
						<td width="100" align="right">respValue</td>
						<td><form:input path="respValue"/></td>
						<td align="left">
						<form:errors path="respValue" cssStyle="color:red"></form:errors>  </td>
					</tr>
					<tr>
						<td colspan="3" align="center">
						<input type="submit" name="" value="Save">
						&nbsp;&nbsp;
						<input type="reset" name="" value="Reset">
						&nbsp;&nbsp;
						<input type="button"  value="Back" onclick="javascript:go('viewAllCustomer.htm');">
						</td>
					</tr>					
				</table>			
		</form:form>
    </td>    
  </tr>
</table>
</body>
</html>
