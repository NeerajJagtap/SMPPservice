package com.vuclip.smpp.client;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vuclip.smpp.service.impl.CoreSMPPServiceImpl;
import com.vuclip.smpp.to.ConfigTO;
import com.vuclip.smpp.to.PDUTO;
import com.vuclip.smpp.to.SMPPReqTO;

public class CoreSMPPClient {
	private static final Logger logger = LoggerFactory.getLogger(CoreSMPPServiceImpl.class);

	private BlockingQueue<SMPPReqTO> smppReqQueue = new ArrayBlockingQueue<SMPPReqTO>(10000);

	private BlockingQueue<SMPPReqTO> smppRespQueue = new ArrayBlockingQueue<SMPPReqTO>(10000);

	private boolean isSMSJobRunning = false;

	private boolean isResponseHandlerRunning = false;

	public boolean submitSMSRequest(SMPPReqTO smppReqTO) {
		smppReqQueue.add(smppReqTO);
		return true;
	}

	public void runSMSJob() throws InterruptedException {
		if (!isSMSJobRunning) {
			final ConfigTO configTO = new ConfigTO();
			final ExecutorService executorService = Executors.newFixedThreadPool(5);
			new Thread(new Runnable() {

				@Override
				public void run() {
					while (true) {
						isSMSJobRunning = true;
						if (!smppReqQueue.isEmpty()) {
							try {

								SMPPReqTO reqTO = smppReqQueue.take();
								executorService.execute(new CoreSMPPServiceImpl(configTO, reqTO));
								smppRespQueue.add(reqTO);
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
					isResponseHandlerRunning = true;
					while (true) {
						if (!smppRespQueue.isEmpty()) {
							try {
								SMPPReqTO respTO = smppRespQueue.take();
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

}
