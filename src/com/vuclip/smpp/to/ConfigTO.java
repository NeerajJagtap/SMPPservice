package com.vuclip.smpp.to;

import java.util.Map;

public class ConfigTO {
	private static final String SYNC = "sync";

	private boolean isAsynchorized = false;

	private String bindOption = "tr";
	private String ipAddress = "127.0.0.1";
	private Integer port = 9400;
	private String systemType = "NULL";
	private String password = "testbr01";
	private String systemId = "71";

	private Map<Integer, Integer> optionalParamMap;

	public boolean isAsynchorized() {
		return isAsynchorized;
	}

	public void setAsynchorized(String stringSync) {
		if (SYNC.equals(stringSync)) {
			this.isAsynchorized = false;
		} else {
			this.isAsynchorized = true;
		}
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

	public void setAsynchorized(boolean isAsynchorized) {
		this.isAsynchorized = isAsynchorized;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public Map<Integer, Integer> getOptionalParamMap() {
		return optionalParamMap;
	}

	public void setOptionalParamMap(Map<Integer, Integer> optionalParamMap) {
		this.optionalParamMap = optionalParamMap;
	}

}
