package com.vuclip.smpp.exceptions;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import com.vuclip.smpp.exceptions.constant.SMPPExceptionConstant;

public class ExceptionPropertiesLoader {

	private static Properties properties;

	public static Properties getPropertyInstance() throws SMPPExceptionJava {
		if (null == properties) {
			properties = new Properties();
			try {
				properties.load(
						ExceptionPropertiesLoader.class.getClassLoader().getResourceAsStream("exceptions.properties"));
			} catch (FileNotFoundException e) {
				throw new SMPPExceptionJava(SMPPExceptionConstant.PROPERTIES_LOADING_EXCEPTION, e.getMessage());
			} catch (IOException e) {
				throw new SMPPExceptionJava(SMPPExceptionConstant.PROPERTIES_LOADING_EXCEPTION, e.getMessage());
			}
		}
		return properties;
	}
}
