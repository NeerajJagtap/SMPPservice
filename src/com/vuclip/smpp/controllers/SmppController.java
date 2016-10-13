package com.vuclip.smpp.controllers;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.logica.smpp.pdu.Address;
import com.logica.smpp.pdu.WrongLengthOfStringException;
import com.vuclip.smpp.client.CoreSMPPClient;
import com.vuclip.smpp.to.PDUTO;
import com.vuclip.smpp.to.SMPPReqTO;
import com.vuclip.util.SmppUtil;

/**
 * @author Vuclip
 *
 */

@Controller
public class SmppController {
	public static Map<String, String> transIdToUrlMap = null;

	private static final Logger logger = LoggerFactory.getLogger(SmppController.class);

	// @Value("${ip}")
	private String ip = "127.0.0.1";

	// @Value("${port}")
	private String port = "8084";

	private CoreSMPPClient coreSMPPClient = null;

	/*
	 * @Autowired HashDAO hashDAO;
	 * 
	 * @Autowired AocMockService aocMockService;
	 * 
	 * @Autowired private HashDataFormValidator validator;
	 */

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
	public ResponseEntity getResp(HttpServletRequest request, HttpServletResponse response) {
		// HashData hashData = hashDAO.getByCustomerNo(customerNo);
		// Initialize SMPP Client
		if (null == coreSMPPClient) {
			coreSMPPClient = new CoreSMPPClient();
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

		System.out.println("******************* Welcome SMPP Billing  ********************");
		System.out.println(" Talend Input  : " + meta_data);
		System.out.println("Talend Input username : " + username);
		System.out.println("Talend Input password : " + password);
		System.out.println("Talend Input to : " + to);
		System.out.println("Talend Input from : " + from);
		System.out.println("Talend Input dlr_mask : " + dlr_mask);
		System.out.println("Talend Input text : " + text);
		System.out.println("Talend Input meta_data : " + meta_data);
		System.out.println("Talend Input dlr_url : " + dlr_url);
		System.out.println("Talend Input message_payload : " + message_payload);
		System.out.println("Talend Input PARTNER_ROLE_ID : " + PARTNER_ROLE_ID);
		System.out.println("Talend Input PRODUCT : " + PRODUCT);
		System.out.println("Talend Input PRICEPOINT : " + PRICEPOINT);

		if (logger.isDebugEnabled()) {
			logger.debug("******************* Welcome SMPP Billing  ********************");
			logger.debug("Talend Input username : " + username);
			logger.debug("Talend Input password : " + password);
			logger.debug("Talend Input to : " + to);
			logger.debug("Talend Input from : " + from);
			logger.debug("Talend Input dlr_mask : " + dlr_mask);
			logger.debug("Talend Input text : " + text);
			logger.debug("Talend Input meta_data : " + meta_data);
			logger.debug("Talend Input dlr_url : " + dlr_url);
			logger.debug("Talend Input message_payload : " + message_payload);
			logger.debug("Talend Input PARTNER_ROLE_ID : " + PARTNER_ROLE_ID);
			logger.debug("Talend Input PRODUCT : " + PRODUCT);
			logger.debug("Talend Input PRICEPOINT : " + PRICEPOINT);

		}
		//Start
		String transactionId = getTransactionIDForURL(dlr_url);
		//Set inside DB

		transIdToUrlMap.put(transactionId, dlr_url);

		// Sending SMS to SMPP - Start
		PDUTO pduto = new PDUTO();
		try {
			pduto.setDestAddress(new Address((byte) 1, (byte) 1, to));
			pduto.setSourceAddress(new Address((byte) 0, (byte) 1, from));
		} catch (WrongLengthOfStringException e) {
			if (logger.isDebugEnabled()) {
				logger.debug("redirectionURL : " + e.getMessage());
			}
		}
		pduto.setShortMessage(text);

		SMPPReqTO smppReqTO = new SMPPReqTO();
		smppReqTO.setDlrURL(dlr_url);
		smppReqTO.setTransId(transactionId);
		smppReqTO.setPduto(pduto);

		try {
			coreSMPPClient.runSMSJob();
			coreSMPPClient.submitSMSRequest(smppReqTO);
			coreSMPPClient.runResponseHandlerJob();
		} catch (InterruptedException e) {
			if (logger.isDebugEnabled()) {
				logger.debug("redirectionURL Error : " + e.getMessage());
			}
		}

		// Sending SMS to SMPP - End

		/*
		String decoded_meta_data = SmppUtil.decodeToUtf8(meta_data);
		String finalRespString = "transid=a189296112773922T28092016163107E40&msisdn=%p&msg=%a&smscid=%i&smsid=%I&dlrsmsc=%A&dlr=%d&time=%t&state=%d";

		System.out.println("Encoded Output : " + decoded_meta_data);
		if (logger.isDebugEnabled()) {
			logger.debug("Encoded Output : " + finalRespString);
		}

		String redirectionURLString = "http://" + ip + ":" + port + "/oovs-timwe/notification?transid="
				+ finalRespString;
		URL redirectionURL = null;
		try {
			redirectionURL = new URL(redirectionURLString);
		} catch (MalformedURLException e) {
			if (logger.isErrorEnabled()) {
				logger.error("redirectionURL : " + redirectionURLString);
			}
		}

		if (null != redirectionURL) {
			System.out.println(redirectionURL);
		}

		System.out.println("redirectionURL : " + redirectionURLString);
		if (logger.isDebugEnabled()) {
			logger.debug("redirectionURL : " + redirectionURLString);
		}
*/
		/*
		 * ModelAndView mav = new ModelAndView("redirect:" + redirectionURL);
		 * return mav;
		 */

		return new ResponseEntity(HttpStatus.OK);
		// return finalRespString;
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
	 * "4u%2Bz3oOVstniZS7VucmMc9ky0r2iBglcQkxAHudhj5p9jSXjH09mXTnPMLGKDINWYMzLD5r5F95b3VZ5CpKg1RoXI8cEzqDSiXuoqajviQU%3D";
	 * // String endocedAOCString = //
	 * "MoO6yxJ2ru4mbjvWVm5IOXxNBbFsMhb2uTCUcTe0N5XYvcxXVi5SUaVzCdZCnG7GDQ81JfZBonN%2B0z2lML6HEIUY6pIVOJxBoPrSfrcfCzQ%3D";
	 * String finalRespString = aocMockService.aocFlowMock(endocedAOCString);
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
