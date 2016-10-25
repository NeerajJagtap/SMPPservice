package com.vuclip.smpp.core.handler;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smpp.Data;

import com.vuclip.smpp.core.service.CoreSMPPService;
import com.vuclip.smpp.core.service.impl.CoreSMPPServiceImpl;
import com.vuclip.smpp.core.to.DeliveryNotificationTO;
import com.vuclip.smpp.core.to.SMPPReqTO;
import com.vuclip.smpp.core.to.SMPPRespTO;
import com.vuclip.smpp.exceptions.SMPPException;
import com.vuclip.smpp.orm.dto.SmppData;
import com.vuclip.smpp.props.SMPPProperties;
import com.vuclip.smpp.service.SmppService;
import com.vuclip.util.LoggingBean;
import com.vuclip.util.SmppUtil;

public class CoreSMPPHandler {

	private static final Logger logger = LoggerFactory.getLogger(CoreSMPPHandler.class);

	private CoreSMPPService coreSMPPService = null;

	private SMPPRespTO submitMessagePDU = null;

	private String transactionID = null;

	private String dlrURL = null;

	private SmppService smppService;

	public CoreSMPPHandler(SMPPProperties smppProperties, String dlrURL, String transactionID, SmppService smppService)
			throws IOException {
		coreSMPPService = new CoreSMPPServiceImpl(smppProperties);
		this.smppService = smppService;
		this.transactionID = transactionID;
		this.dlrURL = dlrURL;
	}

	public SMPPRespTO submitSMSRequest(SMPPReqTO smppReqTO) throws SMPPException {
		coreSMPPService.setSmppReqTO(smppReqTO);
		submitMessagePDU = coreSMPPService.submitMessagePDU();
		runReceiverListener();
		return submitMessagePDU;
	}

	public void runReceiverListener() {
		new Thread(new Runnable() {

			private Date listenerStartTime;

			private Date responseReceivedTime;

			private Date talendRequestTime;

			private Date talendResponseTime;

			@Override
			public void run() {
				DeliveryNotificationTO dnto = null;
				// Start Time
				listenerStartTime = new Date();
				try {
					dnto = coreSMPPService.receiveListener();
				} catch (SMPPException e) {
					if (logger.isDebugEnabled()) {
						logger.debug(" DN Listener Exception in : " + e.getMessage());
					}
				}
				responseReceivedTime = new Date();
				if (null != dnto && Data.ESME_ROK == dnto.getDeliveryStatus()) {
					System.out.println("Response : " + dnto.debugString());
					if (null != submitMessagePDU && null != transactionID && null != dlrURL
							&& submitMessagePDU.getRespStatus() == Data.ESME_ROK) {
						try {
							sendNotificationToTalend(dnto);
						} catch (IOException e) {
							if (logger.isDebugEnabled()) {
								logger.debug("[" + dnto.getMsisdn()
										+ "] DN Listener Error: Error while connecting to Talend.");
							}
						}
					}
				} else {
					if (logger.isDebugEnabled()) {
						logger.debug("[" + dnto.getMsisdn() + "] DN Listener NULL: "
								+ (null == dnto ? "Delivery Notification TO is null" : "Response Status is not OK."));
					}
				}
			}

			private void sendNotificationToTalend(DeliveryNotificationTO dnto) throws IOException {
				talendRequestTime = new Date();
				// Split URL to encode separately
				String splitURL[] = dlrURL.split("smscid");

				// Call REST service of talend
				String urlString = splitURL[0].replace("%p", dnto.getMsisdn()).replace("%a",
						SmppUtil.encodeToUtf8(dnto.getResponseDNString()))
						+ SmppUtil.encodeToUtf8("smscid" + splitURL[1]);
				URL url = new URL(urlString);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");
				conn.setRequestProperty("Accept", "application/json");
				int responseCode = conn.getResponseCode();
				talendResponseTime = new Date();
				if (responseCode == 200) {
					if (logger.isDebugEnabled()) {
						logger.debug(LoggingBean.logData(dnto, urlString, Integer.valueOf(responseCode).toString(),
								listenerStartTime, responseReceivedTime, talendRequestTime, talendResponseTime));
					}
				} else {
					if (logger.isDebugEnabled()) {
						logger.debug(LoggingBean.logData(dnto, urlString, Integer.valueOf(responseCode).toString(),
								listenerStartTime, responseReceivedTime, talendRequestTime, talendResponseTime));
					}
				}
				// Update data in database
				updateDataToDB(dnto, responseCode, urlString);
			}

			private void updateDataToDB(DeliveryNotificationTO dnto, int responseCode, String urlString) {
				SmppData smppData = smppService.getByMsisdn(dnto.getMsisdn());
				smppData.setDlrURL(urlString);
				smppData.setDnMessage(dnto.getResponseDNString());
				smppData.setTalendResponse(Integer.valueOf(responseCode).toString());
				smppService.update(smppData);
			}
		}).start();
	}

}
