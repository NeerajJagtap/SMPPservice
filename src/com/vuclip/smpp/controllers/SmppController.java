package com.vuclip.smpp.controllers;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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

import com.vuclip.config.SmppConfig;
import com.vuclip.smpp.client.CoreSMPPClient;
import com.vuclip.smpp.client.CoreSMPPHandler;
import com.vuclip.smpp.props.SMPPProperties;
import com.vuclip.smpp.service.SmppService;
import com.vuclip.smpp.to.PDUTO;
import com.vuclip.smpp.to.SMPPReqTO;
import com.vuclip.smpp.to.SMPPRespTO;
import com.vuclip.smpp.to.SmppData;
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

	private CoreSMPPClient coreSMPPClient = null;

	@Autowired
	private SMPPProperties config;

	private CoreSMPPHandler coreSMPPHandler = null;

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

		// Initialize SMPP Client
		if (null == coreSMPPClient) {
			try {
				coreSMPPClient = new CoreSMPPClient(config);
			} catch (IOException e) {
				if (logger.isErrorEnabled()) {
					logger.error("Error Sonfigurations Setting. " + e.getMessage());
				}
			}
		}

		if (null == coreSMPPHandler) {
			try {
				coreSMPPHandler = new CoreSMPPHandler(config);
			} catch (IOException e) {
				if (logger.isErrorEnabled()) {
					logger.error("Error Sonfigurations Setting. " + e.getMessage());				
				}
				e.printStackTrace();
			}
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
		Date responseTime = new Date();

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
		pduto.setShortMessage("");
		List<String> list = getPartedMessages(message_payload, 115);
		pduto.setSplitMessges(list);

		pduto.setMessagePayload(message_payload);

		SMPPReqTO smppReqTO = new SMPPReqTO();
		smppReqTO.setDlrURL(dlr_url);
		smppReqTO.setTransId(transactionId);
		smppReqTO.setPduto(pduto);
//		sendAsyncSMS(smppReqTO);

		HttpStatus returnStatus = sendSyncSMS(smppReqTO);

/*		if (logger.isDebugEnabled()) {
			logger.debug(loggingBean.logData(request, "talendResponse", "rawRequest", "rawResponse", requestTime,
					responseTime, to, transactionId, PRICEPOINT));
		}
*/		if (logger.isDebugEnabled()) {
			logger.debug(loggingBean.logData(request, HttpStatus.OK+"", "rawRequest", "rawResponse", requestTime, responseTime, to, transactionId, PRICEPOINT));
		}
		return new ResponseEntity(returnStatus);
		
	}

	private HttpStatus sendSyncSMS(SMPPReqTO smppReqTO) {
		coreSMPPHandler.runReceiverListener();
		SMPPRespTO responseTO = null;
		try {
			coreSMPPHandler.runReceiverListener();
			responseTO = coreSMPPHandler.submitSMSRequest(smppReqTO);
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

	private void sendAsyncSMS(SMPPReqTO smppReqTO) {
		try {
			coreSMPPClient.runSMSJob();
			coreSMPPClient.runResponseHandlerJob();
			coreSMPPClient.runReceiverListener();

			coreSMPPClient.submitSMSRequest(smppReqTO);
		} catch (InterruptedException e) {
			if (logger.isDebugEnabled()) {
				logger.debug("Execption : " + e.getMessage());
			}
			e.printStackTrace();
		}
	}

	private List<String> getPartedMessages(String original, int lenghtOfPart) {
		List<String> list = new ArrayList<String>();
		StringBuilder stringBuilder = new StringBuilder(original);
		while (stringBuilder.length() > 0) {
			if (stringBuilder.length() > lenghtOfPart) {
				list.add(stringBuilder.substring(0, lenghtOfPart));
				stringBuilder.replace(0, lenghtOfPart, "");
			} else {
				list.add(stringBuilder.toString());
				stringBuilder.replace(0, stringBuilder.length(), "");
			}
		}
		return list;
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
	

	/*
	 * // @RequestMapping("/searchCustomer") public ModelAndView
	 * searchCustomer(@RequestParam(required = false, defaultValue = "") String
	 * customerNo) { ModelAndView mav = new ModelAndView("showCustomer");
	 * List<HashData> hashData = hashDAO.searchCustomer(customerNo.trim());
	 * mav.addObject("SEARCH_HASHDATA_RESULTS_KEY", hashData); return mav; }
	 */

	/*
	 * 
	 * // @RequestMapping("/viewAllCustomer") public ModelAndView
	 * getAllCustomer() { ModelAndView mav = new ModelAndView("showCustomer");
	 * List<HashData> hashData = hashDAO.getAllHashData();
	 * mav.addObject("SEARCH_HASHDATA_RESULTS_KEY", hashData); return mav; }
	 * 
	 * // @RequestMapping(value="/saveCustomer", method=RequestMethod.GET)
	 * public ModelAndView newuserForm() { ModelAndView mav = new
	 * ModelAndView("newCustomer"); HashData hashData = new HashData();
	 * mav.getModelMap().put("newCustomer", hashData); return mav; }
	 * 
	 * // @RequestMapping(value="/saveCustomer", method=RequestMethod.POST)
	 * public String create(@ModelAttribute("newCustomer") HashData hashData,
	 * BindingResult result, SessionStatus status) {
	 * validator.validate(hashData, result); if (result.hasErrors()) { return
	 * "newCustomer"; } hashDAO.save(hashData); status.setComplete(); return
	 * "redirect:viewAllCustomer.htm"; }
	 * 
	 * // @RequestMapping(value="/updateCustomer", method=RequestMethod.GET)
	 * public ModelAndView edit(@RequestParam("id") Integer id) { ModelAndView
	 * mav = new ModelAndView("editCustomer"); HashData hashData =
	 * hashDAO.getById(id); mav.addObject("editCustomer", hashData); return mav;
	 * }
	 * 
	 * 
	 * 
	 * @RequestMapping(value = "/getCustomer", method = RequestMethod.GET)
	 * public ModelAndView getCustomer(String customerNo) { ModelAndView mav =
	 * new ModelAndView("editCustomer"); // HashData hashData =
	 * hashDAO.getByCustomerNo(customerNo);
	 * 
	 * // AOC flow starts here String endocedAOCString =
	 * "4u%2Bz3oOVstniZS7VucmMc9ky0r2iBglcQkxAHudhj5p9jSXjH09mXTnPMLGKDINWYMzLD5r5F95b3VZ5CpKg1RoXI8cEzqDSiXuoqajviQU%3D"
	 * ; // String endocedAOCString = //
	 * "MoO6yxJ2ru4mbjvWVm5IOXxNBbFsMhb2uTCUcTe0N5XYvcxXVi5SUaVzCdZCnG7GDQ81JfZBonN%2B0z2lML6HEIUY6pIVOJxBoPrSfrcfCzQ%3D"
	 * ; String finalRespString = aocMockService.aocFlowMock(endocedAOCString);
	 * System.out.println("Encoded Output from AOC : " + finalRespString);
	 * logger.debug("Encoded Output from AOC : " + finalRespString);
	 * 
	 * mav.addObject("editCustomer", finalRespString); return mav; }
	 * 
	 * // @RequestMapping(value="/updateCustomer", method=RequestMethod.POST)
	 * public String update(@ModelAttribute("editCustomer") HashData hashData,
	 * BindingResult result, SessionStatus status) {
	 * validator.validate(hashData, result); if (result.hasErrors()) { return
	 * "editCustomer"; } hashDAO.update(hashData); status.setComplete(); return
	 * "redirect:viewAllCustomer.htm"; }
	 * 
	 * // @RequestMapping("deleteCustomer") public ModelAndView
	 * delete(@RequestParam("id") Integer id) { ModelAndView mav = new
	 * ModelAndView("redirect:viewAllCustomer.htm"); hashDAO.delete(id); return
	 * mav; }
	 */
}
