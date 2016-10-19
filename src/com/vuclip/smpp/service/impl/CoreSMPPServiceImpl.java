package com.vuclip.smpp.service.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;
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
import org.smpp.pdu.tlv.TLVOctets;
import org.smpp.util.ByteBuffer;

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

	public CoreSMPPServiceImpl(ConfigTO configTO) {
		this.configTO = configTO;
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
		request.setCommandId(9);
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
			session = SMPPSession.getInstance(configTO.getIpAddress(), configTO.getPort());

			List<String> splitMessges = smppReqTO.getPduto().getSplitMessges();
			ByteBuffer byteBuffer = null;
			int i = 0;
			int totalMessages = splitMessges.size();
			for (String str : splitMessges) {
				byteBuffer = new ByteBuffer();
				byteBuffer.appendByte((byte) 5); // UDH Length

				byteBuffer.appendByte((byte) 0x00); // IE Identifier

				byteBuffer.appendByte((byte) 3); // IE Data Length

				byteBuffer.appendByte((byte) 2); // Reference Number

				byteBuffer.appendByte((byte) totalMessages); // Number of pieces

				byteBuffer.appendByte((byte) (++i)); // Sequence number

				byteBuffer.appendString(str, Data.ENC_ASCII);
				// set values
				setSubmitParam(request, byteBuffer);
				// send the request
				if (logger.isDebugEnabled()) {
					logger.debug("Submit request " + request.debugString());
				}
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

	private void setSubmitParam(SubmitSM request, ByteBuffer message)
			throws WrongLengthOfStringException, UnsupportedEncodingException, WrongDateFormatException, TLVException {
		PDUTO pduto = smppReqTO.getPduto();
		// Set Request SpeceficParameters
		request.setServiceType(pduto.getServiceType());
		request.setSourceAddr(pduto.getSourceAddress());
		request.setDestAddr(pduto.getDestAddress());
		request.setReplaceIfPresentFlag(pduto.getReplaceIfPresentFlag());
		// request.setShortMessage(pduto.getShortMessage(), Data.ENC_UTF8);
		// request.setMessagePayload(new
		// ByteBuffer(pduto.getMessagePayload().getBytes()));
		request.setShortMessage("");
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
		// Set Config Specific Parameters
		Iterator<Map.Entry<Integer, Integer>> optionalParamMapIterator = configTO.getOptionalParamMap().entrySet()
				.iterator();
		while (optionalParamMapIterator.hasNext()) {
			Map.Entry<Integer, Integer> entry = optionalParamMapIterator.next();
			request.setExtraOptional((short) entry.getKey().intValue(),
					new ByteBuffer(java.nio.ByteBuffer.allocate(4).putInt(entry.getValue()).array()));
		}
	}

	public SMPPRespTO submitSync()
			throws ValueNotSetException, TimeoutException, PDUException, WrongSessionStateException, IOException {
		System.out.println("Submit SM Start");
		SMPPRespTO smppRespTO = null;
		if (logger.isDebugEnabled()) {
			logger.debug("SMPPTest.submit()");
		}
		SubmitSM request = new SubmitSM();
		SubmitSMResp response;
		session = SMPPSession.getInstance(configTO.getIpAddress(), configTO.getPort());
		// set values
		setSubmitParameters(request);
		// send the request
		if (logger.isDebugEnabled()) {
			logger.debug("Submit request " + request.debugString());
		}
		if (configTO.isAsynchorized()) {
			session.submit(request);
		} else {
			response = session.submit(request);
			if (logger.isDebugEnabled()) {
				logger.debug("Submit response " + response.debugString());
			}
			String messageId = response.getMessageId();
			smppRespTO = new SMPPRespTO();
			smppRespTO.setRespStatus(response.getCommandStatus());
			smppRespTO.setResponseId(response.getCommandId());
			smppRespTO.setResposeMessage(response.getBody().toString());
			CoreSMPPClient.smppRespQueue.add(smppRespTO);
		}
		System.out.println("Submit SM End");
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
		// request.setShortMessage(pduto.getShortMessage(), Data.ENC_UTF8);
		// request.setMessagePayload(new
		// ByteBuffer(pduto.getMessagePayload().getBytes()));
		request.setShortMessage("");
		// Payload
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
		Iterator<Map.Entry<Integer, Integer>> optionalParamMapIterator = configTO.getOptionalParamMap().entrySet()
				.iterator();
		while (optionalParamMapIterator.hasNext()) {
			Map.Entry<Integer, Integer> entry = optionalParamMapIterator.next();
			request.setExtraOptional((short) entry.getKey().intValue(),
					new ByteBuffer(java.nio.ByteBuffer.allocate(4).putInt(entry.getValue()).array()));
		}
		ByteBuffer messagePayloadBytes = new ByteBuffer(pduto.getMessagePayload().getBytes());
		TLVOctets octets = new TLVOctets((short) 0x0424, (short) -1, (short) messagePayloadBytes.length(),
				messagePayloadBytes);
		request.setMessagePayload(new ByteBuffer(pduto.getMessagePayload().getBytes()));
		request.setExtraOptional(octets);
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
		if (null != session) {
			try {
				// Check if session is currently running
				if (!session.isBound()) {
					this.bind();
				}
				// With Data.RECEIVE_BLOCKING thread will block forever to
				// receive
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
				e.printStackTrace();
			}
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

	public SMPPRespTO submitMessagePDU()
			throws ValueNotSetException, TimeoutException, PDUException, WrongSessionStateException, IOException {
		if (session == null || (session != null && !session.isBound())) {
			bind();
		}
		return submitSync();
	}
}
