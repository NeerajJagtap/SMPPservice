package com.vuclip.smpp.controllers;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smpp.TimeoutException;
import org.smpp.WrongSessionStateException;
import org.smpp.pdu.Address;
import org.smpp.pdu.PDUException;
import org.smpp.pdu.ValueNotSetException;
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
import com.vuclip.smpp.core.to.PDUTO;
import com.vuclip.smpp.core.to.SMPPReqTO;
import com.vuclip.smpp.core.to.SMPPRespTO;
import com.vuclip.smpp.orm.dto.SmppData;
import com.vuclip.smpp.props.SMPPProperties;
import com.vuclip.smpp.service.SmppService;
import com.vuclip.util.LoggingBean;
import com.vuclip.util.SmppUtil;

/**
 * @author Vuclip
 *
 */

@Controller
public class SmppController {
	public static Map<String, String> transIdToUrlMap = null;

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
		CoreSMPPHandler coreSMPPHandler = null;
		// Initialize SMPP Handler
		try {
			coreSMPPHandler = new CoreSMPPHandler(smppProperties);
		} catch (IOException e) {
			if (logger.isErrorEnabled()) {
				logger.error("Error Configurations Setting. " + e.getMessage());
			}
			e.printStackTrace();
		}
		if (null == transIdToUrlMap) {
			transIdToUrlMap = new HashMap<String, String>();
		}

		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String to = request.getParameter("to");
		String from = request.getParameter("from");
		String dlr_mask = request.getParameter("dlr-mask");
		String text = request.getParameter("text");
		String meta_data = request.getParameter("meta-data");
		String dlr_url = request.getParameter("dlr-url");

		HashMap<String, String> map = SmppUtil.getData(meta_data);

		String message_payload = map.get("message_payload");
		String PARTNER_ROLE_ID = map.get("PARTNER_ROLE_ID");
		String PRODUCT = map.get("PRODUCT");
		String PRICEPOINT = map.get("PRICEPOINT");

		Date requestTime = new Date();

		System.out.println("******************* Welcome SMPP Billing  ********************");
		System.out.println(" Talend Input  : " + meta_data);
		System.out.println("Talend Input username : " + username);
		System.out.println("Talend Input password : " + password);
		System.out.println("Talend Input (msisdn) to : " + to);
		System.out.println("Talend Input (shortCode) from : " + from);
		System.out.println("Talend Input dlr_mask : " + dlr_mask);
		System.out.println("Talend Input text : " + text);
		System.out.println("Talend Input meta_data : " + meta_data);
		System.out.println("Talend Input dlr_url : " + dlr_url);
		System.out.println("Talend Input message_payload : " + message_payload);
		System.out.println("Talend Input PARTNER_ROLE_ID : " + PARTNER_ROLE_ID);
		System.out.println("Talend Input PRODUCT : " + PRODUCT);
		System.out.println("Talend Input PRICEPOINT : " + PRICEPOINT);

		// fetching transaction id
		String transactionId = SmppUtil.getTransactionIDForURL(dlr_url);

		// Set inside DB
		transIdToUrlMap.put(transactionId, dlr_url);
		SmppData smppData = new SmppData();
		smppData.setMsisdn(to);
		smppData.setPricePoint(PRICEPOINT);
		smppData.setTransactionId(transactionId);
		smppService.save(smppData);

		// Sending SMS to SMPP - Start
		PDUTO pduto = new PDUTO();
		try {
			pduto.setDestAddress(new Address((byte) 1, (byte) 0, to));
			pduto.setSourceAddress(new Address((byte) 0, (byte) 0, from));
		} catch (WrongLengthOfStringException e) {
			if (logger.isDebugEnabled()) {
				logger.debug("Exception : " + e.getMessage());
			}
			e.printStackTrace();
		}
		pduto.setMessagePayload(message_payload);

		SMPPReqTO smppReqTO = new SMPPReqTO();
		smppReqTO.setDlrURL(dlr_url);
		smppReqTO.setTransId(transactionId);
		smppReqTO.setPduto(pduto);
		// sendAsyncSMS(smppReqTO);
		HttpStatus returnStatus = sendSyncSMS(smppReqTO, coreSMPPHandler);
		Date responseTime = new Date();
		if (logger.isDebugEnabled()) {
			logger.debug(loggingBean.logData(request, returnStatus + "", smppReqTO.debugString(), "rawResponse",
					requestTime, responseTime, to, transactionId, PRICEPOINT));
		}
		return new ResponseEntity(returnStatus);

	}

	private HttpStatus sendSyncSMS(SMPPReqTO smppReqTO, CoreSMPPHandler coreSMPPHandler) {
		SMPPRespTO responseTO = null;
		try {
			responseTO = coreSMPPHandler.submitSMSRequest(smppReqTO);
			coreSMPPHandler.runReceiverListener();
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

		if (null != responseTO && responseTO.getRespStatus() == 0) {
			return HttpStatus.ACCEPTED;
		}
		return HttpStatus.BAD_REQUEST;
	}

	private String getTransactionIDForURL(String dlr_url) {
		String transactionId = null;

		String[] splitURL = dlr_url.split("&");
		for (int index = 0; index < splitURL.length; index++) {
			if (splitURL[index].contains("transid")) {
				transactionId = splitURL[index].split("=")[1];
			}
		}
		return transactionId;
	}

	// Receive DN notification method
	@RequestMapping(value = "/oovs-timwe/notification", method = RequestMethod.GET)
	public String searchCustomer(HttpServletRequest request, HttpServletResponse response) {

		System.out.println(" Input data : " + request.getParameter("transid"));

		return "200 OK";
	}

}
