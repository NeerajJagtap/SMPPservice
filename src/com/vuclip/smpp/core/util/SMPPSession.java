package com.vuclip.smpp.core.util;

import org.smpp.Session;
import org.smpp.TCPIPConnection;

public class SMPPSession extends Session {

	private static SMPPSession session;

	private static Object mutex = new Object();

	private static final Long TIME_OUT = (long) (10 * 1000);

	private SMPPSession(TCPIPConnection connection) {
		super(connection);
	}

	public static SMPPSession getInstance(String ipAddress, int port) {
		if (null == session) {
				if (null == session) {
					TCPIPConnection connection = new TCPIPConnection(ipAddress, port);
					connection.setReceiveTimeout(TIME_OUT);
					session = new SMPPSession(connection);
				}
		}
		return session;
	}

	public static void flushSession() {
		session = null;
	}

}
