package com.vuclip.smpp.core.service;

import com.vuclip.smpp.core.to.DeliveryNotificationTO;
import com.vuclip.smpp.core.to.SMPPReqTO;
import com.vuclip.smpp.core.to.SMPPRespTO;
import com.vuclip.smpp.exceptions.SMPPException;

public interface CoreSMPPService {

	public SMPPRespTO submitMessagePDU(SMPPReqTO smppReqTO) throws SMPPException;

	public DeliveryNotificationTO getDeliveryNotification() throws SMPPException;

}
