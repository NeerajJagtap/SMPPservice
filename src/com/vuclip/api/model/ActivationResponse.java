package com.vuclip.api.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;


@XmlRootElement(name="activationResponse")
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class ActivationResponse {
	
	//@XmlElement(name="userId")
	private String userId;
	
	//@XmlElement(name="result")
	private Result result;
	
	//@XmlElement(name="customer")
	private CustomerDetails customer;

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the result
	 */
	public Result getResult() {
		return result;
	}

	/**
	 * @param result the result to set
	 */
	public void setResult(Result result) {
		this.result = result;
	}

	/**
	 * @return the customerDetails
	 */
	public CustomerDetails getCustomerDetails() {
		return customer;
	}

	/**
	 * @param customerDetails the customerDetails to set
	 */
	public void setCustomerDetails(CustomerDetails customer) {
		this.customer = customer;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ActivationResponse [userId=" + userId + ", result=" + result + ", customerDetails=" + customer
				+ "]";
	}
	
}
