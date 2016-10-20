package com.vuclip.smpp.core.util;

import org.smpp.Session;
import org.smpp.TCPIPConnection;

public class SMPPSession extends Session {
	private SMPPSession(TCPIPConnection connection) {
		super(connection);
	}

	public static SMPPSession getInstance(String ipAddress, int port) {
		TCPIPConnection connection = new TCPIPConnection(ipAddress, port);
		connection.setReceiveTimeout(20 * 1000);
		return new SMPPSession(connection);
	}

}
