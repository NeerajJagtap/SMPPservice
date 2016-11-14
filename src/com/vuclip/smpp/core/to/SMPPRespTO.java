package com.vuclip.smpp.core.to;

public class SMPPRespTO {

	private String transId;

	private String dlrURL;

	private Integer respStatus;

	private String resposeMessage;

	private Integer responseId;

	private String responseMsgId;

	private String msisdn;

	private String pricePoint;

	public String debugString() {
		return new StringBuilder("Response:[Transaction_Id:").append(transId).append("|MSISDN:").append(msisdn)
				.append("|PricePoint:").append(pricePoint).append("|DLR_Url:").append(dlrURL)
				.append("|Response_Command_Status:").append(respStatus).append("|Response_Body:").append(resposeMessage)
				.append("|Response_Command_Id:").append(responseId).append("|Response_Message_Id:")
				.append(responseMsgId).append("]").toString();
	}

	public String getPricePoint() {
		return pricePoint;
	}

	public void setPricePoint(String pricePoint) {
		this.pricePoint = pricePoint;
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

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

}
