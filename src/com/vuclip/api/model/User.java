package com.vuclip.api.model;

import javax.ws.rs.QueryParam;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;

@XmlRootElement(name="user")
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class User {

	@XmlElement(name="msisdn")
	private String msisdn;
	
	@XmlElement(name="status")
	private String status;
	
	@XmlElement(name="name")
	private String name;
	
	@QueryParam("msisdn")
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}
	
	@QueryParam("status")
	public void setStatus(String status) {
		this.status = status;
	}

	@QueryParam("name")
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "User [msisdn=" + msisdn + ", status=" + status + ", name="
				+ name + "]";
	}
	
	
	
}
