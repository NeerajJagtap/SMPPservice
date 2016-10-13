package com.vuclip.validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.validator.routines.UrlValidator;

import com.vuclip.constants.Constants;


public class BaseValidator {

	protected StringBuilder resultMessage = new StringBuilder();

	protected boolean isMandatoryIntParamValid(String param, String paramName) {

		boolean isParamValid = true;

		if (ValidatorUtil.ifNullOrEmpty(param)) {
			isParamValid = false;
			resultMessage.append(paramName + " "
					+ Constants.MANDATORY_PARAM_NULLITY_MSG);
			return isParamValid;
		}

		if (!isInt(param)) {
			isParamValid = false;
			resultMessage.append(paramName + " "
					+ Constants.NUMBER_CONSTRANT_VOLATION_MSG);
		}

		return isParamValid;
	}

	protected boolean isMandatoryLongParamValid(String param, String paramName) {

		boolean isParamValid = true;

		if (ValidatorUtil.ifNullOrEmpty(param)) {
			isParamValid = false;
			resultMessage.append(paramName + " "
					+ Constants.MANDATORY_PARAM_NULLITY_MSG);
			return isParamValid;
		}

		if (!isLong(param)) {
			isParamValid = false;
			resultMessage.append(paramName + " "
					+ Constants.NUMBER_CONSTRANT_VOLATION_MSG);
		}

		return isParamValid;
	}
	
	
	protected boolean isMandatoryStringParamValid(String param, String paramName) {

		if (ValidatorUtil.ifNullOrEmpty(param)) {
			resultMessage.append(paramName + " "
					+ Constants.MANDATORY_PARAM_NULLITY_MSG);
			return false;
		}
		return true;
	}

	protected boolean isMandatoryURLParamValid(String param, String paramName) {

		if (ValidatorUtil.ifNullOrEmpty(param)) {
			resultMessage.append(paramName + " "
					+ Constants.MANDATORY_PARAM_NULLITY_MSG);
			return false;
		}
		if (!isURLParamValid(param)) {
			resultMessage.append(paramName + " "
					+ Constants.URL_CONSTRAINT_VOLATION_MSG);
			return false;
		}
		return true;
	}

	protected boolean isMandatoryStringParamValid(String param, int maxChar,
			String paramName) {

		if (ValidatorUtil.ifNullOrEmpty(param)) {
			resultMessage.append(paramName + " "
					+ Constants.MANDATORY_PARAM_NULLITY_MSG);
			return false;
		}
		if (isParamOverSize(param, maxChar)) {
			resultMessage.append(paramName + " "
					+ Constants.MAX_CHAR_EXCEED_MSG + " " + maxChar + " ;");
			return false;
		}
		return true;
	}

	protected boolean isMandatoryStringParamValid(String param, int maxChar,
			String pattern, String paramName) {

		if (ValidatorUtil.ifNullOrEmpty(param)) {
			resultMessage.append(paramName + " "
					+ Constants.MANDATORY_PARAM_NULLITY_MSG);
			return false;
		}
		if (isParamOverSize(param, maxChar)) {
			resultMessage.append(paramName + " "
					+ Constants.MAX_CHAR_EXCEED_MSG + " " + maxChar + " ;");
			return false;
		}
		if (!isParamPatternValid(param, pattern)) {
			resultMessage.append(paramName+" "+Constants.CHAR_CONSTRANT_VOLATION_MSG+Constants.SOURCE_ALLOWED_CHARS);
			return false;
		}
		return true;
	}

	protected boolean isParamOverSize(String param, int maxChar) {

		if (param != null) {
			return (param.length() > maxChar);
		}
		return false;
	}

	protected boolean isParamOverSize(String param, int maxChar, String paramName) {

		boolean isParamOverSized = false;
		if (param != null) {
			
			isParamOverSized = param.length() > maxChar;
		}
		if(isParamOverSized){
			resultMessage.append(paramName + " "
					+ Constants.MAX_CHAR_EXCEED_MSG + " " + maxChar + " ;");
		}
		return isParamOverSized;
	}
	
	protected boolean isIntParamValid(String param, String paramName) {

		boolean isParamValid = true;

		if (!isInt(param)) {
			isParamValid = false;
			resultMessage.append(paramName + " "
					+ Constants.NUMBER_CONSTRANT_VOLATION_MSG);
		}

		return isParamValid;
	}

	protected static boolean isInt(String param) {

		boolean isNumber = false;

		try {
			Integer.parseInt(param);
			isNumber = true;
		} catch (NumberFormatException ex) {
			isNumber = false;
		}
		return isNumber;
	}

	protected static boolean isLong(String param) {

		boolean isNumber = false;

		try {
			Long.parseLong(param);
			isNumber = true;
		} catch (NumberFormatException ex) {
			isNumber = false;
		}
		return isNumber;
	}
	
	
	public boolean isParamPatternValid(String param, String pattern) {
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(param);
		return m.matches();
	}

	protected boolean isURLParamValid(String param) {

		return new UrlValidator(Constants.ALLOWED_PROTOCOLS_IN_URL,UrlValidator.ALLOW_2_SLASHES)
				.isValid(param);

	}

	protected boolean isURLParamValid(String param, String paramName) {

		boolean isValid = new UrlValidator(Constants.ALLOWED_PROTOCOLS_IN_URL,UrlValidator.ALLOW_2_SLASHES)
				.isValid(param);

		if (!isValid) {
			resultMessage.append(paramName + " "
					+ Constants.URL_CONSTRAINT_VOLATION_MSG);
		}
		return isValid;

	}

	public String getResultMessage() {
		return resultMessage.toString();
	}

}
