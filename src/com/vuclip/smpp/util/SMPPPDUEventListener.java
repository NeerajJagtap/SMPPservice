package com.vuclip.smpp.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.logica.smpp.ServerPDUEvent;
import com.logica.smpp.ServerPDUEventListener;
import com.logica.smpp.Session;
import com.logica.smpp.SmppObject;
import com.logica.smpp.pdu.PDU;
import com.logica.smpp.pdu.ValueNotSetException;
import com.logica.smpp.util.Queue;
import com.vuclip.smpp.controllers.SmppController;

public class SMPPPDUEventListener extends SmppObject implements ServerPDUEventListener {
	Session session;
	Queue requestEvents = new Queue();
	private static final Logger logger = LoggerFactory.getLogger(SmppController.class);

	public SMPPPDUEventListener(Session session) {
		this.session = session;
	}

	public void handleEvent(ServerPDUEvent event) {
		PDU pdu = event.getPDU();
		if (pdu.isRequest()) {
			if (logger.isDebugEnabled()) {
				logger.debug("async request received, enqueuing " + pdu.debugString());
			}
			synchronized (requestEvents) {
				requestEvents.enqueue(event);
				requestEvents.notify();
			}
		} else if (pdu.isResponse()) {
			if (logger.isDebugEnabled()) {
				try {

					logger.debug(pdu.getData().getBuffer().toString());
				} catch (ValueNotSetException e) {
					logger.error(e.getMessage());
				}
				logger.debug("async response received " + pdu.debugString());
			}
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("pdu of unknown class (not request nor " + "response) received, discarding "
						+ pdu.debugString());
			}
		}
	}

	/**
	 * Returns received pdu from the queue. If the queue is empty, the method
	 * blocks for the specified timeout.
	 */
	public ServerPDUEvent getRequestEvent(long timeout) {
		ServerPDUEvent pduEvent = null;
		synchronized (requestEvents) {
			if (requestEvents.isEmpty()) {
				try {
					requestEvents.wait(timeout);
				} catch (InterruptedException e) {
					// ignoring, actually this is what we're waiting for
				}
			}
			if (!requestEvents.isEmpty()) {
				pduEvent = (ServerPDUEvent) requestEvents.dequeue();
			}
		}
		return pduEvent;
	}
}
