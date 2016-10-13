package com.vuclip.api.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;

@XmlRootElement(name="activationRequestResult")
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class Result {

	@XmlElement(name="status")
	private String status;
	
	@XmlElement(name="code")
	private String code;
	
	@XmlElement(name="message")
	private String message;

	public void setStatus(String status) {
		this.status = status;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "Result [status=" + status + ", code=" + code + ", message="
				+ message + "]";
	}
	
	
}
