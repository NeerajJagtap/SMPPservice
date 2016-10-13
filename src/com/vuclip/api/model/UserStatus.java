package com.vuclip.api.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;


@XmlRootElement(name="userStatus")
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class UserStatus {

	private long subscriptionId;

	private long previousSubscriptionId;
	
	private int customerId;

	private int providerId;
	
	private String msisdn;
	
	private String userId;
	
	private int itemId;

	private int itemTypeId;
	
	private int billingCode;
	
	private int subscriptionBillingCode;
	
	private int currentBillingCode;
	
	private int subscriptionStatusId;
	
	private Date startDate;
	
	private Date endDate;
	
	private Date subcriptionDate;
	
	private String isDeactivationInitiated;
	
	private Date lastBillingRequestDate;
	
	private String lastBillingRequestTransactionId;

	private Date lastBillingResponseDate;
	
	private int lastBillingResponseStatusId;
	
	private Date lastNotificationDate;

	private String lastNotificationTransactionId;
	
	private int lastNotificationStatusId;

	private Date nextBillingDate;
	
	private int billingRetry;
	
	private String userSource;
	
	private String chargingSource;
	
	private String customerTransactionId;
	
	private int credits;
	
	private int adnetworkId;

	private String adnetworkTransactionId;
	
	private int adnetworkRetry;

	private String circleCode;

	private Date createDate;

	private Date modifyDate;

	private int lastFallbackBillingCode;
	
	private int currentRetry;
	
	private Date lastContentSmsDate;

	private Date lastDeactivationDate;

	public UserStatus() {
	}

	public long getSubscriptionId() {
		return subscriptionId;
	}

	public void setSubscriptionId(long subscriptionId) {
		this.subscriptionId = subscriptionId;
	}

	public long getPreviousSubscriptionId() {
		return previousSubscriptionId;
	}

	public void setPreviousSubscriptionId(long previousSubscriptionId) {
		this.previousSubscriptionId = previousSubscriptionId;
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public int getProviderId() {
		return providerId;
	}

	public void setProviderId(int providerId) {
		this.providerId = providerId;
	}

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public int getItemTypeId() {
		return itemTypeId;
	}

	public void setItemTypeId(int itemTypeId) {
		this.itemTypeId = itemTypeId;
	}

	public int getBillingCode() {
		return billingCode;
	}

	public void setBillingCode(int billingCode) {
		this.billingCode = billingCode;
	}

	public int getSubscriptionBillingCode() {
		return subscriptionBillingCode;
	}

	public void setSubscriptionBillingCode(int subscriptionBillingCode) {
		this.subscriptionBillingCode = subscriptionBillingCode;
	}

	public int getCurrentBillingCode() {
		return currentBillingCode;
	}

	public void setCurrentBillingCode(int currentBillingCode) {
		this.currentBillingCode = currentBillingCode;
	}

	public int getSubscriptionStatusId() {
		return subscriptionStatusId;
	}

	public void setSubscriptionStatusId(int subscriptionStatusId) {
		this.subscriptionStatusId = subscriptionStatusId;
	}
	
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getSubcriptionDate() {
		return subcriptionDate;
	}

	public void setSubcriptionDate(Date subcriptionDate) {
		this.subcriptionDate = subcriptionDate;
	}

	public String getIsDeactivationInitiated() {
		return isDeactivationInitiated;
	}

	public void setIsDeactivationInitiated(String isDeactivationInitiated) {
		this.isDeactivationInitiated = isDeactivationInitiated;
	}

	public Date getLastBillingRequestDate() {
		return lastBillingRequestDate;
	}

	public void setLastBillingRequestDate(Date lastBillingRequestDate) {
		this.lastBillingRequestDate = lastBillingRequestDate;
	}

	

	public Date getLastBillingResponseDate() {
		return lastBillingResponseDate;
	}

	public void setLastBillingResponseDate(Date lastBillingResponseDate) {
		this.lastBillingResponseDate = lastBillingResponseDate;
	}

	public int getLastBillingResponseStatusId() {
		return lastBillingResponseStatusId;
	}

	public void setLastBillingResponseStatusId(int lastBillingResponseStatusId) {
		this.lastBillingResponseStatusId = lastBillingResponseStatusId;
	}

	public Date getLastNotificationDate() {
		return lastNotificationDate;
	}

	public void setLastNotificationDate(Date lastNotificationDate) {
		this.lastNotificationDate = lastNotificationDate;
	}

	

	public String getLastBillingRequestTransactionId() {
		return lastBillingRequestTransactionId;
	}

	public void setLastBillingRequestTransactionId(
			String lastBillingRequestTransactionId) {
		this.lastBillingRequestTransactionId = lastBillingRequestTransactionId;
	}

	public String getLastNotificationTransactionId() {
		return lastNotificationTransactionId;
	}

	public void setLastNotificationTransactionId(
			String lastNotificationTransactionId) {
		this.lastNotificationTransactionId = lastNotificationTransactionId;
	}

	public int getLastNotificationStatusId() {
		return lastNotificationStatusId;
	}

	public void setLastNotificationStatusId(int lastNotificationStatusId) {
		this.lastNotificationStatusId = lastNotificationStatusId;
	}

	public Date getNextBillingDate() {
		return nextBillingDate;
	}

	public void setNextBillingDate(Date nextBillingDate) {
		this.nextBillingDate = nextBillingDate;
	}

	public int getBillingRetry() {
		return billingRetry;
	}

	public void setBillingRetry(int billingRetry) {
		this.billingRetry = billingRetry;
	}

	public String getUserSource() {
		return userSource;
	}

	public void setUserSource(String userSource) {
		this.userSource = userSource;
	}

	public String getChargingSource() {
		return chargingSource;
	}

	public void setChargingSource(String chargingSource) {
		this.chargingSource = chargingSource;
	}

	public String getCustomerTransactionId() {
		return customerTransactionId;
	}

	public void setCustomerTransactionId(String customerTransactionId) {
		this.customerTransactionId = customerTransactionId;
	}

	public int getCredits() {
		return credits;
	}

	public void setCredits(int credits) {
		this.credits = credits;
	}

	public int getAdnetworkId() {
		return adnetworkId;
	}

	public void setAdnetworkId(int adnetworkId) {
		this.adnetworkId = adnetworkId;
	}

	public String getAdnetworkTransactionId() {
		return adnetworkTransactionId;
	}

	public void setAdnetworkTransactionId(String adnetworkTransactionId) {
		this.adnetworkTransactionId = adnetworkTransactionId;
	}

	public int getAdnetworkRetry() {
		return adnetworkRetry;
	}

	public void setAdnetworkRetry(int adnetworkRetry) {
		this.adnetworkRetry = adnetworkRetry;
	}

	public String getCircleCode() {
		return circleCode;
	}

	public void setCircleCode(String circleCode) {
		this.circleCode = circleCode;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	public int getLastFallbackBillingCode() {
		return lastFallbackBillingCode;
	}

	public void setLastFallbackBillingCode(int lastFallbackBillingCode) {
		this.lastFallbackBillingCode = lastFallbackBillingCode;
	}

	public int getCurrentRetry() {
		return currentRetry;
	}

	public void setCurrentRetry(int currentRetry) {
		this.currentRetry = currentRetry;
	}

	public Date getLastContentSmsDate() {
		return lastContentSmsDate;
	}

	public void setLastContentSmsDate(Date lastContentSmsDate) {
		this.lastContentSmsDate = lastContentSmsDate;
	}

	public Date getLastDeactivationDate() {
		return lastDeactivationDate;
	}

	public void setLastDeactivationDate(Date lastDeactivationDate) {
		this.lastDeactivationDate = lastDeactivationDate;
	}


		
}
