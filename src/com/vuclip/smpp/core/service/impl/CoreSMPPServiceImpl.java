package com.vuclip.smpp.core.service.impl;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.vuclip.smpp.exceptions.SMPPExceptionJava;
import com.vuclip.smpp.exceptions.constant.SMPPExceptionConstant;
import com.vuclip.smpp.props.SMPPProperties;

public class CoreSMPPServiceImpl implements CoreSMPPService {

	private static final Logger logger = LoggerFactory.getLogger(CoreSMPPServiceImpl.class);

	private volatile Session session = null;

	private static SMPPPDUEventListener eventListener = null;

	private SMPPProperties smppProperties;

	private SMPPReqTO smppReqTO;

	private String msisdn;

	public CoreSMPPServiceImpl(SMPPProperties smppProperties) {
		this.smppProperties = smppProperties;
	}

	public SMPPReqTO getSmppReqTO() {
		return smppReqTO;
	}

	public void setSmppReqTO(SMPPReqTO smppReqTO) {
		this.smppReqTO = smppReqTO;
		this.msisdn = smppReqTO.getDestAddress().getAddress();
	}

	public boolean bind() throws SMPPExceptionJava {
		try {
			System.out.println("Bind Start");
			if (logger.isDebugEnabled()) {
				logger.debug("[" + msisdn + "] Bind request Start.");
			}
			BindRequest request = null;
			BindResponse response = null;
			session = SMPPSession.getInstance(smppProperties.getSmppServerIP(),
					Integer.parseInt(smppProperties.getSmppServerPort()));
			// SetParameters
			request = setBindParams(request);
			// send the request
			if (logger.isDebugEnabled()) {
				logger.debug("[" + msisdn + "] Bind request " + request.debugString());
			}
			if (smppProperties.isAsynchorized()) {
				eventListener = new SMPPPDUEventListener(session);
				response = session.bind(request, eventListener);
			} else {
				response = session.bind(request);
			}
			if (logger.isDebugEnabled()) {
				logger.debug("[" + msisdn + "] Bind response " + response.debugString());
			}
		} catch (PDUException e) {
			throw new SMPPExceptionJava(SMPPExceptionConstant.BIND_PDU_EXCEPTION, e.getMessage());
		} catch (TimeoutException e) {
			throw new SMPPExceptionJava(SMPPExceptionConstant.BIND_TIMEOUT_EXCEPTION, e.getMessage());
		} catch (WrongSessionStateException e) {
			throw new SMPPExceptionJava(SMPPExceptionConstant.BIND_WRONG_SESSION_STATE_EXCEPTION, e.getMessage());
		} catch (IOException e) {
			throw new SMPPExceptionJava(SMPPExceptionConstant.BIND_IO_EXCEPTION, e.getMessage());
		}
		System.out.println("Bind End");
		if (logger.isDebugEnabled()) {
			logger.debug("[" + msisdn + "] Bind request End.");
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
				logger.debug("[" + msisdn + "] Invalid bind mode, expected t, r or tr, got " + smppBindOption
						+ ". Operation canceled.");
			}
		}
		// set values
		request.setSystemId(smppProperties.getSystemID());
		request.setPassword(smppProperties.getPassword());
		request.setSystemType(smppProperties.getSystemType());
		request.setInterfaceVersion((byte) 0x34);
		return request;
	}

	public SMPPRespTO submitSync() throws SMPPExceptionJava {
		System.out.println("Submit SM Start.");
		SMPPRespTO smppRespTO = null;
		if (logger.isDebugEnabled()) {
			logger.debug("[" + msisdn + "] Submit SM Start.");
		}
		SubmitSM request = new SubmitSM();
		SubmitSMResp response;
		if (null == session)
			session = SMPPSession.getInstance(smppProperties.getSmppServerIP(),
					Integer.parseInt(smppProperties.getSmppServerPort()));
		// set values
		setSubmitParameters(request);
		// send the request
		try {
			if (logger.isDebugEnabled()) {
				logger.debug("[" + msisdn + "] Submit_SM request " + request.debugString());
			}
			if (smppProperties.isAsynchorized()) {
				session.submit(request);
			} else {
				response = session.submit(request);
				if (logger.isDebugEnabled()) {
					logger.debug("[" + msisdn + "] Submit_SM response " + response.debugString());
				}
				smppRespTO = new SMPPRespTO();
				smppRespTO.setRespStatus(response.getCommandStatus());
				smppRespTO.setResponseId(response.getCommandId());
				smppRespTO.setResponseMsgId(response.getMessageId());
			}
		} catch (PDUException e) {
			throw new SMPPExceptionJava(SMPPExceptionConstant.SUBMIT_SM_PDU_EXCEPTION, e.getMessage());
		} catch (TimeoutException e) {
			throw new SMPPExceptionJava(SMPPExceptionConstant.SUBMIT_SM_TIMEOUT_EXCEPTION, e.getMessage());
		} catch (WrongSessionStateException e) {
			throw new SMPPExceptionJava(SMPPExceptionConstant.SUBMIT_SM_WRONG_SESSION_STATE_EXCEPTION, e.getMessage());
		} catch (IOException e) {
			throw new SMPPExceptionJava(SMPPExceptionConstant.SUBMIT_SM_IO_EXCEPTION, e.getMessage());
		}
		System.out.println("Submit SM End");
		if (logger.isDebugEnabled()) {
			logger.debug("[" + msisdn + "] Submit SM End.");
		}
		return smppRespTO;
	}

	private void setSubmitParameters(SubmitSM request) throws SMPPExceptionJava {
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
			throw new SMPPExceptionJava(SMPPExceptionConstant.TLV_EXCEPTION, e.getMessage());
		} catch (WrongDateFormatException e) {
			throw new SMPPExceptionJava(SMPPExceptionConstant.WRONG_DATE_FORMAT_EXCEPTION, e.getMessage());
		} catch (WrongLengthOfStringException e) {
			throw new SMPPExceptionJava(SMPPExceptionConstant.WRONG_LENGTH_OF_STRING_EXCEPTION, e.getMessage());
		}
	}

	public boolean enquire() throws SMPPExceptionJava {
		if (logger.isDebugEnabled()) {
			logger.debug("[" + msisdn + "] Enquire Link Start.");
		}
		try {

			EnquireLink request = new EnquireLink();
			EnquireLinkResp response;
			if (logger.isDebugEnabled()) {
				logger.debug("[" + msisdn + "] Enquire Link request " + request.debugString());
			}
			if (smppProperties.isAsynchorized()) {
				session.enquireLink(request);
			} else {
				response = session.enquireLink(request);
				logger.debug("[" + msisdn + "] Enquire Link response " + response.debugString());
				return response.getCommandStatus() == Data.ESME_ROK ? true : false;
			}
			return true;
		} catch (PDUException e) {
			throw new SMPPExceptionJava(SMPPExceptionConstant.ENQUIRE_PDU_EXCEPTION, e.getMessage());
		} catch (TimeoutException e) {
			throw new SMPPExceptionJava(SMPPExceptionConstant.ENQUIRE_TIMEOUT_EXCEPTION, e.getMessage());
		} catch (WrongSessionStateException e) {
			throw new SMPPExceptionJava(SMPPExceptionConstant.ENQUIRE_WRONG_SESSION_STATE_EXCEPTION, e.getMessage());
		} catch (IOException e) {
			throw new SMPPExceptionJava(SMPPExceptionConstant.ENQUIRE_IO_EXCEPTION, e.getMessage());
		}
	}

	public boolean unbind() throws SMPPExceptionJava {
		if (!session.isBound()) {
			return true;
		}
		try {
			// send the request
			if (logger.isDebugEnabled()) {
				logger.debug(Thread.currentThread().getName() + "[" + msisdn + "] : Going to unbind. Session.");
			}
			/**
			 * if (session.getReceiver().isReceiver()) {
			 * log.log(Thread.currentThread().getName() + ": It can take a while
			 * to stop the receiver."); }
			 **/
			UnbindResp response = session.unbind();
			if (logger.isDebugEnabled()) {
				logger.debug(Thread.currentThread().getName() + "[" + msisdn + "] : Unbind response "
						+ response.debugString());
			}
		} catch (IOException e) {
			throw new SMPPExceptionJava(SMPPExceptionConstant.UNBIND_IO_EXCEPTION, e.getMessage());
		} catch (ValueNotSetException e) {
			throw new SMPPExceptionJava(SMPPExceptionConstant.UNBIND_VALUE_NOT_SET_EXCEPTION, e.getMessage());
		} catch (TimeoutException e) {
			throw new SMPPExceptionJava(SMPPExceptionConstant.UNBIND_TIMEOUT_EXCEPTION, e.getMessage());
		} catch (PDUException e) {
			throw new SMPPExceptionJava(SMPPExceptionConstant.UNBIND_PDU_EXCEPTION, e.getMessage());
		} catch (WrongSessionStateException e) {
			throw new SMPPExceptionJava(SMPPExceptionConstant.UNBIND_WRONG_SESSION_STATE_EXCEPTION, e.getMessage());
		}
		return session.isBound();
	}

	public DeliveryNotificationTO receiveListener() throws SMPPExceptionJava {
		if (null != session && session.isBound()) {
			try {
				// if (!session.isBound()) {
				// this.bind();
				// }
				if (logger.isDebugEnabled()) {
					logger.debug("[" + msisdn + "] Receiver Started.");
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
						dnto.setResponseToCarrier(response.debugString());
					}
					dnto.setMsisdn(deliverSM.getSourceAddr().getAddress());
					String shortMessage = deliverSM.getShortMessage();
					dnto.setResponseDNString(shortMessage);
					dnto.setDeliveryStatus(deliverSM.getCommandStatus());
					dnto.setMessageId(getMessageIDFromString(shortMessage));
					unbind();
					session.close();
					if (logger.isDebugEnabled()) {
						logger.debug("[" + msisdn + "] Receiver End Success.");
					}
					return dnto;
				} else {
					System.out.println("No PDU received this time.");
				}
				unbind();
				session.close();
			} catch (IOException e) {
				throw new SMPPExceptionJava(SMPPExceptionConstant.LISTENER_IO_EXCEPTION, e.getMessage());
			} catch (SMPPExceptionJava e) {
				throw new SMPPExceptionJava(SMPPExceptionConstant.LISTENER_SMPP_NESTED_EXCEPTION, e.getMessage());
			} catch (WrongSessionStateException e) {
				throw new SMPPExceptionJava(SMPPExceptionConstant.LISTENER_WRONG_SESSION_STATE_EXCEPTION,
						e.getMessage());
			} catch (ValueNotSetException e) {
				throw new SMPPExceptionJava(SMPPExceptionConstant.LISTENER_VALUE_NOT_SET_EXCEPTION, e.getMessage());
			} catch (UnknownCommandIdException e) {
				throw new SMPPExceptionJava(SMPPExceptionConstant.LISTENER_UNKNOWN_COMMAND_ID_EXCEPTION,
						e.getMessage());
			} catch (TimeoutException e) {
				throw new SMPPExceptionJava(SMPPExceptionConstant.LISTENER_TIMEOUT_EXCEPTION, e.getMessage());
			} catch (NotSynchronousException e) {
				throw new SMPPExceptionJava(SMPPExceptionConstant.LISTENER_NOT_SYNC_EXCEPTION, e.getMessage());
			} catch (PDUException e) {
				throw new SMPPExceptionJava(SMPPExceptionConstant.LISTENER_PDU_EXCEPTION, e.getMessage());
			}
		}
		return null;
	}

	private String getMessageIDFromString(String shortMessage) {
		return shortMessage.split(" ")[0].split(":")[1];
	}

	public SMPPRespTO submitMessagePDU() throws SMPPExceptionJava {
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
