package com.vuclip.smpp.to;

public class SMPPReqTO {
	private String transId = null;

	private String dlrURL = null;

	private String respStatus = null;

	private PDUTO pduto = null;

	public String getTransId() {
		return transId;
	}

	public void setTransId(String transId) {
		this.transId = transId;
	}

	public String getDlrURL() {
		return dlrURL;
	}

	public void setDlrURL(String dlrURL) {
		this.dlrURL = dlrURL;
	}

	public PDUTO getPduto() {
		return pduto;
	}

	public void setPduto(PDUTO pduto) {
		this.pduto = pduto;
	}

	public String getRespStatus() {
		return respStatus;
	}

	public void setRespStatus(String respStatus) {
		this.respStatus = respStatus;
	}

}
