package com.vuclip.smpp.core.service;

import java.io.IOException;

import org.smpp.TimeoutException;
import org.smpp.WrongSessionStateException;
import org.smpp.pdu.PDUException;
import org.smpp.pdu.ValueNotSetException;

import com.vuclip.smpp.core.to.DeliveryNotificationTO;
import com.vuclip.smpp.core.to.SMPPReqTO;
import com.vuclip.smpp.core.to.SMPPRespTO;

public interface CoreSMPPService {
	
	public void setSmppReqTO(SMPPReqTO smppReqTO);

	public boolean bind();

	public SMPPRespTO submitSync()
			throws ValueNotSetException, TimeoutException, PDUException, WrongSessionStateException, IOException;

	public boolean enquire();

	public boolean unbind();

	public SMPPRespTO submitMessagePDU()
			throws ValueNotSetException, TimeoutException, PDUException, WrongSessionStateException, IOException;

	public DeliveryNotificationTO receiveListener();

}
