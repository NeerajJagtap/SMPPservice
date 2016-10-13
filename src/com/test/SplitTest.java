package com.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;

public class SplitTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String decodedmsgdata = "?smpp?PRICEPOINT=180375&PRODUCT=681&PARTNER ROLE ID=71&message_payload=Ooredoo Video Store has been activated for FREE. Free service is valid for 2 days Charges after free period: BZ 100/day. Get access to 200 videos! To watching videos http://goo.gl/r1ipVD To cancel click http://goo.gl/ttXggi SMS unsub to 91998";

		HashMap<String, String> map = getData(decodedmsgdata);
		
		System.out.println("PRICEPOINT :" + map.get("PRICEPOINT"));
		System.out.println("PRODUCT :" +map.get("PRODUCT") );
		System.out.println("PARTNER_ROLE_ID :" +map.get("PARTNER_ROLE_ID") );
		System.out.println("message_payload :" +map.get("message_payload") );


	}

	public static HashMap<String, String> getData(String decodedmsgdata) {

		HashMap<String, String> map = new HashMap<String, String>();

		String PRICEPOINT = "";
		String PRODUCT = "";
		String PARTNER_ROLE_ID = "";
		String message_payload = "";

		String[] decodedMessages = decodedmsgdata.split("&");
		for (String msg : decodedMessages) {
			if (msg != null && msg.contains("PRICEPOINT=")){
				PRICEPOINT = msg.substring(msg.indexOf("PRICEPOINT=") + 11);
			map.put("PRICEPOINT", PRICEPOINT);}

			if (msg != null && msg.contains("PRODUCT=")){
				PRODUCT = msg.substring(msg.indexOf("PRODUCT=") + 8);
			map.put("PRODUCT", PRODUCT);}

			if (msg != null && msg.contains("PARTNER ROLE ID=")){
				PARTNER_ROLE_ID = msg.substring(msg.indexOf("PARTNER ROLE ID=") + 16);
			map.put("PARTNER_ROLE_ID", PARTNER_ROLE_ID);}

			if (msg != null && msg.contains("message_payload=")){
				message_payload = msg.substring(msg.indexOf("message_payload=") + 16);
			map.put("message_payload", message_payload);
			}

		}

	
		return map;
	}
}
