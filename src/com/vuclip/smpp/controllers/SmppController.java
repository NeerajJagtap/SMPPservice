package com.vuclip.smpp.controllers;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.smpp.pdu.Address;
import org.smpp.pdu.WrongLengthOfStringException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.vuclip.smpp.config.SmppConfig;
import com.vuclip.smpp.core.handler.CoreSMPPHandler;
import com.vuclip.smpp.core.to.SMPPReqTO;
import com.vuclip.smpp.core.to.SMPPRespTO;
import com.vuclip.smpp.exceptions.SMPPException;
import com.vuclip.smpp.orm.dto.SmppData;
import com.vuclip.smpp.props.SMPPProperties;
import com.vuclip.smpp.service.SmppService;
import com.vuclip.smpp.util.LoggingBean;
import com.vuclip.smpp.util.SmppUtil;

/**
 * @author Devendra
 *
 */

@Controller
public class SmppController {

	private static final Logger SMPPLOGGER = LogManager.getLogger("smpplogger");

	private static final Logger SENDSMSLOGGER = LogManager.getLogger("sendsmslogger");

	@Autowired
	private LoggingBean loggingBean;

	@Autowired
	private SMPPProperties smppProperties;

	@Autowired
	private SmppConfig smppConfig;

	CoreSMPPHandler coreSMPPHandlerActivation[] = null;

	CoreSMPPHandler coreSMPPHandlerDeactivation[] = null;

	CoreSMPPHandler coreSMPPHandlerRenewal[] = null;

	CoreSMPPHandler coreSMPPHandlerOtherJobs[] = null;

	int activations = 0, deactivations = 0, renewals = 0, others = 0;

	@Autowired
	private SmppService smppService;

