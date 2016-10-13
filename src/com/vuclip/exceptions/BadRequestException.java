package com.vuclip.exceptions;

import com.vuclip.exceptions.LCMServiceException;

public class BadRequestException extends LCMServiceException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String message;
	public BadRequestException(String message){
		super(message);
		this.message = message;
	}
	
	@Override
	public String getMessage() {
		return this.message;
	}
	
}
