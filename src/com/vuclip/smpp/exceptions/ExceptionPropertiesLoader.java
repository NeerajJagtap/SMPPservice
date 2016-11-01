package com.vuclip.smpp.exceptions;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.vuclip.smpp.exceptions.constant.SMPPExceptionConstant;

public class ExceptionPropertiesLoader {

	private static Properties properties;
	static Logger smpplogger = LogManager.getLogger("smpplogger");

	public static Properties getPropertyInstance() throws SMPPException {
		if (null == properties) {
			properties = new Properties();
			try {
				properties.load(
						ExceptionPropertiesLoader.class.getClassLoader().getResourceAsStream("exceptions.properties"));
				if (smpplogger.isDebugEnabled()) {
					smpplogger.debug("In ExceptionPropertiesLoader : All exception properties loaded.");
				}
			} catch (FileNotFoundException e) {
				throw new SMPPException(SMPPExceptionConstant.PROPERTIES_LOADING_EXCEPTION, e.getMessage());
			} catch (IOException e) {
				throw new SMPPException(SMPPExceptionConstant.PROPERTIES_LOADING_EXCEPTION, e.getMessage());
			}
		}
		return properties;
	}
}
