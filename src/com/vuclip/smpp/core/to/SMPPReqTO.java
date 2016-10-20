package com.vuclip.smpp.core.to;

public class SMPPReqTO {
	private String transId = null;

	private String dlrURL = null;

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

	public String debugString() {
		return "Transaction ID : " + transId + " DLR url : " + dlrURL + "\n Request PDU : " + pduto.debugString();
	}

}
