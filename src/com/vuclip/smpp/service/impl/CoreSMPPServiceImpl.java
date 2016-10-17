package com.vuclip.smpp.service.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.logica.smpp.Data;
import com.logica.smpp.ServerPDUEvent;
import com.logica.smpp.Session;
import com.logica.smpp.TimeoutException;
import com.logica.smpp.WrongSessionStateException;
import com.logica.smpp.pdu.BindReceiver;
import com.logica.smpp.pdu.BindRequest;
import com.logica.smpp.pdu.BindResponse;
import com.logica.smpp.pdu.BindTransciever;
import com.logica.smpp.pdu.BindTransmitter;
import com.logica.smpp.pdu.EnquireLink;
import com.logica.smpp.pdu.EnquireLinkResp;
import com.logica.smpp.pdu.PDU;
import com.logica.smpp.pdu.PDUException;
import com.logica.smpp.pdu.Request;
import com.logica.smpp.pdu.Response;
import com.logica.smpp.pdu.SubmitSM;
import com.logica.smpp.pdu.SubmitSMResp;
import com.logica.smpp.pdu.UnbindResp;
import com.logica.smpp.pdu.ValueNotSetException;
import com.logica.smpp.pdu.WrongDateFormatException;
import com.logica.smpp.pdu.WrongLengthOfStringException;
import com.logica.smpp.pdu.tlv.TLVException;
import com.logica.smpp.util.ByteBuffer;
import com.vuclip.smpp.client.CoreSMPPClient;
import com.vuclip.smpp.service.CoreSMPPService;
import com.vuclip.smpp.to.ConfigTO;
import com.vuclip.smpp.to.PDUTO;
import com.vuclip.smpp.to.SMPPReqTO;
import com.vuclip.smpp.to.SMPPRespTO;
import com.vuclip.smpp.util.SMPPPDUEventListener;
import com.vuclip.smpp.util.SMPPSession;

public class CoreSMPPServiceImpl implements CoreSMPPService, Runnable {

	private static final Logger logger = LoggerFactory.getLogger(CoreSMPPServiceImpl.class);

	private volatile Session session = null;

	private static SMPPPDUEventListener eventListener = null;

	private ConfigTO configTO;
	private SMPPReqTO smppReqTO;

	public CoreSMPPServiceImpl(ConfigTO configTO, SMPPReqTO smppReqTO) {
		this.configTO = configTO;
		this.smppReqTO = smppReqTO;
	}

