package com.vuclip.smpp.core.to;

import org.smpp.Data;
import org.smpp.pdu.Address;

public class SMPPReqTO {
	private String systemType = "";
	private String serviceType = "";
	private Address sourceAddress = null;
	private Address destAddress = null;
	private String scheduleDeliveryTime = null;
	private String validityPeriod = null;
	private String shortMessage = "";
	private String messagePayload = null;
	private String messageId = "";
	private byte esmClass = (byte) Data.SM_STORE_FORWARD_MODE;
	private byte protocolId = 0;
	private byte priorityFlag = 0;
	private byte registeredDelivery = 1;
	private byte replaceIfPresentFlag = 0;
	private byte dataCoding = 0;
	private byte smDefaultMsgId = 0;

	private SMPPRespTO expetedResponseTO;

	public String debugString() {
		return new StringBuilder("Request[System_Type:").append(systemType).append("|Service_Type:").append(serviceType)
				.append("|Source_Address:").append(sourceAddress.debugString()).append("|Destination_Address:")
				.append(destAddress.debugString()).append("|Schedule_Delivery_Time:").append(scheduleDeliveryTime)
				.append("|Validity_Period:").append(validityPeriod).append("|Short_Message:").append(shortMessage)
				.append("|Message_Payload:").append(messagePayload).append("|Message_Id:").append(messageId)
				.append("|ESM_Class:").append(esmClass).append("|Protocol_Id:").append(protocolId)
				.append("|Priority_Flag:").append(priorityFlag).append("|Register_Delivery:").append(registeredDelivery)
				.append("|Replace_If_Present_Flag:").append(replaceIfPresentFlag).append("|Data_Coding:")
				.append(dataCoding).append("|Default_Msg_Id:").append(smDefaultMsgId).append("]").toString();
	}

	public String getSystemType() {
		return systemType;
	}

	public void setSystemType(String systemType) {
		this.systemType = systemType;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public Address getSourceAddress() {
		return sourceAddress;
	}

	public void setSourceAddress(Address sourceAddress) {
		this.sourceAddress = sourceAddress;
	}

	public Address getDestAddress() {
		return destAddress;
	}

	public void setDestAddress(Address destAddress) {
		this.destAddress = destAddress;
	}

	public String getScheduleDeliveryTime() {
		return scheduleDeliveryTime;
	}

	public void setScheduleDeliveryTime(String scheduleDeliveryTime) {
		this.scheduleDeliveryTime = scheduleDeliveryTime;
	}

	public String getValidityPeriod() {
		return validityPeriod;
	}

	public void setValidityPeriod(String validityPeriod) {
		this.validityPeriod = validityPeriod;
	}

	public String getShortMessage() {
		return shortMessage;
	}

	public void setShortMessage(String shortMessage) {
		this.shortMessage = shortMessage;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public byte getEsmClass() {
		return esmClass;
	}

	public void setEsmClass(byte esmClass) {
		this.esmClass = esmClass;
	}

	public byte getProtocolId() {
		return protocolId;
	}

	public void setProtocolId(byte protocolId) {
		this.protocolId = protocolId;
	}

	public byte getPriorityFlag() {
		return priorityFlag;
	}

	public void setPriorityFlag(byte priorityFlag) {
		this.priorityFlag = priorityFlag;
	}

	public byte getRegisteredDelivery() {
		return registeredDelivery;
	}

	public void setRegisteredDelivery(byte registeredDelivery) {
		this.registeredDelivery = registeredDelivery;
	}

	public byte getReplaceIfPresentFlag() {
		return replaceIfPresentFlag;
	}

	public void setReplaceIfPresentFlag(byte replaceIfPresentFlag) {
		this.replaceIfPresentFlag = replaceIfPresentFlag;
	}

	public byte getDataCoding() {
		return dataCoding;
	}

	public void setDataCoding(byte dataCoding) {
		this.dataCoding = dataCoding;
	}

	public byte getSmDefaultMsgId() {
		return smDefaultMsgId;
	}

	public void setSmDefaultMsgId(byte smDefaultMsgId) {
		this.smDefaultMsgId = smDefaultMsgId;
	}

	public String getMessagePayload() {
		return messagePayload;
	}

	public void setMessagePayload(String messagePayload) {
		this.messagePayload = messagePayload;
	}

	public SMPPRespTO getExpetedResponseTO() {
		return expetedResponseTO;
	}

	public void setExpetedResponseTO(SMPPRespTO expetedResponseTO) {
		this.expetedResponseTO = expetedResponseTO;
	}

}
