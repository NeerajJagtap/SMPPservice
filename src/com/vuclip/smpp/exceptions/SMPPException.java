package com.vuclip.smpp.exceptions;

import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.vuclip.smpp.exceptions.constant.SMPPExceptionConstant;

public class SMPPException extends Exception {

	Logger smpplogger = LogManager.getLogger("smpplogger");

	private static final String COLON = " : ";

	private static final String EXCEPTION = "SMPP_Exception : ";

	private static final long serialVersionUID = 1L;

	private static Properties properties;

	private String exceptionId = null;

	public String getExceptionId() {
		return exceptionId;
	}

	public SMPPException(String exceptionId, String message) {
		super(message);
		this.exceptionId = exceptionId;
	}

	public String getMessage() {
		if (!SMPPExceptionConstant.PROPERTIES_LOADING_EXCEPTION.equals(this.exceptionId)) {
			try {
				properties = ExceptionPropertiesLoader.getPropertyInstance();
			} catch (SMPPException e) {
				if (smpplogger.isDebugEnabled()) {
					smpplogger.debug("In SMPPException :" + e.getMessage());
				}
			}
			StringBuilder message = new StringBuilder(EXCEPTION).append(properties.getProperty(exceptionId))
					.append(COLON).append(super.getMessage());
			return message.toString();
		} else {
			return new StringBuilder(this.exceptionId).append(" Error while reading property file. [")
					.append(super.getMessage()).append("]").toString();
		}

	}
}
