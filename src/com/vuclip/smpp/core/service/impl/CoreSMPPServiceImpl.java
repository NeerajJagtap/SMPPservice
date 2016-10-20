package com.vuclip.smpp.core.service.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smpp.Data;
import org.smpp.ServerPDUEvent;
import org.smpp.Session;
import org.smpp.TimeoutException;
import org.smpp.WrongSessionStateException;
import org.smpp.pdu.BindReceiver;
import org.smpp.pdu.BindRequest;
import org.smpp.pdu.BindResponse;
import org.smpp.pdu.BindTransciever;
import org.smpp.pdu.BindTransmitter;
import org.smpp.pdu.DeliverSM;
import org.smpp.pdu.EnquireLink;
import org.smpp.pdu.EnquireLinkResp;
import org.smpp.pdu.PDU;
import org.smpp.pdu.PDUException;
import org.smpp.pdu.Request;
import org.smpp.pdu.Response;
import org.smpp.pdu.SubmitSM;
import org.smpp.pdu.SubmitSMResp;
import org.smpp.pdu.UnbindResp;
import org.smpp.pdu.ValueNotSetException;
import org.smpp.pdu.WrongDateFormatException;
import org.smpp.pdu.WrongLengthOfStringException;
import org.smpp.pdu.tlv.TLVException;
import org.smpp.util.ByteBuffer;

import com.vuclip.smpp.core.constants.MessageState;
import com.vuclip.smpp.core.service.CoreSMPPService;
import com.vuclip.smpp.core.to.DeliveryNotificationTO;
import com.vuclip.smpp.core.to.PDUTO;
import com.vuclip.smpp.core.to.SMPPReqTO;
import com.vuclip.smpp.core.to.SMPPRespTO;
import com.vuclip.smpp.core.util.SMPPPDUEventListener;
import com.vuclip.smpp.core.util.SMPPSession;
import com.vuclip.smpp.props.SMPPProperties;

public class CoreSMPPServiceImpl implements CoreSMPPService {

	private static final Logger logger = LoggerFactory.getLogger(CoreSMPPServiceImpl.class);

	private volatile Session session = null;

	private static SMPPPDUEventListener eventListener = null;

	private SMPPProperties smppProperties;

	private SMPPReqTO smppReqTO;

	// Constants
	private static final String COLLON = ":";

	private static final String EMPTY_STRING = "";

	private static final String SPACE = " ";

	private static final String ID = "id";

	private static final String DLVRD = "dlvrd";

	private static final String STATE = "stat";

	private static final String SUBMIT_DATE = "submitdate";

	private static final String DONE_DATE = "donedate";

	private static final String UNDELIVERED = "UNDELIV";

	public CoreSMPPServiceImpl(SMPPProperties smppProperties) {
		this.smppProperties = smppProperties;
	}

	public SMPPReqTO getSmppReqTO() {
		return smppReqTO;
	}

	public void setSmppReqTO(SMPPReqTO smppReqTO) {
		this.smppReqTO = smppReqTO;
	}

