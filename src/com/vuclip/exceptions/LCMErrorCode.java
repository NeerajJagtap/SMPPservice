package com.vuclip.exceptions;

public enum LCMErrorCode {
 
	
	
	
	SUCCESS ("VE000", "Success"),
	ACCEPTED( "VE001", "Accepted"),
	CONSENT_REDIRECT("VE002", "Consent Redirect"),
	CONSENT_POST("VE003", "Consent Post"),
	INSIFFICIENT_FUND("VE100", "Insufficient Fund"),
	ALREADY_SUBSCRIBED("VE101","Already Subscribed"),
	ALREADY_UNSUBSCRIBED("VE102", "Already Unsubscribed"),
	DEACTIVATION_IN_PROGRESS("VE103", "Deactivation In Progess"),
	FAILED_AT_OPERATOR("VE199", "Failed at Operator"),
	BAD_REQUEST("VE200", "Bad Request"),
	FORBIDDEN("VE201", "Forbidden"),
	UNKNOWN_ERROR("VE999","Unknown Error"),
	USER_NOT_SUBSCRIBED("VE104", "User not Subscribed"),
	
	/**
	 * 
	 * Database entity error
	 * 
	 * */
	
	BILLING_CODE_ERROR("VE300","Billing Code error"),
	PROVIDER_INFO_ERROR("VE301","Provider Info error"),
	USER_NOT_ELLIGIBLE_FOR_FREE_TRAIL("VE303","User is not elligible for free trail"),
	USER_FREE_TRAIL_HOSTORY_ERROR("VE304","User free trail history error"),
	USER_CONETXT_ERROR("VE305","User Context Error"),
	IO_EXCEPTION("VE306","FILE Handling Exception");
	
	String code;
	String messageText;
	
	private LCMErrorCode(String code , String messageText) {
		this.code = code;
		this.messageText = messageText;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessageText() {
		return messageText;
	}

	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}
	
}
