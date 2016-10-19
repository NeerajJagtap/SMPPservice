package com.vuclip.smpp.client;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smpp.TimeoutException;
import org.smpp.WrongSessionStateException;
import org.smpp.pdu.PDUException;
import org.smpp.pdu.ValueNotSetException;

import com.vuclip.smpp.props.SMPPProperties;
import com.vuclip.smpp.service.impl.CoreSMPPServiceImpl;
import com.vuclip.smpp.to.ConfigTO;
import com.vuclip.smpp.to.SMPPReqTO;
import com.vuclip.smpp.to.SMPPRespTO;

public class CoreSMPPHandler {
	private static final Logger logger = LoggerFactory.getLogger(CoreSMPPServiceImpl.class);

	private boolean isReceiverListenerRunning = false;

	private ConfigTO configTO = new ConfigTO();

	//private WebProperties config;
	private SMPPProperties config;
	
	private CoreSMPPServiceImpl coreSMPPServiceImpl = null;

	public CoreSMPPHandler(SMPPProperties config) throws IOException {
		initialize(config);
	}

	private void initialize(SMPPProperties config) throws IOException {
		this.config = config;
		loadConfigurations();
	}

	private void loadConfigurations() {
		if (logger.isDebugEnabled()) {
			logger.debug("Configuration Load Start");
		}
		configTO.setIpAddress(config.getSmppServerIP());
		configTO.setPort(Integer.valueOf(config.getSmppServerPort()));
		configTO.setBindOption(config.getSmppBindOption());
		configTO.setAsynchorized(config.getSmppSyncOption());
		configTO.setSystemType(config.getSystemType());
		configTO.setSystemId(config.getSystemID());
		configTO.setPassword(config.getPassword());

		// Optional Parameters
		Map<Integer, Integer> optionalMap = new HashMap<Integer, Integer>();

		optionalMap.put(Integer.valueOf(config.getOptionalParam1Tag(), 16),
				Integer.valueOf(config.getOptionalParam1()));
		optionalMap.put(Integer.valueOf(config.getOptionalParam2Tag(), 16),
				Integer.valueOf(config.getOptionalParam2()));
		optionalMap.put(Integer.valueOf(config.getOptionalParam3Tag(), 16),
				Integer.valueOf(config.getOptionalParam3()));

		configTO.setOptionalParamMap(optionalMap);
		coreSMPPServiceImpl = new CoreSMPPServiceImpl(configTO);
		
		if (logger.isDebugEnabled()) {
			logger.debug("Configuration load Success");
		}
	}

	public SMPPRespTO submitSMSRequest(SMPPReqTO smppReqTO)
			throws ValueNotSetException, TimeoutException, PDUException, WrongSessionStateException, IOException {
		coreSMPPServiceImpl.setSmppReqTO(smppReqTO);
		return coreSMPPServiceImpl.submitMessagePDU();
	}

	public void runReceiverListener() {
		if (!isReceiverListenerRunning) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					if (!isReceiverListenerRunning) {
						isReceiverListenerRunning = true;
					}
					while (true) {
						SMPPRespTO respTO = coreSMPPServiceImpl.receiveListener();
						if (null != respTO) {
							System.out.println(
									"Response : " + respTO.getDlrURL() + " Command Status : " + respTO.getRespStatus());
						}
					}
				}
			}).start();
		}
	}

}
