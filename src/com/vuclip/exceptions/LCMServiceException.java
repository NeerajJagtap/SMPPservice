/**
 * 
 */
package com.vuclip.exceptions;

import com.vuclip.exceptions.LCMErrorCode;

/**
 * @author 
 *
 */
public class LCMServiceException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4780446719321454425L;
	LCMErrorCode lcmErrorCode;
	private String message;
	
	public LCMServiceException(LCMErrorCode code, Exception e){
		super(e);
		this.message = e.getMessage();
		lcmErrorCode = code;
	}

	public LCMServiceException(String message){
		this.message = message;
	}
	
	public LCMServiceException(LCMErrorCode code) {
		super();
		this.message = "";
		lcmErrorCode = code;
	}

	public LCMErrorCode getLcmErrorCode() {
		return lcmErrorCode;
	}

	public void setLcmErrorCode(LCMErrorCode lcmErrorCode) {
		this.lcmErrorCode = lcmErrorCode;
	}
	
	@Override
	public String getMessage() {
		return this.message;
	}
}