	public boolean bind() {
		try {
			System.out.println("Bind Start");
			BindRequest request = null;
			BindResponse response = null;
			session = SMPPSession.getInstance(configTO.getIpAddress(), configTO.getPort());
			// SetParameters
			request = setBindParams(request);
			// send the request
			if (logger.isDebugEnabled()) {
				logger.debug(Thread.currentThread().getName() + ": Bind request " + request.debugString());
			}
			if (configTO.isAsynchorized()) {
				eventListener = new SMPPPDUEventListener(session);
				response = session.bind(request, eventListener);
			} else {
				response = session.bind(request);
			}
			if (logger.isDebugEnabled()) {
				logger.debug(Thread.currentThread().getName() + ": Bind response " + response.debugString());
			}
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error(e.getMessage());
			}
		}
		System.out.println("Bind End");
		return session.isBound();

	}

	private BindRequest setBindParams(BindRequest request) throws WrongLengthOfStringException {
		if (configTO.getBindOption().compareToIgnoreCase("t") == 0) {
			request = new BindTransmitter();
		} else if (configTO.getBindOption().compareToIgnoreCase("r") == 0) {
			request = new BindReceiver();
		} else if (configTO.getBindOption().compareToIgnoreCase("tr") == 0) {
			request = new BindTransciever();
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("Invalid bind mode, expected t, r or tr, got " + configTO.getBindOption()
						+ ". Operation canceled.");
			}
		}
		// set values
		request.setSystemId(configTO.getSystemId());
		request.setPassword(configTO.getPassword());
		request.setSystemType(configTO.getSystemType());
		request.setInterfaceVersion((byte) 0x34);
		// request.setAddressRange(addressRange);
		return request;
	}

	public void submit() {
		System.out.println("Submit SM Start");
		if (logger.isDebugEnabled()) {
			logger.debug("SMPPTest.submit()");
		}
		try {
			SubmitSM request = new SubmitSM();
			SubmitSMResp response;
			// set values
			setSubmitParam(request);
			// send the request
			if (logger.isDebugEnabled()) {
				logger.debug("Submit request " + request.debugString());
			}
			session = SMPPSession.getInstance(configTO.getIpAddress(), configTO.getPort());
			if (configTO.isAsynchorized()) {
				session.submit(request);
			} else {
				response = session.submit(request);
				if (logger.isDebugEnabled()) {
					logger.debug("Submit response " + response.debugString());
				}
				String messageId = response.getMessageId();
				SMPPRespTO smppRespTO = new SMPPRespTO();
				smppRespTO.setRespStatus(response.getCommandStatus());
				smppRespTO.setResponseId(response.getCommandId());
				smppRespTO.setResposeMessage(response.getBody().toString());
				CoreSMPPClient.smppRespQueue.add(smppRespTO);
			}
		} catch (WrongLengthOfStringException e) {
			if (logger.isErrorEnabled()) {
				logger.error("Submit operation failed. " + e);
			}
		} catch (ValueNotSetException e) {
			if (logger.isErrorEnabled()) {
				logger.error("Submit operation failed. " + e);
			}
		} catch (TimeoutException e) {
			if (logger.isErrorEnabled()) {
				logger.error("Submit operation failed. " + e);
			}
		} catch (PDUException e) {
			if (logger.isErrorEnabled()) {
				logger.error("Submit operation failed. " + e);
			}
		} catch (WrongSessionStateException e) {
			if (logger.isErrorEnabled()) {
				logger.error("Submit operation failed. " + e);
			}
		} catch (IOException e) {
			if (logger.isErrorEnabled()) {
				logger.error("Submit operation failed. " + e);
			}
		}
		System.out.println("Submit SM End");
	}

	private void setSubmitParam(SubmitSM request)
			throws WrongLengthOfStringException, UnsupportedEncodingException, WrongDateFormatException, TLVException {
		PDUTO pduto = smppReqTO.getPduto();
		// Set Config Specific Parameters
		Iterator<Map.Entry<Integer, Integer>> optionalParamMapIterator = configTO.getOptionalParamMap().entrySet()
				.iterator();
		while (optionalParamMapIterator.hasNext()) {
			Map.Entry<Integer, Integer> entry = optionalParamMapIterator.next();
			request.setExtraOptional((short) entry.getKey().intValue(),
					new ByteBuffer(java.nio.ByteBuffer.allocate(4).putInt(entry.getValue()).array()));
		}

		// Set Request SpeceficParameters
		request.setServiceType(pduto.getServiceType());
		request.setSourceAddr(pduto.getSourceAddress());
		request.setDestAddr(pduto.getDestAddress());
		request.setReplaceIfPresentFlag(pduto.getReplaceIfPresentFlag());
		request.setShortMessage(pduto.getShortMessage(), Data.ENC_UTF8);
		request.setMessagePayload(new ByteBuffer(pduto.getMessagePayload().getBytes()));
		request.setScheduleDeliveryTime(pduto.getScheduleDeliveryTime());
		request.setValidityPeriod(pduto.getValidityPeriod());
		request.setEsmClass(pduto.getEsmClass());
		request.setProtocolId(pduto.getProtocolId());
		request.setPriorityFlag(pduto.getPriorityFlag());
		request.setRegisteredDelivery(pduto.getRegisteredDelivery());
		request.setDataCoding(pduto.getDataCoding());
		request.setSmDefaultMsgId(pduto.getSmDefaultMsgId());
		request.assignSequenceNumber(true);
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
			if (configTO.isAsynchorized()) {
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

	public SMPPRespTO receiveListener() {
		try {
			// Check if session is currently running
			if (!session.isBound()) {
				this.bind();
			}
			// With Data.RECEIVE_BLOCKING thread will block forever to receive
			PDU pdu = null;
			System.out.print("Going to receive a PDU. ");
			if (configTO.isAsynchorized()) {
				ServerPDUEvent pduEvent = eventListener.getRequestEvent(Data.RECEIVE_BLOCKING);
				if (pduEvent != null) {
					pdu = pduEvent.getPDU();
				}
			} else {
				pdu = session.receive(Data.RECEIVE_BLOCKING);
			}
			if (pdu != null) {
				SMPPRespTO smppRespTO = new SMPPRespTO();
				System.out.println("Received PDU " + pdu.debugString());
				if (pdu.isRequest()) {
					Response response = ((Request) pdu).getResponse();
					// respond with default response
					System.out.println("Going to send default response to request " + response.debugString());
					session.respond(response);
					smppRespTO.setResposeMessage(response.getBody().toString());
					smppRespTO.setRespStatus(response.getCommandStatus());
					smppRespTO.setResponseId(response.getCommandId());
				} else {
					smppRespTO.setResposeMessage(pdu.getData().toString());
					smppRespTO.setRespStatus(pdu.getCommandStatus());
					smppRespTO.setResponseId(pdu.getCommandId());
				}
				return smppRespTO;
			} else {
				System.out.println("No PDU received this time.");
			}
		} catch (Exception e) {
			System.out.println("Receiving failed. " + e);
		}
		return null;
	}

	public void run() {
		synchronized (CoreSMPPServiceImpl.class) {

			if (session == null || (session != null && !session.isBound())) {
				bind();
			}
			submit();
		}
	}

}
