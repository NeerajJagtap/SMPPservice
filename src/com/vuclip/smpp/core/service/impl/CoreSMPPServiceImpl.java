package com.vuclip.smpp.core.service.impl;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.smpp.Data;
import org.smpp.NotSynchronousException;
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
import org.smpp.pdu.UnknownCommandIdException;
import org.smpp.pdu.ValueNotSetException;
import org.smpp.pdu.WrongDateFormatException;
import org.smpp.pdu.WrongLengthOfStringException;
import org.smpp.pdu.tlv.TLVException;
import org.smpp.util.ByteBuffer;

import com.vuclip.smpp.core.service.CoreSMPPService;
import com.vuclip.smpp.core.to.DeliveryNotificationTO;
import com.vuclip.smpp.core.to.SMPPReqTO;
import com.vuclip.smpp.core.to.SMPPRespTO;
import com.vuclip.smpp.core.util.SMPPPDUEventListener;
import com.vuclip.smpp.core.util.SMPPSession;
import com.vuclip.smpp.exceptions.SMPPException;
import com.vuclip.smpp.exceptions.constant.SMPPExceptionConstant;
import com.vuclip.smpp.props.SMPPProperties;

public class CoreSMPPServiceImpl implements CoreSMPPService {

	Logger smpplogger = LogManager.getLogger("smpplogger");

	private volatile Session session = null;

	private static SMPPPDUEventListener eventListener = null;

	private SMPPProperties smppProperties;

	private String msisdn = null;

	private static final String MESSAGE_ID = "id:";

	private Object mutex = new Object();

	public CoreSMPPServiceImpl(SMPPProperties smppProperties) {
		this.smppProperties = smppProperties;
	}

	private boolean bind() throws SMPPException {
		try {
			System.out.println("Bind Start");
			if (smpplogger.isDebugEnabled()) {
				smpplogger.debug("In CoreSMPPServiceImpl : [" + msisdn + "] Bind request Start.");
			}
			BindRequest request = null;
			BindResponse response = null;
			session = SMPPSession.getInstance(smppProperties.getSmppServerIP(),
					Integer.parseInt(smppProperties.getSmppServerPort()));
			// SetParameters
			request = setBindParams(request);
			// send the request
			if (smpplogger.isDebugEnabled()) {
				smpplogger.debug("In CoreSMPPServiceImpl : [" + msisdn + "] Bind request " + request.debugString());
			}
			if (smppProperties.isAsynchorized()) {
				eventListener = new SMPPPDUEventListener(session);
				response = session.bind(request, eventListener);
			} else {
				response = session.bind(request);
			}
			if (smpplogger.isDebugEnabled()) {
				smpplogger.debug("In CoreSMPPServiceImpl : [" + msisdn + "] Bind response " + response.debugString());
			}
		} catch (PDUException e) {
			throw new SMPPException(SMPPExceptionConstant.BIND_PDU_EXCEPTION, e.getMessage());
		} catch (TimeoutException e) {
			throw new SMPPException(SMPPExceptionConstant.BIND_TIMEOUT_EXCEPTION, e.getMessage());
		} catch (WrongSessionStateException e) {
			throw new SMPPException(SMPPExceptionConstant.BIND_WRONG_SESSION_STATE_EXCEPTION, e.getMessage());
		} catch (IOException e) {
			throw new SMPPException(SMPPExceptionConstant.BIND_IO_EXCEPTION, e.getMessage());
		}
		System.out.println("Bind End");
		if (smpplogger.isDebugEnabled()) {
			smpplogger.debug("[" + msisdn + "] Bind request End.");
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
			if (smpplogger.isDebugEnabled()) {
				smpplogger.debug("In CoreSMPPServiceImpl : [" + msisdn
						+ "] Invalid bind mode, expected t, r or tr, got " + smppBindOption + ". Operation canceled.");
			}
		}
		// set values
		request.setSystemId(smppProperties.getSystemID());
		request.setPassword(smppProperties.getPassword());
		request.setSystemType(smppProperties.getSystemType());
		request.setInterfaceVersion((byte) 0x34);
		return request;
	}

