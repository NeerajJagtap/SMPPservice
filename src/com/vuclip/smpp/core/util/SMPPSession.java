package com.vuclip.smpp.core.util;

import org.smpp.Session;
import org.smpp.TCPIPConnection;

public class SMPPSession extends Session {

	private static SMPPSession session;
	
	private static Object mutex= new Object();

	private static final Long TIME_OUT = (long) (20 * 1000);

	private SMPPSession(TCPIPConnection connection) {
		super(connection);
	}

	public static SMPPSession getInstance(String ipAddress, int port) {
		if (null == session) {
			synchronized (mutex) {
				if (null == session) {
					TCPIPConnection connection = new TCPIPConnection(ipAddress, port);
					connection.setReceiveTimeout(TIME_OUT);
					session = new SMPPSession(connection);
				}
			}
		}
		return session;
	}

}
