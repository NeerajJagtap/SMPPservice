package com.vuclip.validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidatorUtil {

	
	public static boolean ifNullOrEmpty(String param){
		if(param == null || param == ""){
			return true;
		}
		return false;
	}
	
	public static boolean ifNullOrEmpty(long param){
		if(param == 0){
			return true;
		}
		return false;
	}
	
	public static boolean ifNullOrEmpty(int param){
		if(param == 0){
			return true;
		}
		return false;
	}
	
	
	public static String checkForNullityAndGetMessage(String param, String paramName){
		
		String result = "";
		if(ifNullOrEmpty(param)){
			result=paramName + "is mandatory, can't be empty";
		}
		return result;
	}
	
	
public static String checkForNullityAndGetMessage(int param, String paramName){
		
		String result = "";
		if(ifNullOrEmpty(param)){
			result=paramName + "is mandatory, can't be empty";
		}
		return result;
	}

public static boolean checkPatter(String paramString, String pattern){
	boolean result = false;
	
	Pattern regex = Pattern.compile("[$&+,:;=?@#|]");
	Matcher matcher = regex.matcher("123=456");
	if (matcher.find()){
	   result = true;
	}
	
	return result;
}



}
