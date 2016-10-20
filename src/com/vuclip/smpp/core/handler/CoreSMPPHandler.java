package com.vuclip.smpp.core.handler;

import java.io.IOException;

import org.smpp.TimeoutException;
import org.smpp.WrongSessionStateException;
import org.smpp.pdu.PDUException;
import org.smpp.pdu.ValueNotSetException;

import com.vuclip.smpp.core.service.CoreSMPPService;
import com.vuclip.smpp.core.service.impl.CoreSMPPServiceImpl;
import com.vuclip.smpp.core.to.DeliveryNotificationTO;
import com.vuclip.smpp.core.to.SMPPReqTO;
import com.vuclip.smpp.core.to.SMPPRespTO;
import com.vuclip.smpp.props.SMPPProperties;

public class CoreSMPPHandler {

	private CoreSMPPService coreSMPPService = null;

	public CoreSMPPHandler(SMPPProperties smppProperties) throws IOException {
		coreSMPPService = new CoreSMPPServiceImpl(smppProperties);
	}

	public SMPPRespTO submitSMSRequest(SMPPReqTO smppReqTO)
			throws ValueNotSetException, TimeoutException, PDUException, WrongSessionStateException, IOException {
		coreSMPPService.setSmppReqTO(smppReqTO);
		return coreSMPPService.submitMessagePDU();
	}

	public void runReceiverListener() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				DeliveryNotificationTO dnto = coreSMPPService.receiveListener();
				if (null != dnto) {
					System.out.println("Response : " + dnto.debugString());
				}
			}
		}).start();
	}

}
