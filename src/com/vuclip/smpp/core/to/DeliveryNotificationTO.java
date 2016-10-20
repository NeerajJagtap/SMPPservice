package com.vuclip.smpp.core.to;

import com.vuclip.smpp.core.constants.MessageState;

public class DeliveryNotificationTO {

	private String responseDNString;

	private String deliveryStatus;

	private MessageState messageState;

	private String messageId;

	private String submitDate;

	private String doneDate;

	private String msisdn;

	public String debugString() {

		return "Deliver SM : msisdn : " + msisdn + " Message Id : " + messageId + " Message State : "
				+ messageState.name() + " Submit Date : " + submitDate + " Done Date : " + doneDate;
	}

	public String getResponseDNString() {
		return responseDNString;
	}

	public void setResponseDNString(String responseDNString) {
		this.responseDNString = responseDNString;
	}

	public String getDeliveryStatus() {
		return deliveryStatus;
	}

	public void setDeliveryStatus(String deliveryStatus) {
		this.deliveryStatus = deliveryStatus;
	}

	public MessageState getMessageState() {
		return messageState;
	}

	public void setMessageState(MessageState messageState) {
		this.messageState = messageState;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getSubmitDate() {
		return submitDate;
	}

	public void setSubmitDate(String submitDate) {
		this.submitDate = submitDate;
	}

	public String getDoneDate() {
		return doneDate;
	}

	public void setDoneDate(String doneDate) {
		this.doneDate = doneDate;
	}

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

}
