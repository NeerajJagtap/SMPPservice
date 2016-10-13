package com.vuclip.smpp.service;

public interface CoreSMPPService {

	public boolean bind();

	public void submit();

	public boolean enquire();

	public boolean unbind();

}
