package com.vuclip.smpp.exceptions;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vuclip.smpp.exceptions.constant.SMPPExceptionConstant;

public class SMPPExceptionJava extends Exception {

	private static final Logger logger = LoggerFactory.getLogger(SMPPExceptionJava.class);

	private static final String COLON = " : ";

	private static final String EXCEPTION = "SMPP_Exception : ";

	private static final long serialVersionUID = 1L;

	private static Properties properties;

	private String exceptionId;

	public SMPPExceptionJava(String exceptionId, String message) {
		super(message);
		this.exceptionId = exceptionId;
	}

	public String getMessage() {
		if (!SMPPExceptionConstant.PROPERTIES_LOADING_EXCEPTION.equals(this.exceptionId)) {
			try {
				properties = ExceptionPropertiesLoader.getPropertyInstance();
			} catch (SMPPExceptionJava e) {
				if (logger.isErrorEnabled()) {
					logger.error(e.getMessage());
				}
			}
			StringBuilder message = new StringBuilder(EXCEPTION).append(properties.getProperty(exceptionId))
					.append(COLON).append(super.getMessage());
			System.out.println(message.toString());
			return message.toString();
		} else {
			return this.exceptionId + " Error while reading property file. [" + super.getMessage() + "]";
		}

	}
}