	public void initializeHandlers() {
		if (null == coreSMPPHandlerActivation || null == coreSMPPHandlerDeactivation || null == coreSMPPHandlerRenewal
				|| null == coreSMPPHandlerOtherJobs) {
			coreSMPPHandlerActivation = new CoreSMPPHandler[10];
			coreSMPPHandlerDeactivation = new CoreSMPPHandler[10];
			coreSMPPHandlerRenewal = new CoreSMPPHandler[20];
			coreSMPPHandlerOtherJobs = new CoreSMPPHandler[20];
			try {
				for (int i = 0; i < 10; i++) {
					coreSMPPHandlerActivation[i] = new CoreSMPPHandler(smppProperties, smppService);
					coreSMPPHandlerDeactivation[i] = new CoreSMPPHandler(smppProperties, smppService);
					coreSMPPHandlerRenewal[i] = new CoreSMPPHandler(smppProperties, smppService);
					coreSMPPHandlerOtherJobs[i] = new CoreSMPPHandler(smppProperties, smppService);
				}
				for (int i = 10; i < 20; i++) {
					coreSMPPHandlerRenewal[i] = new CoreSMPPHandler(smppProperties, smppService);
					coreSMPPHandlerOtherJobs[i] = new CoreSMPPHandler(smppProperties, smppService);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}

	@RequestMapping("/home")
	public String home() {
		return "home";
	}

	@RequestMapping(value = "/sendsms", method = RequestMethod.GET)
	public ResponseEntity<?> getResp(HttpServletRequest request, HttpServletResponse response) {
		initializeHandlers();
		String dlr = request.getParameter("dlr-url");
		String dlrUrl = SmppUtil.decodeToUtf8(dlr);
		String transactionId = SmppUtil.getTransactionIDForURL(dlrUrl);

		if (transactionId.toCharArray()[0] == 'a') {
			// Send Activation
			if (activations == 10)
				activations = 0;
			return sendSMSProcess(request, dlr, coreSMPPHandlerActivation[activations++]);
		} else if (transactionId.toCharArray()[0] == 'd') {
			// Send Deactivation
			if (deactivations == 10)
				deactivations = 0;
			return sendSMSProcess(request, dlr, coreSMPPHandlerDeactivation[deactivations++]);
		} else if (transactionId.toCharArray()[0] == 'r') {
			// Send Renewal
			if (renewals == 20)
				renewals = 0;
			return sendSMSProcess(request, dlr, coreSMPPHandlerRenewal[renewals++]);
		} else {
			// Send To Other Job
			if (others == 10)
				others = 0;
			return sendSMSProcess(request, dlr, coreSMPPHandlerOtherJobs[others++]);
		}

	}

	private ResponseEntity<?> sendSMSProcess(HttpServletRequest request, String dlr, CoreSMPPHandler coreSMPPHandler) {
		HttpStatus returnStatus = HttpStatus.GATEWAY_TIMEOUT;
		if (SMPPLOGGER.isDebugEnabled()) {
			SMPPLOGGER.debug("In SmppController: /sendsms processing Start for Request : "
					+ new StringBuilder(request.getRequestURL()).append("?").append(request.getQueryString())
							.toString());
		}
		String to = request.getParameter("to");
		String from = request.getParameter("from");
		String metaData = request.getParameter("meta-data");
		if (null != to && null != from && null != metaData && null != dlr) {
			String dlrUrl = SmppUtil.decodeToUtf8(dlr);

			if (SMPPLOGGER.isDebugEnabled()) {
				SMPPLOGGER.debug("In SmppController: decoded URL :" + dlrUrl);
			}

			HashMap<String, String> map = SmppUtil.getData(SmppUtil.decodeToUtf8(metaData));
			String message_payload = map.get("message_payload");
			String PRICEPOINT = map.get("PRICEPOINT");

			Date requestTime = new Date();
			// fetching transaction id
			String transactionId = SmppUtil.getTransactionIDForURL(dlrUrl);
			if (null == coreSMPPHandler) {
				// Initialize SMPP Handler
				try {
					coreSMPPHandler = new CoreSMPPHandler(smppProperties, smppService);
				} catch (IOException e) {
					SMPPLOGGER.debug("Error Configurations Setting. " + e.getMessage());
				}
			}
			SMPPRespTO expectedSMPPRespTO = new SMPPRespTO();
			expectedSMPPRespTO.setDlrURL(dlrUrl);
			expectedSMPPRespTO.setMsisdn(to);
			expectedSMPPRespTO.setPricePoint(PRICEPOINT);
			expectedSMPPRespTO.setTransId(transactionId);
			// Sending SMS to SMPP - Start
			SMPPReqTO smppReqTO = new SMPPReqTO();
			try {
				smppReqTO.setDestAddress(new Address((byte) 1, (byte) 0, to));
				smppReqTO.setSourceAddress(new Address((byte) 0, (byte) 0, from));
			} catch (WrongLengthOfStringException e) {
				SMPPLOGGER.debug("Exception : " + e.getMessage());
				e.printStackTrace();
			}
			smppReqTO.setMessagePayload(message_payload);
			smppReqTO.setExpetedResponseTO(expectedSMPPRespTO);

			// insert in db
			try {
				insertInDB(to, PRICEPOINT, transactionId, dlrUrl);
			} catch (SMPPException e) {
				if (SMPPLOGGER.isDebugEnabled()) {
					SMPPLOGGER.debug("SMPP Exception : " + e.getMessage());
				}
			}

			// Send SMS
			SMPPRespTO smppRespTO = null;
			try {
				smppRespTO = coreSMPPHandler.submitSMSRequest(smppReqTO);
			} catch (SMPPException e) {
				if (SMPPLOGGER.isDebugEnabled()) {
					SMPPLOGGER.debug("SMPP Exception : " + e.getMessage());
				}
			}

			// Insert Data in local DB
			try {
				returnStatus = updateDataInDB(smppRespTO, returnStatus);
			} catch (SMPPException e) {
				if (SMPPLOGGER.isDebugEnabled()) {
					SMPPLOGGER.debug("SMPP Exception : " + e.getMessage());
				}
			}

			Date responseTime = new Date();
			if (SMPPLOGGER.isDebugEnabled()) {
				if (smppRespTO != null) {
					String data = loggingBean.logData(request, returnStatus + "", smppReqTO.debugString(),
							smppRespTO.debugString(), requestTime, responseTime, to, transactionId, PRICEPOINT);
					SMPPLOGGER.debug("In SmppController: final response returned :" + data);
					SENDSMSLOGGER.info(data);
				} else {
					String data = loggingBean.logData(request, returnStatus + "", smppReqTO.debugString(),
							smppRespTO + "", requestTime, responseTime, to, transactionId, PRICEPOINT);
					SMPPLOGGER.debug("In SmppController: final response returned :" + data);
					SENDSMSLOGGER.info(data);
				}
			}
		}
		return new ResponseEntity(returnStatus);
	}

	private HttpStatus updateDataInDB(SMPPRespTO smppRespTO, HttpStatus returnStatus) throws SMPPException {
		// Set data for DB
		if (null != smppRespTO) {
			SmppData smppData = new SmppData();
			smppData.setMsisdn(smppRespTO.getMsisdn());
			smppData.setPricePoint(smppRespTO.getPricePoint());
			smppData.setTransactionId(smppRespTO.getTransId());
			smppData.setReqStatus("0");
			smppData.setDlrURL(smppRespTO.getDlrURL());
			smppData = smppService.getRecord(smppData);
			smppService.getRecord(smppData);
			if (null != smppRespTO.getRespStatus() && smppRespTO.getRespStatus() == 0) {
				returnStatus = HttpStatus.ACCEPTED;
				smppData.setMessageId(smppRespTO.getResponseMsgId());
				smppData.setReqStatus("1");
				smppData.setRespStatus(returnStatus + "");
				smppService.update(smppData);
				if (SMPPLOGGER.isDebugEnabled()) {
					SMPPLOGGER.debug("In SmppController: Data inserted into DB. " + smppData.toString());
				}
			} else {
				smppData.setRespStatus(returnStatus + "");
				smppService.delete(smppData.getId());
				if (SMPPLOGGER.isDebugEnabled()) {
					SMPPLOGGER.debug("In SmppController: Data deleted from DB. " + smppData.toString());
				}
			}
		}
		return returnStatus;
	}

	private void insertInDB(String msisdn, String pricePoint, String transId, String dlrURL) throws SMPPException {
		// Set data for DB
		SmppData smppData = new SmppData();
		smppData.setMsisdn(msisdn);
		smppData.setPricePoint(pricePoint);
		smppData.setTransactionId(transId);
		smppData.setReqStatus("0");
		smppData.setDlrURL(dlrURL);
		smppService.save(smppData);
	}

}
