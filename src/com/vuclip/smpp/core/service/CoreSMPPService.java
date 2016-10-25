package com.vuclip.smpp.core.service;

import com.vuclip.smpp.core.to.DeliveryNotificationTO;
import com.vuclip.smpp.core.to.SMPPReqTO;
import com.vuclip.smpp.core.to.SMPPRespTO;
import com.vuclip.smpp.exceptions.SMPPException;

public interface CoreSMPPService {

	public void setSmppReqTO(SMPPReqTO smppReqTO);

	public boolean bind() throws SMPPException;

	public SMPPRespTO submitSync() throws SMPPException;

	public boolean enquire() throws SMPPException;

	public boolean unbind() throws SMPPException;

	public SMPPRespTO submitMessagePDU() throws SMPPException;

	public DeliveryNotificationTO receiveListener() throws SMPPException;

}
