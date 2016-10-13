package com.vuclip.api.model;

import javax.ws.rs.QueryParam;


public class ActivationRequest {
	
	@QueryParam("userId")
	private String userId;
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ActivationRequest [userId=" + userId + "]";
	}

	

	
	
	
}
