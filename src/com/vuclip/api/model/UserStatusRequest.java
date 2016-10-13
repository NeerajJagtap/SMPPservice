package com.vuclip.api.model;

import javax.ws.rs.QueryParam;

public class UserStatusRequest {

	@QueryParam("userId")
	private String userId;
	
	@QueryParam("transactionId")
	private String transactionId;
	
	
	@QueryParam("epocTime")
	private String epocTime;
	
	@QueryParam("authKey")
	private String authKey;

	@QueryParam("customerId")
	private String customerId;
	
	@QueryParam("additionalInfo")
	private String additionalInfo;

	@QueryParam("fetchMode")
	private String fetchMode;
	
	@QueryParam("itemId")
	private String itemId;
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getEpocTime() {
		return epocTime;
	}

	public void setEpocTime(String epocTime) {
		this.epocTime = epocTime;
	}

	public String getAuthKey() {
		return authKey;
	}

	public void setAuthKey(String authKey) {
		this.authKey = authKey;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	
	/**
	 * @return the additionalInfo
	 */
	public String getAdditionalInfo() {
		return additionalInfo;
	}

	/**
	 * @param additionalInfo the additionalInfo to set
	 */
	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}
	
	public String getFetchMode() {
		return fetchMode;
	}

	public void setFetchMode(String fetchMode) {
		this.fetchMode = fetchMode;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	@Override
	public String toString() {
		return "UserStatusRequest [userId=" + userId + ", transactionId="
				+ transactionId + ", epocTime=" + epocTime + ", authKey="
				+ authKey + ", customerId=" + customerId + ", additionalInfo="
				+ additionalInfo + ", fetchMode=" + fetchMode + ", itemId="
				+ itemId + "]";
	}
	
	
}