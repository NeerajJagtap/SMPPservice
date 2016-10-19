package com.vuclip.smpp.util;

import org.smpp.Session;
import org.smpp.TCPIPConnection;

public class SMPPSession extends Session {
	private static SMPPSession session = null;

	private SMPPSession(TCPIPConnection connection) {
		super(connection);
	}

	public static SMPPSession getInstance(String ipAddress, int port) {
		TCPIPConnection connection = new TCPIPConnection(ipAddress, port);
		connection.setReceiveTimeout(20 * 1000);
		if (null == session) {
			synchronized (SMPPSession.class) {
				if (null == session) {
					session = new SMPPSession(connection);
				}
			}
		}
		return session;
	}

}
