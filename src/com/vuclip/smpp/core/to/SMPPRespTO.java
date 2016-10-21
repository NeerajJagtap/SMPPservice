package com.vuclip.smpp.core.to;

public class SMPPRespTO {

	private String transId;

	private String dlrURL;

	private Integer respStatus;

	private String resposeMessage;

	private Integer responseId;

	private String responseMsgId;

	public String debugString() {
		return "Response:[Transaction_Id:" + transId + "|DLR_Url:" + dlrURL + "|Response_Command_Status:"
				+ respStatus + "|Response_Body:" + resposeMessage + "|Response_Command_Id:" + responseId
				+ "|Response_Message_Id:" + responseMsgId + "]";
	}

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

	public String getResposeMessage() {
		return resposeMessage;
	}

	public void setResposeMessage(String resposeMessage) {
		this.resposeMessage = resposeMessage;
	}

	public Integer getRespStatus() {
		return respStatus;
	}

	public void setRespStatus(Integer respStatus) {
		this.respStatus = respStatus;
	}

	public Integer getResponseId() {
		return responseId;
	}

	public void setResponseId(Integer responseId) {
		this.responseId = responseId;
	}

	public String getResponseMsgId() {
		return responseMsgId;
	}

	public void setResponseMsgId(String responseMsgId) {
		this.responseMsgId = responseMsgId;
	}

}
