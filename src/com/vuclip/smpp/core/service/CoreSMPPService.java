package com.vuclip.smpp.core.service;

import java.io.IOException;

import org.smpp.TimeoutException;
import org.smpp.WrongSessionStateException;
import org.smpp.pdu.PDUException;
import org.smpp.pdu.ValueNotSetException;

import com.vuclip.smpp.core.to.SMPPRespTO;

public interface CoreSMPPService {

	public boolean bind();

	public SMPPRespTO submitSync()
			throws ValueNotSetException, TimeoutException, PDUException, WrongSessionStateException, IOException;

	public boolean enquire();

	public boolean unbind();

}
