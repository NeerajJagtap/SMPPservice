package com.vuclip.smpp.core.to;

public class SMPPRespTO {

	private String transId;

	private String dlrURL;

	private Integer respStatus;

	private String resposeMessage;

	private Integer responseId;

	private String responseMsgId;

	public String debugString() {
		return new StringBuilder("Response:[Transaction_Id:").append(transId).append("|DLR_Url:").append(dlrURL)
				.append("|Response_Command_Status:").append(respStatus).append("|Response_Body:").append(resposeMessage)
				.append("|Response_Command_Id:").append(responseId).append("|Response_Message_Id:")
				.append(responseMsgId).append("]").toString();
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