	private SMPPRespTO submitSync(SMPPReqTO smppReqTO) throws SMPPException {
		System.out.println("Submit SM Start.");
		SMPPRespTO smppRespTO = null;
		if (smpplogger.isDebugEnabled()) {
			smpplogger.debug("In CoreSMPPServiceImpl : [" + msisdn + "] Submit SM Start.");
		}
		SubmitSM request = new SubmitSM();
		SubmitSMResp response;
		if (null == session)
			session = SMPPSession.getInstance(smppProperties.getSmppServerIP(),
					Integer.parseInt(smppProperties.getSmppServerPort()));
		// set values
		setSubmitParameters(request, smppReqTO);
		// send the request
		try {
			if (smpplogger.isDebugEnabled()) {
				smpplogger
						.debug("In CoreSMPPServiceImpl : [" + msisdn + "] Submit_SM request " + request.debugString());
			}
			if (smppProperties.isAsynchorized()) {
				session.submit(request);
			} else {
				response = session.submit(request);
				if (smpplogger.isDebugEnabled()) {
					smpplogger.debug(
							"In CoreSMPPServiceImpl : [" + msisdn + "] Submit_SM response " + response.debugString());
				}
				smppRespTO = new SMPPRespTO();
				SMPPRespTO expectedRespTO = smppReqTO.getExpetedResponseTO();
				smppRespTO.setDlrURL(expectedRespTO.getDlrURL());
				smppRespTO.setMsisdn(expectedRespTO.getMsisdn());
				smppRespTO.setPricePoint(expectedRespTO.getPricePoint());
				smppRespTO.setTransId(expectedRespTO.getTransId());
				smppRespTO.setRespStatus(response.getCommandStatus());
				smppRespTO.setResponseId(response.getCommandId());
				smppRespTO.setResponseMsgId(response.getMessageId());
			}
		} catch (PDUException e) {
			throw new SMPPException(SMPPExceptionConstant.SUBMIT_SM_PDU_EXCEPTION, e.getMessage());
		} catch (TimeoutException e) {
			throw new SMPPException(SMPPExceptionConstant.SUBMIT_SM_TIMEOUT_EXCEPTION, e.getMessage());
		} catch (WrongSessionStateException e) {
			throw new SMPPException(SMPPExceptionConstant.SUBMIT_SM_WRONG_SESSION_STATE_EXCEPTION, e.getMessage());
		} catch (IOException e) {
			throw new SMPPException(SMPPExceptionConstant.SUBMIT_SM_IO_EXCEPTION, e.getMessage());
		}

		System.out.println("Submit SM End");

		if (smpplogger.isDebugEnabled()) {
			smpplogger.debug("In CoreSMPPServiceImpl : [" + msisdn + "] Submit SM End.");
		}
		return smppRespTO;
	}

	private void setSubmitParameters(SubmitSM request, SMPPReqTO smppReqTO) throws SMPPException {
		try {
			// Set Request SpeceficParameters
			request.setServiceType(smppReqTO.getServiceType());
			request.setSourceAddr(smppReqTO.getSourceAddress());
			request.setDestAddr(smppReqTO.getDestAddress());
			request.setReplaceIfPresentFlag(smppReqTO.getReplaceIfPresentFlag());
			request.setShortMessage(smppReqTO.getShortMessage());
			request.setScheduleDeliveryTime(smppReqTO.getScheduleDeliveryTime());
			request.setValidityPeriod(smppReqTO.getValidityPeriod());
			request.setEsmClass(smppReqTO.getEsmClass());
			request.setProtocolId(smppReqTO.getProtocolId());
			request.setPriorityFlag(smppReqTO.getPriorityFlag());
			request.setRegisteredDelivery(smppReqTO.getRegisteredDelivery());
			request.setDataCoding(smppReqTO.getDataCoding());
			request.setSmDefaultMsgId(smppReqTO.getSmDefaultMsgId());
			request.setSequenceNumber(1);
			request.assignSequenceNumber(true);
			// Set Config Specific Parameters
			Iterator<Map.Entry<Integer, Integer>> optionalParamMapIterator = smppProperties.getOptionalParamMap()
					.entrySet().iterator();
			while (optionalParamMapIterator.hasNext()) {
				Map.Entry<Integer, Integer> entry = optionalParamMapIterator.next();
				request.setExtraOptional((short) entry.getKey().intValue(),
						new ByteBuffer(java.nio.ByteBuffer.allocate(4).putInt(entry.getValue()).array()));
			}
			// Payload
			request.setMessagePayload(new ByteBuffer(smppReqTO.getMessagePayload().getBytes()));
		} catch (TLVException e) {
			throw new SMPPException(SMPPExceptionConstant.TLV_EXCEPTION, e.getMessage());
		} catch (WrongDateFormatException e) {
			throw new SMPPException(SMPPExceptionConstant.WRONG_DATE_FORMAT_EXCEPTION, e.getMessage());
		} catch (WrongLengthOfStringException e) {
			throw new SMPPException(SMPPExceptionConstant.WRONG_LENGTH_OF_STRING_EXCEPTION, e.getMessage());
		}
	}

