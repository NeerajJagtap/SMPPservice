function go(url)
{
	window.location = url;
}

function newCustomer()
{
	window.location = "saveCustomer.htm";
}

function deleteCustomer(url)
{
	var isOK = confirm("Are you sure to delete?");
	if(isOK)
	{
		go(url);
		
	}
}
