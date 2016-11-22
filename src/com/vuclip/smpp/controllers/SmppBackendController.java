package com.vuclip.smpp.controllers;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.vuclip.smpp.exceptions.SMPPException;
import com.vuclip.smpp.orm.dto.SmppData;
import com.vuclip.smpp.service.SmppBackendService;
import com.vuclip.smpp.service.SmppService;
import com.vuclip.smpp.util.RetryLoggingBean;
import com.vuclip.smpp.util.SmppUtil;

@Controller
public class SmppBackendController {

	private static final Logger SMPPDBCLEANERLOGGER = LogManager.getLogger("smppDBCleanerLogger");

	private static final Logger SMPPTALENDRETRAIL = LogManager.getLogger("smppTalendRetrail");

	private static final Logger SMPPTALENDRETRAILREC = LogManager.getLogger("smppTalendRetrailRec");

	@Autowired
	private SmppBackendService smppBackendService;

	@Autowired
	private SmppService smppService;

	@RequestMapping(value = "/purgeSmppDB", method = RequestMethod.GET)
	public ResponseEntity<?> purgeSmppDB(HttpServletRequest request, HttpServletResponse response) {
		HttpStatus returnStatus = HttpStatus.GATEWAY_TIMEOUT;
		if (SMPPDBCLEANERLOGGER.isDebugEnabled()) {
			SMPPDBCLEANERLOGGER.debug("In SmppDBCleanController: /purgeSmppDB processing Start for Request : "
					+ new StringBuilder(request.getRequestURL()).append("?").append(request.getQueryString())
							.toString());
		}
		// Purge the DB
		try {

			smppBackendService.purgeSmppDB();
			returnStatus = HttpStatus.ACCEPTED;
		} catch (SMPPException e) {
			if (SMPPDBCLEANERLOGGER.isDebugEnabled()) {
				SMPPDBCLEANERLOGGER.debug("SMPP Exception : " + e.getMessage());
			}
		}

		return new ResponseEntity(returnStatus);
	}

	// Retry the notification received from Carrier to Talend
	@RequestMapping(value = "/retryToTalend", method = RequestMethod.GET)
	public ResponseEntity<?> retryToTalend(HttpServletRequest request, HttpServletResponse response) {
		HttpStatus returnStatus = HttpStatus.GATEWAY_TIMEOUT;
		if (SMPPTALENDRETRAIL.isDebugEnabled()) {
			SMPPTALENDRETRAIL.debug("In SmppRetryToTalendController: /retryToTalend processing Start for Request : "
					+ new StringBuilder(request.getRequestURL()).append("?").append(request.getQueryString())
							.toString());
		}

		// retry To Talend
		try {
			List<SmppData> retryToTalendList = smppBackendService.getRetryToTalendList();
			if (retryToTalendList != null && retryToTalendList.size() > 0) {

				if (SMPPTALENDRETRAIL.isInfoEnabled()) {
					SMPPTALENDRETRAIL.info(" RetryToTalend List Size : " + retryToTalendList.size());
				}

				for (SmppData smppData : retryToTalendList) {
					// Split URL to encode separately
					String splitURL[] = smppData.getDlrURL().split("smscid");

					// Call REST service of talend
					String urlString = splitURL[0].replace("%p", smppData.getMsisdn()).replace("%a",
							SmppUtil.encodeToUtf8(smppData.getDnMessage()))
							+ SmppUtil.encodeToUtf8("smscid" + splitURL[1]);
					URL url = new URL(urlString);
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setRequestMethod("GET");
					conn.setRequestProperty("Accept", "application/json");
					int responseCode = conn.getResponseCode();

					// Update number of retries
					smppData.setTalendResponse(Integer.valueOf(responseCode).toString());
					smppData.setReties(smppData.getReties() + 1);
					smppService.update(smppData);
					if (SMPPTALENDRETRAILREC.isInfoEnabled()) {
						SMPPTALENDRETRAILREC.info(new RetryLoggingBean(new Date(), smppData).getLogFormat());
					}
				}
			}

			returnStatus = HttpStatus.ACCEPTED;
		} catch (IOException e) {
			String message = e.getMessage();
			if (SMPPTALENDRETRAIL.isDebugEnabled()) {
				SMPPTALENDRETRAIL.debug("SMPP IOException : " + message);
			}
			if (SMPPTALENDRETRAILREC.isInfoEnabled()) {
				SMPPTALENDRETRAILREC.info("SMPP IOException : " + message);
			}
		} catch (SMPPException e) {
			String message = e.getMessage();
			if (SMPPTALENDRETRAIL.isDebugEnabled()) {
				SMPPTALENDRETRAIL.debug("RetryToTalendEnd with Exception : " + message);
			}
			if (SMPPTALENDRETRAILREC.isInfoEnabled()) {
				SMPPTALENDRETRAILREC.info("Exception while reading " + message);
			}
		}

		return new ResponseEntity(returnStatus);
	}

}