	private boolean enquire() throws SMPPException {
		if (null == session) {
			return false;
		}
		if (!session.isBound()) {
			return false;
		}
		if (smpplogger.isDebugEnabled()) {
			smpplogger.debug("In CoreSMPPServiceImpl : [" + msisdn + "] Enquire Link Start.");
		}
		try {

			EnquireLink request = new EnquireLink();
			EnquireLinkResp response;
			if (smpplogger.isDebugEnabled()) {
				smpplogger.debug(
						"In CoreSMPPServiceImpl : [" + msisdn + "] Enquire Link request " + request.debugString());
			}
			if (smppProperties.isAsynchorized()) {
				session.enquireLink(request);
			} else {
				response = session.enquireLink(request);
				smpplogger.debug(
						"In CoreSMPPServiceImpl : [" + msisdn + "] Enquire Link response " + response.debugString());
				return response.getCommandStatus() == Data.ESME_ROK ? true : false;
			}
			return true;
		} catch (PDUException e) {
			throw new SMPPException(SMPPExceptionConstant.ENQUIRE_PDU_EXCEPTION, e.getMessage());
		} catch (TimeoutException e) {
			throw new SMPPException(SMPPExceptionConstant.ENQUIRE_TIMEOUT_EXCEPTION, e.getMessage());
		} catch (WrongSessionStateException e) {
			throw new SMPPException(SMPPExceptionConstant.ENQUIRE_WRONG_SESSION_STATE_EXCEPTION, e.getMessage());
		} catch (IOException e) {
			throw new SMPPException(SMPPExceptionConstant.ENQUIRE_IO_EXCEPTION, e.getMessage());
		}
	}

	private boolean unbind() throws SMPPException {
		if (!session.isBound()) {
			return true;
		}
		try {
			// send the request
			if (smpplogger.isDebugEnabled()) {
				smpplogger.debug("In CoreSMPPServiceImpl : " + Thread.currentThread().getName() + "[" + msisdn
						+ "] : Going to unbind. Session.");
			}
			/**
			 * if (session.getReceiver().isReceiver()) {
			 * log.log(Thread.currentThread().getName() + ": It can take a while
			 * to stop the receiver."); }
			 **/
			UnbindResp response = null;
			response = session.unbind();
			if (smpplogger.isDebugEnabled()) {
				smpplogger.debug("In CoreSMPPServiceImpl : " + Thread.currentThread().getName() + "[" + msisdn
						+ "] : Unbind response " + (null != response ? response.debugString() : "is null"));
			}
		} catch (IOException e) {
			throw new SMPPException(SMPPExceptionConstant.UNBIND_IO_EXCEPTION, e.getMessage());
		} catch (ValueNotSetException e) {
			throw new SMPPException(SMPPExceptionConstant.UNBIND_VALUE_NOT_SET_EXCEPTION, e.getMessage());
		} catch (TimeoutException e) {
			throw new SMPPException(SMPPExceptionConstant.UNBIND_TIMEOUT_EXCEPTION, e.getMessage());
		} catch (PDUException e) {
			throw new SMPPException(SMPPExceptionConstant.UNBIND_PDU_EXCEPTION, e.getMessage());
		} catch (WrongSessionStateException e) {
			throw new SMPPException(SMPPExceptionConstant.UNBIND_WRONG_SESSION_STATE_EXCEPTION, e.getMessage());
		}
		return session.isBound();
	}

