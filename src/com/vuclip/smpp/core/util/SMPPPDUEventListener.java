package com.vuclip.smpp.core.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.smpp.ServerPDUEvent;
import org.smpp.ServerPDUEventListener;
import org.smpp.Session;
import org.smpp.SmppObject;
import org.smpp.pdu.PDU;
import org.smpp.pdu.ValueNotSetException;
import org.smpp.util.Queue;

public class SMPPPDUEventListener extends SmppObject implements ServerPDUEventListener {
	Session session;
	Queue requestEvents = new Queue();
	
	Logger smpplogger = LogManager.getLogger("smpplogger");

	public SMPPPDUEventListener(Session session) {
		this.session = session;
	}

	public void handleEvent(ServerPDUEvent event) {
		PDU pdu = event.getPDU();
		if (pdu.isRequest()) {
			if (smpplogger.isDebugEnabled()) {
				smpplogger.debug("In SMPPPDUEventListener : async request received, enqueuing " + pdu.debugString());
			}
			synchronized (requestEvents) {
				requestEvents.enqueue(event);
				requestEvents.notify();
			}
		} else if (pdu.isResponse()) {
			if (smpplogger.isDebugEnabled()) {
				try {

					smpplogger.debug(pdu.getData().getBuffer().toString());
				} catch (ValueNotSetException e) {
					smpplogger.error(e.getMessage());
				}
				smpplogger.debug("In SMPPPDUEventListener : async response received " + pdu.debugString());
			}
		} else {
			if (smpplogger.isDebugEnabled()) {
				smpplogger.debug("In SMPPPDUEventListener : pdu of unknown class (not request nor " + "response) received, discarding "
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
