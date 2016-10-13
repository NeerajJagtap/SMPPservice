package com.vuclip.util;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public class SmppUtil {

	public static HashMap<String, String> getData(String decodedmsgdata) {

		HashMap<String, String> map = new HashMap<String, String>();

		String PRICEPOINT = "";
		String PRODUCT = "";
		String PARTNER_ROLE_ID = "";
		String message_payload = "";

		String[] decodedMessages = decodedmsgdata.split("&");
		for (String msg : decodedMessages) {
			if (msg != null && msg.contains("PRICEPOINT=")) {
				PRICEPOINT = msg.substring(msg.indexOf("PRICEPOINT=") + 11);
				map.put("PRICEPOINT", PRICEPOINT);
			}

			if (msg != null && msg.contains("PRODUCT=")) {
				PRODUCT = msg.substring(msg.indexOf("PRODUCT=") + 8);
				map.put("PRODUCT", PRODUCT);
			}

			if (msg != null && msg.contains("PARTNER ROLE ID=")) {
				PARTNER_ROLE_ID = msg.substring(msg.indexOf("PARTNER ROLE ID=") + 16);
				map.put("PARTNER_ROLE_ID", PARTNER_ROLE_ID);
			}

			if (msg != null && msg.contains("message_payload=")) {
				message_payload = msg.substring(msg.indexOf("message_payload=") + 16);
				map.put("message_payload", message_payload);
			}

		}

		return map;
	}

	public static String decodeToUtf8(String input) {
		String decodedString = null;
		try {
			decodedString = java.net.URLDecoder.decode(input, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return decodedString;
	}

	public static String encodeToUtf8(String input) {
		String encodedString = null;
		try {
			encodedString = java.net.URLEncoder.encode(input, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return encodedString;
	}
}
