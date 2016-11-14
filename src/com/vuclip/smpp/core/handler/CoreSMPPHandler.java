package com.vuclip.smpp.core.handler;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
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

	private static final Logger SMPPLOGGER = LogManager.getLogger("smpplogger");

	private static final Logger DNLOGGER = LogManager.getLogger("dnlogger");

	private static final Logger MOLOGGER = LogManager.getLogger("dnlogger");

	private CoreSMPPService coreSMPPService = null;

	private SMPPRespTO submitMessagePDU = null;

	private SmppService smppService = null;

	private boolean isReceiverActive = false;

	private String moTalendURL = null;

	public CoreSMPPHandler(SMPPProperties smppProperties, SmppService smppService) throws IOException {
		this.moTalendURL = smppProperties.getTalendMOURL();
		coreSMPPService = new CoreSMPPServiceImpl(smppProperties);
		this.smppService = smppService;
	}

	public SMPPRespTO submitSMSRequest(SMPPReqTO smppReqTO) throws SMPPException {
		submitMessagePDU = coreSMPPService.submitMessagePDU(smppReqTO);
		if (!isReceiverActive) {
			runReceiverListener();
		}
		return submitMessagePDU;
	}

	private void runReceiverListener() {
		new Thread(new Runnable() {

			private Date listenerStartTime;

			private Date responseReceivedTime;

			private Date talendRequestTime;

			private Date talendResponseTime;

			@Override
			public void run() {
				while (true) {
					isReceiverActive = true;
					DeliveryNotificationTO dnto = listener();
					responseReceivedTime = new Date();
					if (null != dnto && Data.ESME_ROK == dnto.getDeliveryStatus()) {
						if (SMPPLOGGER.isDebugEnabled()) {
							SMPPLOGGER
									.debug("In CoreSMPPHandler:Response received from carrier: " + dnto.debugString());
						}
						try {
							sendNotificationToTalend(dnto);
						} catch (IOException e) {
							e.printStackTrace();
							if (SMPPLOGGER.isDebugEnabled()) {
								SMPPLOGGER.debug("In CoreSMPPHandler:[" + dnto.getMsisdn()
										+ "] DN Listener Error: Error while connecting to Talend.");
							}
						} catch (Exception e) {
							e.printStackTrace();
							if (SMPPLOGGER.isDebugEnabled()) {
								SMPPLOGGER.debug("In CoreSMPPHandler:[" + dnto.getMsisdn()
										+ "] DN Listener Error: Error while connecting to DB.");
							}
						}
					} else {
						if (SMPPLOGGER.isDebugEnabled()) {
							SMPPLOGGER.debug("In CoreSMPPHandler:[" + (null != dnto ? dnto.getMsisdn() : "null")
									+ "] DN Listener NULL: " + (null == dnto ? "Delivery Notification TO is null"
											: "Response Status is not OK."));
						}
					}
				}
			}

			private DeliveryNotificationTO listener() {
				DeliveryNotificationTO dnto = null;
				// Start Time
				listenerStartTime = new Date();
				try {
					dnto = coreSMPPService.getDeliveryNotification();
					if (SMPPLOGGER.isDebugEnabled()) {
						SMPPLOGGER.debug("In CoreSMPPHandler:DN Listener started: ");
					}

				} catch (SMPPException e) {
					SMPPLOGGER.debug("In CoreSMPPHandler:DN Listener Exception: " + e.getMessage());
				}
				return dnto;
			}

			private void sendNotificationToTalend(DeliveryNotificationTO dnto) throws Exception {
				if (SMPPLOGGER.isDebugEnabled()) {
					SMPPLOGGER.debug("In CoreSMPPHandler : SendNotificationToTalend start: " + dnto.debugString());
				}
				if (!dnto.isMO()) {
					// Send DN
					sendDeliverDN(dnto);
				} else {
					// Send MO
					sendMO(dnto);
				}
			}

			private void sendMO(DeliveryNotificationTO dnto)
					throws MalformedURLException, ProtocolException, IOException {
				String talendURL = moTalendURL.replace("%text", SmppUtil.encodeToUtf8(dnto.getResponseDNString()))
						.replace("%msisdn", dnto.getMsisdn()).replace("%to", "");
				restCall(dnto, talendURL);
			}

			private void sendDeliverDN(DeliveryNotificationTO dnto)
					throws SMPPException, MalformedURLException, IOException, ProtocolException {
				talendRequestTime = new Date();
				// Get Talend URL
				String talendURL = getTalendURLFromDB(dnto);
				if (null != talendURL) {
					// Split URL to encode separately
					String splitURL[] = talendURL.split("smscid");

					// Call REST service of talend
					String urlString = splitURL[0].replace("%p", dnto.getMsisdn()).replace("%a",
							SmppUtil.encodeToUtf8(dnto.getResponseDNString()))
							+ SmppUtil.encodeToUtf8("smscid" + splitURL[1]);
					int responseCode = restCall(dnto, urlString);
					// Update data in database
					updateDataToDB(dnto, responseCode, urlString);
				}
			}

			private int restCall(DeliveryNotificationTO dnto, String urlString)
					throws MalformedURLException, IOException, ProtocolException {
				URL url = new URL(urlString);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");
				conn.setRequestProperty("Accept", "application/json");
				int responseCode = conn.getResponseCode();
				talendResponseTime = new Date();
				boolean isMO = dnto.isMO();
				if (responseCode == 200) {
					if (SMPPLOGGER.isDebugEnabled()) {
						String data = LoggingBean.logData(dnto, urlString, Integer.valueOf(responseCode).toString(),
								listenerStartTime, responseReceivedTime, talendRequestTime, talendResponseTime);
						SMPPLOGGER.debug(data);
						if (!isMO) {
							DNLOGGER.info(data);
						} else {
							MOLOGGER.info(data);
						}
					}
				} else {
					if (SMPPLOGGER.isDebugEnabled()) {
						String data = LoggingBean.logData(dnto, urlString, Integer.valueOf(responseCode).toString(),
								listenerStartTime, responseReceivedTime, talendRequestTime, talendResponseTime);
						SMPPLOGGER.debug(data);
						if (!isMO) {
							DNLOGGER.info(data);
						} else {
							MOLOGGER.info(data);
						}
					}
				}
				return responseCode;
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
				if (SMPPLOGGER.isDebugEnabled()) {
					SMPPLOGGER.debug("In CoreSMPPHandler : updateDataToDB after DN Receive: " + smppData.toString());
				}

				smppService.update(smppData);
			}

			private String getTalendURLFromDB(DeliveryNotificationTO dnto) throws SMPPException {

				SmppData smppData = new SmppData();
				smppData.setMessageId(dnto.getMessageId());
				smppData.setMsisdn(dnto.getMsisdn());
				System.out.println(smppData.toString());

				smppData = smppService.getRecord(smppData);
				return smppData.getDlrURL();
			}
		}).start();
	}

}
