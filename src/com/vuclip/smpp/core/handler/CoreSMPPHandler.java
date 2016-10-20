package com.vuclip.smpp.core.handler;

import java.io.IOException;

import org.smpp.TimeoutException;
import org.smpp.WrongSessionStateException;
import org.smpp.pdu.PDUException;
import org.smpp.pdu.ValueNotSetException;

import com.vuclip.smpp.core.service.impl.CoreSMPPServiceImpl;
import com.vuclip.smpp.core.to.DeliveryNotificationTO;
import com.vuclip.smpp.core.to.SMPPReqTO;
import com.vuclip.smpp.core.to.SMPPRespTO;
import com.vuclip.smpp.props.SMPPProperties;

public class CoreSMPPHandler {

	private CoreSMPPServiceImpl coreSMPPServiceImpl = null;

	public CoreSMPPHandler(SMPPProperties smppProperties) throws IOException {
		coreSMPPServiceImpl = new CoreSMPPServiceImpl(smppProperties);
	}

	public SMPPRespTO submitSMSRequest(SMPPReqTO smppReqTO)
			throws ValueNotSetException, TimeoutException, PDUException, WrongSessionStateException, IOException {
		coreSMPPServiceImpl.setSmppReqTO(smppReqTO);
		return coreSMPPServiceImpl.submitMessagePDU();
	}

	public void runReceiverListener() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				DeliveryNotificationTO dnto = coreSMPPServiceImpl.receiveListener();
				if (null != dnto) {
					System.out.println("Response : " + dnto.debugString());
				}
			}
		}).start();
	}

}
