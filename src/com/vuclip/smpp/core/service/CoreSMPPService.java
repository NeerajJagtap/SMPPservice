package com.vuclip.smpp.core.service;

import com.vuclip.smpp.core.to.DeliveryNotificationTO;
import com.vuclip.smpp.core.to.SMPPReqTO;
import com.vuclip.smpp.core.to.SMPPRespTO;
import com.vuclip.smpp.exceptions.SMPPExceptionJava;

public interface CoreSMPPService {

	public void setSmppReqTO(SMPPReqTO smppReqTO);

	public boolean bind() throws SMPPExceptionJava;

	public SMPPRespTO submitSync() throws SMPPExceptionJava;

	public boolean enquire() throws SMPPExceptionJava;

	public boolean unbind() throws SMPPExceptionJava;

	public SMPPRespTO submitMessagePDU() throws SMPPExceptionJava;

	public DeliveryNotificationTO receiveListener() throws SMPPExceptionJava;

}
