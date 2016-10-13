package com.vuclip.smpp.to;

public class ConfigTO {
	private boolean isAsynchorized = false;
	private String bindOption = "tr";
	private String ipAddress = "127.0.0.1";
	private int port = 9400;
	private String systemType = "NULL";
	private String password = "testbr01";
	private String systemId = "71";

	private int pricePoint = 60225;
	private int productId = 681;
	private int rollId = 71;

	private static final short PRICE_POINT_TAG = (short) 0x1400;
	private static final short PRODUCT_ID_TAG = (short) 0x1401;
	private static final short ROLL_ID_TAG = (short) 0x1402;

	public boolean isAsynchorized() {
		return isAsynchorized;
	}

	public void setAsynchorized(boolean isAsynchorized) {
		this.isAsynchorized = isAsynchorized;
	}

	public String getBindOption() {
		return bindOption;
	}

	public void setBindOption(String bindOption) {
		this.bindOption = bindOption;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getSystemType() {
		return systemType;
	}

	public void setSystemType(String systemType) {
		this.systemType = systemType;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

}
