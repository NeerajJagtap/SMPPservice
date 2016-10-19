package com.vuclip.smpp.props;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public final class WebProperties {

	
	private final String sendsmsTxt ;
	private final String billingTxt ;
	private final String zeroPricePoint ;
	private final String providerId;
	private final String customerId;
	
	 @Autowired
	    public WebProperties(@Value("${sendsmsTxt}") String sendsmsTxt,
	                         @Value("${billingTxt}") String billingTxt,
	                         @Value("${zeroPricePoint}") String zeroPricePoint, 
	                         @Value("${providerId}") String providerId, 
	                         @Value("${customerId}") String customerId) {
	 
	        this.sendsmsTxt = sendsmsTxt;
	        this.billingTxt = billingTxt;
	        this.zeroPricePoint = zeroPricePoint;
	        this.providerId = providerId;
	        this.customerId = customerId;
	    }

	public String getSendsmsTxt() {
		return sendsmsTxt;
	}

	public String getBillingTxt() {
		return billingTxt;
	}

	public String getZeroPricePoint() {
		return zeroPricePoint;
	}

	public String getProviderId() {
		return providerId;
	}

	public String getCustomerId() {
		return customerId;
	}
	 
	 
}
