package com.vuclip.constants;

public class Constants {

	public static final String urlPattern = "^(http:\\/\\/|https:\\/\\/)?(www.)?([a-zA-Z0-9]+).[a-zA-Z0-9]*.[a-z]{3}.?([a-z]+)?$";
	
	public static final int MAX_CHAR_USER_ID = 100;
	public static final int MAX_CHAR_SOURCE = 100;
	public static final int MAX_CHAR_AUTH_KEY = 45;
	public static final int MAX_CHAR_AD_NETWORK_TRANSACTION_ID = 400;
	public static final String SOURCE_ALLOWED_CHARS = "Allowed chars are : _-@.;";
	
	public static final String SOURCE_PATTERN = "^[[a-zA-Z0-9][_\\-@\\.]*[a-zA-Z0-9]*]*$";
	
	public static final String MAX_CHAR_EXCEED_MSG = "parameter exceeded max characters constraint, allowed max chars are:";
	public static final String MANDATORY_PARAM_NULLITY_MSG="parameter is mandatory, can't be empty;";
	public static final String CHAR_CONSTRANT_VOLATION_MSG = "paramter voilates allowed character constraints, ";
	public static final String NUMBER_CONSTRANT_VOLATION_MSG = "parameter should be number;";
	public static final String URL_CONSTRAINT_VOLATION_MSG = "parameter is not a valid URL;";
	
	public static final String TRANS_ID_TO_BILLING_CODE = "tranToBillingCode.properties";
	public static final String TRANS_ID_TO_CALLBACK_URL = "transToCallbackURL.properties";
	
	public static final String[] ALLOWED_PROTOCOLS_IN_URL = {"http","https"};

}