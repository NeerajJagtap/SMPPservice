package com.vuclip.smpp.client;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vuclip.smpp.props.SMPPProperties;
import com.vuclip.smpp.service.impl.CoreSMPPServiceImpl;
import com.vuclip.smpp.to.ConfigTO;
import com.vuclip.smpp.to.SMPPReqTO;
import com.vuclip.smpp.to.SMPPRespTO;

public class CoreSMPPClient {
	private static final Logger logger = LoggerFactory.getLogger(CoreSMPPServiceImpl.class);

	private static volatile BlockingQueue<SMPPReqTO> smppReqQueue = new ArrayBlockingQueue<SMPPReqTO>(10000);

	public static volatile BlockingQueue<SMPPRespTO> smppRespQueue = new ArrayBlockingQueue<SMPPRespTO>(10000);

	private boolean isSMSJobRunning = false;

	private boolean isResponseHandlerRunning = false;

	private boolean isReceiverListenerRunning = false;

	private static final String PROPS_FILE_PATH = "./resources/smpp-config.properties";

	private static final String OPTIONAL_PROP_NAME = "opt_param";

	private static final String OPTIONAL_TAG_STRING = "_tag";

	private Properties properties = new Properties();

	private ConfigTO configTO = new ConfigTO();

	private SMPPProperties config;

	public CoreSMPPClient(SMPPProperties config) throws IOException {
		initialize(config);
	}

	private void initialize(SMPPProperties config) throws IOException {
		// loadPeropertiesFile();
		this.config = config;
		loadConfigurations();
	}

	private void loadPeropertiesFile() throws IOException {
		if (logger.isDebugEnabled()) {
			logger.debug("Property Load Start");
		}
		FileInputStream propsFile = new FileInputStream(PROPS_FILE_PATH);
		properties.load(propsFile);
		propsFile.close();
		if (logger.isDebugEnabled()) {
			logger.debug("Properties load Success");
		}
	}

	private void loadConfigurations() {
		if (logger.isDebugEnabled()) {
			logger.debug("Configuration Load Start");
		}
		// Required Parameters
		// configTO.setIpAddress(environment.getProperty("smpp_server_ip"));
		// configTO.setPort(Integer.valueOf(environment.getProperty("smpp_server_port")));
		// configTO.setBindOption(environment.getProperty("smpp_bind_option"));
		// configTO.setAsynchorized(environment.getProperty("smpp_sync_option"));
		// configTO.setSystemType(environment.getProperty("system_type"));
		// configTO.setSystemId(environment.getProperty("system_id"));
		// configTO.setPassword(environment.getProperty("password"));

		configTO.setIpAddress(config.getSmppServerIP());
		configTO.setPort(Integer.valueOf(config.getSmppServerPort()));
		configTO.setBindOption(config.getSmppBindOption());
		configTO.setAsynchorized(config.getSmppSyncOption());
		configTO.setSystemType(config.getSystemType());
		configTO.setSystemId(config.getSystemID());
		configTO.setPassword(config.getPassword());
		// Optional Parameters
		Map<Integer, Integer> optionalMap = new HashMap<Integer, Integer>();
		// Integer propertyNo = 1;
		// String optionalParam = OPTIONAL_PROP_NAME + propertyNo.toString();
		// while (null != environment.getProperty(optionalParam)) {
		// optionalMap.put(Integer
		// .valueOf(environment.getProperty(optionalParam +
		// OPTIONAL_TAG_STRING).trim().substring(2), 16),
		// Integer.valueOf(environment.getProperty(optionalParam).trim()));
		// propertyNo = propertyNo + 1;
		// optionalParam = OPTIONAL_PROP_NAME + propertyNo.toString();
		// }

		optionalMap.put(Integer.valueOf(config.getOptionalParam1Tag(), 16),
				Integer.valueOf(config.getOptionalParam1()));
		optionalMap.put(Integer.valueOf(config.getOptionalParam2Tag(), 16),
				Integer.valueOf(config.getOptionalParam2()));
		optionalMap.put(Integer.valueOf(config.getOptionalParam3Tag(), 16),
				Integer.valueOf(config.getOptionalParam3()));

		configTO.setOptionalParamMap(optionalMap);
		if (logger.isDebugEnabled()) {
			logger.debug("Configuration load Success");
		}
	}

	public boolean submitSMSRequest(SMPPReqTO smppReqTO) {
		smppReqQueue.add(smppReqTO);
		return true;
	}

	public void runSMSJob() throws InterruptedException {
		if (!isSMSJobRunning) {
			final ExecutorService executorService = Executors.newFixedThreadPool(5);
			new Thread(new Runnable() {

				@Override
				public void run() {
					while (true) {
						if (!isSMSJobRunning) {
							isSMSJobRunning = true;
						}
						if (!smppReqQueue.isEmpty()) {
							try {
								SMPPReqTO reqTO = smppReqQueue.take();
								executorService.execute(new CoreSMPPServiceImpl(configTO, reqTO));
							} catch (InterruptedException e) {
								if (logger.isErrorEnabled()) {
									logger.error("Error in SMS job thread. " + e.getMessage());
								}
							}
						}
					}
				}
			}).start();

		}
	}

	public void runResponseHandlerJob() {
		if (!isResponseHandlerRunning) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					if (!isResponseHandlerRunning) {
						isResponseHandlerRunning = true;
					}
					while (true) {
						if (!smppRespQueue.isEmpty()) {
							try {
								SMPPRespTO respTO = smppRespQueue.take();
								System.out.println("Response : " + respTO.getDlrURL() + " Command Status : "
										+ respTO.getRespStatus());
							} catch (InterruptedException e) {
								if (logger.isErrorEnabled()) {
									logger.error("Error in SMS job thread. " + e.getMessage());
								}
							}
						}
					}
				}
			}).start();
		}
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
						SMPPRespTO respTO = new CoreSMPPServiceImpl(configTO, null).receiveListener();
						if (null != respTO) {
							smppRespQueue.add(respTO);
						}
					}
				}
			}).start();
		}
	}

}
