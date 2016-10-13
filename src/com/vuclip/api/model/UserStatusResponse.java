package com.vuclip.api.model;

import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;

@JsonAutoDetect(fieldVisibility = Visibility.ANY)
@XmlRootElement(name="userStatusResponse")
public class UserStatusResponse {

	
	private Result result;

	private String transactionId;
	
	private List<UserStatus> userStatuses;

	private Map<String,String> additionalInfo;

	public Result getResult() {
		return result;
	}



	public void setResult(Result result) {
		this.result = result;
	}



	public String getTransactionId() {
		return transactionId;
	}



	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}


	@XmlElementWrapper
	@XmlElement(name="userStatus")
	public List<UserStatus> getUserStatuses() {
		return userStatuses;
	}



	public void setUserStatuses(List<UserStatus> userStatuses) {
		this.userStatuses = userStatuses;
	}



	@Override
	public String toString() {
		return "UserStatusResponse [result=" + result + ", transactionId="
				+ transactionId + ", userStatuses=" + userStatuses + "]";
	}



	/**
	 * @return the additionalInfo
	 */
	public Map<String, String> getAdditionalInfo() {
		return additionalInfo;
	}



	/**
	 * @param additionalInfo the additionalInfo to set
	 */
	public void setAdditionalInfo(Map<String, String> additionalInfo) {
		this.additionalInfo = additionalInfo;
	}


	
}
