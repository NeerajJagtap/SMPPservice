package com.vuclip.smpp.core.handler;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import com.vuclip.smpp.util.LoggingBean;
import com.vuclip.smpp.util.SmppUtil;

public class CoreSMPPHandler {

	Logger smpplogger = LogManager.getLogger("smpplogger");
	Logger dnlogger = LogManager.getLogger("dnlogger");

	private CoreSMPPService coreSMPPService = null;

	private SMPPRespTO submitMessagePDU = null;

	private String transactionID = null;

	private String dlrURL = null;

	private SmppService smppService;

	public CoreSMPPHandler(SMPPProperties smppProperties, String dlrURL, String transactionID, SmppService smppService) throws IOException {
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

					if (smpplogger.isDebugEnabled()) {
						smpplogger.debug("In CoreSMPPHandler:DN Listener started: ");
					}

				} catch (SMPPException e) {
					smpplogger.debug("In CoreSMPPHandler:DN Listener Exception: " + e.getMessage());
				}
				responseReceivedTime = new Date();
				if (null != dnto && Data.ESME_ROK == dnto.getDeliveryStatus()) {
					if (smpplogger.isDebugEnabled()) {
						smpplogger.debug("In CoreSMPPHandler:Response received from carrier: " + dnto.debugString());
					}
					if (null != submitMessagePDU && null != transactionID && null != dlrURL && submitMessagePDU.getRespStatus() == Data.ESME_ROK) {
						try {
							sendNotificationToTalend(dnto);
						} catch (IOException e) {
							// if (smpplogger.isDebugEnabled()) {
							smpplogger.debug("In CoreSMPPHandler:[" + dnto.getMsisdn() + "] DN Listener Error: Error while connecting to Talend.");
							// }
						} catch (Exception e) {
							// if (smpplogger.isDebugEnabled()) {
							smpplogger.debug("In CoreSMPPHandler:[" + dnto.getMsisdn() + "] DN Listener Error: Error while connecting to DB.");
							// }
						}
					}
				} else {
					// if (notice.isDebugEnabled()) {
					smpplogger.debug("In CoreSMPPHandler:[" + dnto.getMsisdn() + "] DN Listener NULL: "
							+ (null == dnto ? "Delivery Notification TO is null" : "Response Status is not OK."));
					// }
				}
			}

			private void sendNotificationToTalend(DeliveryNotificationTO dnto) throws Exception {
				if (smpplogger.isDebugEnabled()) {
					smpplogger.debug("In CoreSMPPHandler : SendNotificationToTalend start: " + dnto.debugString());
				}
				talendRequestTime = new Date();
				// Split URL to encode separately
				String splitURL[] = dlrURL.split("smscid");

				// Call REST service of talend
				String urlString = splitURL[0].replace("%p", dnto.getMsisdn()).replace("%a", SmppUtil.encodeToUtf8(dnto.getResponseDNString()))
						+ SmppUtil.encodeToUtf8("smscid" + splitURL[1]);
				URL url = new URL(urlString);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");
				conn.setRequestProperty("Accept", "application/json");
				int responseCode = conn.getResponseCode();
				talendResponseTime = new Date();
				if (responseCode == 200) {
					// if (notice.isDebugEnabled()) {
					String data = LoggingBean.logData(dnto, urlString, Integer.valueOf(responseCode).toString(), listenerStartTime, responseReceivedTime, talendRequestTime,
							talendResponseTime);
					smpplogger.debug(data);
					dnlogger.info(data);
					// }
				} else {
					// if (notice.isDebugEnabled()) {
					String data = LoggingBean.logData(dnto, urlString, Integer.valueOf(responseCode).toString(), listenerStartTime, responseReceivedTime, talendRequestTime,
							talendResponseTime);
					smpplogger.debug(data);
					dnlogger.info(data);
					// }
				}
				// Update data in database
				updateDataToDB(dnto, responseCode, urlString);
			}

			
			private void updateDataToDB(DeliveryNotificationTO dnto, int responseCode, String urlString)
					throws SMPPException {
				SmppData smppData = new SmppData();
				smppData.setMessageId(dnto.getMessageId());
				smppData.setMsisdn(dnto.getMsisdn());

				smppData = smppService.getRecord(smppData);
				smppData.setDlrURL(urlString);
				smppData.setDnMessage(dnto.getResponseDNString());
				smppData.setTalendResponse(Integer.valueOf(responseCode).toString());
				if (smpplogger.isDebugEnabled()) {
					smpplogger.debug("In CoreSMPPHandler : updateDataToDB after DN Receive: " + smppData.toString());
				}
				
				smppService.update(smppData);
			
			}
		}).start();
	}

}
