package com.vuclip.soap.services;

import javax.jws.WebParam;
import javax.jws.WebService;

import com.vuclip.api.model.User;


@WebService(endpointInterface="com.vuclip.soap.services.ActivationServiceSOAPEndpoint", serviceName="ActivationServiceSOAPService")
public class ActivationServiceSOAP implements ActivationServiceSOAPEndpoint{

	public User getUserInfo(@WebParam(name="user") User user) {
		User user1 = new User();
		System.out.println("Incoming Soap Request "+user);
		user1.setMsisdn("7028639302");
		user1.setName("Devendra");
		user1.setName("Thank you");
		System.out.println(" User info : "+user1);
		return user1;
	}

}
