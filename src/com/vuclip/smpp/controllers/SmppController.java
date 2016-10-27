package com.vuclip.smpp.controllers;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * @author Vuclip
 *
 */

@Controller
public class SmppController {

	private static final Logger logger = LoggerFactory.getLogger(SmppController.class);

	@Autowired
	private LoggingBean loggingBean;

	@Autowired
	private SMPPProperties smppProperties;

	@Autowired
	private SmppConfig smppConfig;

	@Autowired
	private SmppService smppService;

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
		String to = request.getParameter("to");
		String from = request.getParameter("from");
		String meta_data = request.getParameter("meta-data");
		String dlr_url = SmppUtil.decodeToUtf8(request.getParameter("dlr-url"));

		HashMap<String, String> map = SmppUtil.getData(SmppUtil.decodeToUtf8(meta_data));
		String message_payload = map.get("message_payload");
		String PRICEPOINT = map.get("PRICEPOINT");

		Date requestTime = new Date();

		// fetching transaction id
		String transactionId = SmppUtil.getTransactionIDForURL(dlr_url);
		CoreSMPPHandler coreSMPPHandler = null;
		// Initialize SMPP Handler
		try {
			coreSMPPHandler = new CoreSMPPHandler(smppProperties, dlr_url, transactionId, smppService);
		} catch (IOException e) {
			if (logger.isDebugEnabled()) {
				logger.debug("Error Configurations Setting. " + e.getMessage());
			}
		}

		// Sending SMS to SMPP - Start
		SMPPReqTO smppReqTO = new SMPPReqTO();
		try {
			smppReqTO.setDestAddress(new Address((byte) 1, (byte) 0, to));
			smppReqTO.setSourceAddress(new Address((byte) 0, (byte) 0, from));
		} catch (WrongLengthOfStringException e) {
			if (logger.isDebugEnabled()) {
				logger.debug("Exception : " + e.getMessage());
			}
			e.printStackTrace();
		}
		smppReqTO.setMessagePayload(message_payload);
		// Send SMS
		SMPPRespTO smppRespTO = sendSyncSMS(smppReqTO, coreSMPPHandler);

		HttpStatus returnStatus = HttpStatus.GATEWAY_TIMEOUT;
		// Insert Data in local DB
		try {
			returnStatus = insertDataInDB(to, PRICEPOINT, transactionId, smppRespTO, returnStatus);
		} catch (SMPPException e) {
			if (logger.isDebugEnabled()) {
				logger.debug("Exception : " + e.getMessage());
			}
			e.printStackTrace();
		}

		Date responseTime = new Date();
		if (logger.isDebugEnabled()) {
			if (smppRespTO != null) {
				logger.info(loggingBean.logData(request, returnStatus + "", smppReqTO.debugString(),
						smppRespTO.debugString(), requestTime, responseTime, to, transactionId, PRICEPOINT));
			} else {
				logger.info(loggingBean.logData(request, returnStatus + "", smppReqTO.debugString(), smppRespTO + "",
						requestTime, responseTime, to, transactionId, PRICEPOINT));
			}
		}

		return new ResponseEntity(returnStatus);
	}

	private HttpStatus insertDataInDB(String to, String PRICEPOINT, String transactionId, SMPPRespTO smppRespTO,
			HttpStatus returnStatus) throws SMPPException {
		// Set data for DB
		SmppData smppData = new SmppData();
		smppData.setMsisdn(to);
		smppData.setPricePoint(PRICEPOINT);
		smppData.setTransactionId(transactionId);
		smppData.setReqStatus("0");
		if (null != smppRespTO && smppRespTO.getRespStatus() == 0) {
			returnStatus = HttpStatus.ACCEPTED;
			smppData.setMessageId(smppRespTO.getResponseMsgId());
			smppData.setReqStatus("1");
			smppData.setRespStatus(returnStatus + "");
			smppService.save(smppData);
		} else {
			smppData.setReqStatus("1");
			smppData.setRespStatus(returnStatus + "");
			smppService.save(smppData);
		}
		return returnStatus;
	}

	private SMPPRespTO sendSyncSMS(SMPPReqTO smppReqTO, CoreSMPPHandler coreSMPPHandler) {
		SMPPRespTO responseTO = null;
		try {
			responseTO = coreSMPPHandler.submitSMSRequest(smppReqTO);
		} catch (SMPPException e) {
			if (logger.isDebugEnabled()) {
				logger.debug("Submit operation failed. " + e.getMessage());
			}
		}
		return responseTO;
	}

}
