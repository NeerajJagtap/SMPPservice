package com.vuclip.soap.services;

import javax.jws.WebService;

import com.vuclip.api.model.User;

@WebService
public interface ActivationServiceSOAPEndpoint {
	
	public User getUserInfo(User user);

}