	private DeliveryNotificationTO receiveListener() throws SMPPException {
		try {
			if (!session.isBound()) {
				this.bind();
			}
			if (smpplogger.isDebugEnabled()) {
				smpplogger.debug("In CoreSMPPServiceImpl : Receiver Started.");
			}

			PDU pdu = null;

			if (smpplogger.isDebugEnabled()) {
				smpplogger.debug("In CoreSMPPServiceImpl : Going to receive a PDU. ");
			}
			if (smppProperties.isAsynchorized()) {
				ServerPDUEvent pduEvent = eventListener.getRequestEvent(Data.RECEIVE_BLOCKING);
				if (pduEvent != null) {
					pdu = pduEvent.getPDU();
				}
			} else {
				pdu = session.receive(100);
			}
			// Take only Deliver SM
			if (pdu != null && pdu.getCommandId() == Data.DELIVER_SM && pdu.getCommandStatus() == Data.ESME_ROK) {
				DeliverSM deliverSM = (DeliverSM) pdu;
				DeliveryNotificationTO dnto = new DeliveryNotificationTO();

				if (smpplogger.isDebugEnabled()) {
					smpplogger.debug("In CoreSMPPServiceImpl : Received PDU " + pdu.debugString());
				}

				if (deliverSM.isRequest()) {
					Response response = ((Request) deliverSM).getResponse();
					// respond with default response
					if (smpplogger.isDebugEnabled()) {
						smpplogger.debug("In CoreSMPPServiceImpl : Going to send default response to request "
								+ response.debugString());
					}
					session.respond(response);
					dnto.setResponseToCarrier(response.debugString());
				}
				dnto.setMsisdn(deliverSM.getSourceAddr().getAddress());
				String shortMessage = deliverSM.getShortMessage();
				dnto.setResponseDNString(shortMessage);
				dnto.setDeliveryStatus(deliverSM.getCommandStatus());
				String messageIDFromString = getMessageIDFromString(shortMessage);
				dnto.setMessageId(messageIDFromString);
				dnto.setMO(null != messageIDFromString ? false : true);
				if (smpplogger.isDebugEnabled()) {
					smpplogger.debug("In CoreSMPPServiceImpl : Receiver End Success.");
				}
				return dnto;
			} else {
				if (smpplogger.isDebugEnabled()) {
					smpplogger.debug("In CoreSMPPServiceImpl : No PDU received this time.");
				}
			}
		} catch (IOException e) {
			throw new SMPPException(SMPPExceptionConstant.LISTENER_IO_EXCEPTION, e.getMessage());
		} catch (WrongSessionStateException e) {
			throw new SMPPException(SMPPExceptionConstant.LISTENER_WRONG_SESSION_STATE_EXCEPTION, e.getMessage());
		} catch (ValueNotSetException e) {
			throw new SMPPException(SMPPExceptionConstant.LISTENER_VALUE_NOT_SET_EXCEPTION, e.getMessage());
		} catch (UnknownCommandIdException e) {
			throw new SMPPException(SMPPExceptionConstant.LISTENER_UNKNOWN_COMMAND_ID_EXCEPTION, e.getMessage());
		} catch (TimeoutException e) {
			throw new SMPPException(SMPPExceptionConstant.LISTENER_TIMEOUT_EXCEPTION, e.getMessage());
		} catch (NotSynchronousException e) {
			throw new SMPPException(SMPPExceptionConstant.LISTENER_NOT_SYNC_EXCEPTION, e.getMessage());
		} catch (PDUException e) {
			throw new SMPPException(SMPPExceptionConstant.LISTENER_PDU_EXCEPTION, e.getMessage());
		}
		return null;
	}

	private String getMessageIDFromString(String shortMessage) {
		if (shortMessage.contains(MESSAGE_ID)) {
			return shortMessage.split(" ")[0].split(":")[1];
		} else {
			return null;
		}
	}

	public SMPPRespTO submitMessagePDU(SMPPReqTO smppReqTO) throws SMPPException {
		SMPPRespTO smppRespTO = null;
		synchronized (mutex) {
			this.msisdn = smppReqTO.getExpetedResponseTO().getMsisdn();
			if (!enquire()) {
				bind();
			}
			smppRespTO = submitSync(smppReqTO);
		}
		return smppRespTO;
	}

	public DeliveryNotificationTO getDeliveryNotification() throws SMPPException {
		DeliveryNotificationTO deliveryNotificationTO = null;
		synchronized (mutex) {
			deliveryNotificationTO = receiveListener();
		}
		return deliveryNotificationTO;
	}
}