	public boolean bind() {
		try {
			System.out.println("Bind Start");
			if (logger.isDebugEnabled()) {
				logger.debug("Bind request Start");
			}
			BindRequest request = null;
			BindResponse response = null;
			session = SMPPSession.getInstance(smppProperties.getSmppServerIP(),
					Integer.parseInt(smppProperties.getSmppServerPort()));
			// SetParameters
			request = setBindParams(request);
			// send the request
			if (logger.isDebugEnabled()) {
				logger.debug("Bind request " + request.debugString());
			}
			if (smppProperties.isAsynchorized()) {
				eventListener = new SMPPPDUEventListener(session);
				response = session.bind(request, eventListener);
			} else {
				response = session.bind(request);
			}
			if (logger.isDebugEnabled()) {
				logger.debug("Bind response " + response.debugString());
			}
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error(e.getMessage());
			}
		}
		System.out.println("Bind End");
		if (logger.isDebugEnabled()) {
			logger.debug("Bind request End");
		}
		return session.isBound();

	}

	private BindRequest setBindParams(BindRequest request) throws WrongLengthOfStringException {
		String smppBindOption = smppProperties.getSmppBindOption();
		if (smppBindOption.compareToIgnoreCase("t") == 0) {
			request = new BindTransmitter();
		} else if (smppBindOption.compareToIgnoreCase("r") == 0) {
			request = new BindReceiver();
		} else if (smppBindOption.compareToIgnoreCase("tr") == 0) {
			request = new BindTransciever();
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("Invalid bind mode, expected t, r or tr, got " + smppBindOption + ". Operation canceled.");
			}
		}
		// set values
		request.setSystemId(smppProperties.getSystemID());
		request.setPassword(smppProperties.getPassword());
		request.setSystemType(smppProperties.getSystemType());
		request.setInterfaceVersion((byte) 0x34);
		return request;
	}

	public SMPPRespTO submitSync()
			throws ValueNotSetException, TimeoutException, PDUException, WrongSessionStateException, IOException {
		System.out.println("Submit SM Start");
		SMPPRespTO smppRespTO = null;
		if (logger.isDebugEnabled()) {
			logger.debug("Submit SM Start");
		}
		SubmitSM request = new SubmitSM();
		SubmitSMResp response;
		if (null == session)
			session = SMPPSession.getInstance(smppProperties.getSmppServerIP(),
					Integer.parseInt(smppProperties.getSmppServerPort()));
		// set values
		setSubmitParameters(request);
		// send the request
		if (logger.isDebugEnabled()) {
			logger.debug("Submit_SM request " + request.debugString());
		}
		if (smppProperties.isAsynchorized()) {
			session.submit(request);
		} else {
			response = session.submit(request);
			if (logger.isDebugEnabled()) {
				logger.debug("Submit_SM response " + response.debugString());
			}
			smppRespTO = new SMPPRespTO();
			smppRespTO.setRespStatus(response.getCommandStatus());
			smppRespTO.setResponseId(response.getCommandId());
			smppRespTO.setResposeMessage(response.getBody().toString());
			smppRespTO.setResponseMsgId(response.getMessageId());
		}
		System.out.println("Submit SM End");
		if (logger.isDebugEnabled()) {
			logger.debug("Submit SM End with");
		}
		return smppRespTO;
	}

	private void setSubmitParameters(SubmitSM request)
			throws WrongLengthOfStringException, UnsupportedEncodingException, WrongDateFormatException, TLVException {
		PDUTO pduto = smppReqTO.getPduto();
		// Set Request SpeceficParameters
		request.setServiceType(pduto.getServiceType());
		request.setSourceAddr(pduto.getSourceAddress());
		request.setDestAddr(pduto.getDestAddress());
		request.setReplaceIfPresentFlag(pduto.getReplaceIfPresentFlag());
		request.setShortMessage(pduto.getShortMessage());
		request.setScheduleDeliveryTime(pduto.getScheduleDeliveryTime());
		request.setValidityPeriod(pduto.getValidityPeriod());
		request.setEsmClass(pduto.getEsmClass());
		request.setProtocolId(pduto.getProtocolId());
		request.setPriorityFlag(pduto.getPriorityFlag());
		request.setRegisteredDelivery(pduto.getRegisteredDelivery());
		request.setDataCoding(pduto.getDataCoding());
		request.setSmDefaultMsgId(pduto.getSmDefaultMsgId());
		request.setSequenceNumber(1);
		request.assignSequenceNumber(true);
		// Set Config Specific Parameters
		Iterator<Map.Entry<Integer, Integer>> optionalParamMapIterator = smppProperties.getOptionalParamMap().entrySet()
				.iterator();
		while (optionalParamMapIterator.hasNext()) {
			Map.Entry<Integer, Integer> entry = optionalParamMapIterator.next();
			request.setExtraOptional((short) entry.getKey().intValue(),
					new ByteBuffer(java.nio.ByteBuffer.allocate(4).putInt(entry.getValue()).array()));
		}
		// Payload
		request.setMessagePayload(new ByteBuffer(pduto.getMessagePayload().getBytes()));
	}

	public boolean enquire() {
		if (logger.isDebugEnabled()) {
			logger.debug("SMPPTest.enquireLink()");
		}
		try {

			EnquireLink request = new EnquireLink();
			EnquireLinkResp response;
			if (logger.isDebugEnabled()) {
				logger.debug("Enquire Link request " + request.debugString());
			}
			if (smppProperties.isAsynchorized()) {
				session.enquireLink(request);
			} else {
				response = session.enquireLink(request);
				logger.debug("Enquire Link response " + response.debugString());
				return response.getCommandStatus() == Data.ESME_ROK ? true : false;
			}
			return true;

		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error("Enquire Link operation failed. " + e);
			}
		}
		return false;
	}

	public boolean unbind() {
		if (!session.isBound()) {
			return true;
		}
		try {
			// send the request
			if (logger.isDebugEnabled()) {
				logger.debug(Thread.currentThread().getName() + ": Going to unbind. Session:" + session);
			}
			/**
			 * if (session.getReceiver().isReceiver()) {
			 * log.log(Thread.currentThread().getName() + ": It can take a while
			 * to stop the receiver."); }
			 **/
			UnbindResp response = session.unbind();
			if (logger.isDebugEnabled()) {
				logger.debug(Thread.currentThread().getName() + ": Unbind response " + response);
			}
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error(e.getMessage());
			}
		} finally {

		}
		return session.isBound();
	}

	public DeliveryNotificationTO receiveListener() {
		if (null != session && session.isBound()) {
			try {
				// if (!session.isBound()) {
				// this.bind();
				// }
				if (logger.isDebugEnabled()) {
					logger.debug("Receiver Started");
				}
				// With Data.RECEIVE_BLOCKING thread will block forever to
				// receive
				PDU pdu = null;
				System.out.print("Going to receive a PDU. ");
				if (smppProperties.isAsynchorized()) {
					ServerPDUEvent pduEvent = eventListener.getRequestEvent(Data.RECEIVE_BLOCKING);
					if (pduEvent != null) {
						pdu = pduEvent.getPDU();
					}
				} else {
					pdu = session.receive(Data.RECEIVE_BLOCKING);
				}
				// Take only Deliver SM
				if (pdu != null && pdu.getCommandId() == Data.DELIVER_SM && pdu.getCommandStatus() == Data.ESME_ROK) {
					DeliverSM deliverSM = (DeliverSM) pdu;
					DeliveryNotificationTO dnto = new DeliveryNotificationTO();
					System.out.println("Received PDU " + pdu.debugString());
					if (deliverSM.isRequest()) {
						Response response = ((Request) deliverSM).getResponse();
						// respond with default response
						System.out.println("Going to send default response to request " + response.debugString());
						session.respond(response);
					}
					dnto.setMsisdn(deliverSM.getSourceAddr().getAddress());
					getDataFromMessage(dnto, deliverSM.getShortMessage());
					session.unbind();
					session.close();
					if (logger.isDebugEnabled()) {
						logger.debug("Receiver End Success ");
					}
					return dnto;
				} else {
					System.out.println("No PDU received this time.");
				}
				session.unbind();
				session.close();
			} catch (Exception e) {
				System.out.println("Receiving failed. " + e);
				e.printStackTrace();
			}
		}
		return null;
	}

	private void getDataFromMessage(DeliveryNotificationTO dnto, String shortMessage) {
		String[] splitMessage = shortMessage.split(SPACE);
		int length = splitMessage.length;
		int dateCount = 0;
		for (int i = 0; i < length; i++) {

			if (splitMessage[i].contains(COLLON)) {
				String[] splitKeyValue = splitMessage[i].split(COLLON);
				if (splitKeyValue[0].equals(STATE)) {
					dnto.setMessageState(splitKeyValue[1].equalsIgnoreCase(UNDELIVERED) ? MessageState.UNDELIVERED
							: MessageState.DELIVERED);
				} else if (splitKeyValue[0].equals(ID)) {
					dnto.setMessageId(splitKeyValue[1]);
				} else if (splitKeyValue[0].equals("date")) {
					if (dateCount == 0) {
						dnto.setSubmitDate(splitKeyValue[1]);
					} else {
						dnto.setDoneDate(splitKeyValue[1]);
					}
				}
			}
		}

		System.out.println(shortMessage);
	}

	public SMPPRespTO submitMessagePDU()
			throws ValueNotSetException, TimeoutException, PDUException, WrongSessionStateException, IOException {
		boolean bindSuccess = false;
		if (session == null || (session != null && !session.isBound())) {
			bindSuccess = bind();
		}
		if (bindSuccess) {
			return submitSync();
		}
		return null;
	}
}
