package com.vuclip.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vuclip.smpp.core.to.DeliveryNotificationTO;
import com.vuclip.smpp.props.WebProperties;

@Component
public class LoggingBean {

	private Date timeStamp;
	private String msisdn;
	private String transactionId;
	private String activityType;
	private String pricePoint;
	private Date requestTime;
	private Date responseTime;
	private String requestResponseTimeInterval;
	private String smppMethod;
	private String rawRequest;
	private String rawResponse;
	private String requestToSmpp;
	private String responseFromSmpp;
	private String requestFromSmpp;
	private String responseToSmpp;
	private String additionalParameter;

	private String sendsmsTxt;
	private String billingTxt;
	private String zeroPricePoint;
	private String providerId;
	private String customerId;

	private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

	@Autowired
	WebProperties properties;

	public LoggingBean() {
		// TODO Auto-generated constructor stub
	}

	@Autowired
	public LoggingBean(WebProperties properties) {
		this.sendsmsTxt = properties.getSendsmsTxt();
		this.billingTxt = properties.getBillingTxt();
		this.zeroPricePoint = properties.getZeroPricePoint();
		this.providerId = properties.getProviderId();
		this.customerId = properties.getCustomerId();
	}

	public LoggingBean(Date timeStamp, String msisdn, String transactionId, String pricePoint, Date requestTime,
			Date responseTime, String requestResponseTimeInterval, String smppMethod, String rawRequest,
			String rawResponse, String requestToSmpp, String responseFromSmpp, String requestFromSmpp,
			String responseToSmpp, String additionalParameters) {

		this.timeStamp = timeStamp;
		this.msisdn = msisdn;
		this.transactionId = transactionId;
		this.pricePoint = pricePoint;
		this.requestTime = requestTime;
		this.responseTime = responseTime;
		this.requestResponseTimeInterval = requestResponseTimeInterval;
		this.smppMethod = smppMethod;
		this.rawRequest = rawRequest;
		this.rawResponse = rawResponse;
		this.requestToSmpp = requestToSmpp;
		this.responseFromSmpp = responseFromSmpp;
		this.requestFromSmpp = requestFromSmpp;
		this.responseToSmpp = responseToSmpp;
		this.additionalParameter = additionalParameters;
	}

	public String getPricePoint() {
		return pricePoint;
	}

	public void setPricePoint(String pricePoint) {
		this.pricePoint = pricePoint;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	public String getProviderId() {
		return providerId;
	}

	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getActivityType() {
		return activityType;
	}

	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}

	public Date getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(Date requestTime) {
		this.requestTime = requestTime;
	}

	public Date getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(Date responseTime) {
		this.responseTime = responseTime;
	}

	public String getRequestResponseTimeInterval() {
		return requestResponseTimeInterval;
	}

	public void setRequestResponseTimeInterval(String requestResponseTimeInterval) {
		this.requestResponseTimeInterval = requestResponseTimeInterval;
	}

	public String getSmppMethod() {
		return smppMethod;
	}

	public void setSmppMethod(String smppMethod) {
		this.smppMethod = smppMethod;
	}

	public String getRawRequest() {
		return rawRequest;
	}

	public void setRawRequest(String rawRequest) {
		this.rawRequest = rawRequest;
	}

	public String getRawResponse() {
		return rawResponse;
	}

	public void setRawResponse(String rawResponse) {
		this.rawResponse = rawResponse;
	}

	public String getRequestToSmpp() {
		return requestToSmpp;
	}

	public void setRequestToSmpp(String requestToSmpp) {
		this.requestToSmpp = requestToSmpp;
	}

	public String getResponseFromSmpp() {
		return responseFromSmpp;
	}

	public void setResponseFromSmpp(String responseFromSmpp) {
		this.responseFromSmpp = responseFromSmpp;
	}

	public String getRequestFromSmpp() {
		return requestFromSmpp;
	}

	public void setRequestFromSmpp(String requestFromSmpp) {
		this.requestFromSmpp = requestFromSmpp;
	}

	public String getResponseToSmpp() {
		return responseToSmpp;
	}

	public void setResponseToSmpp(String responseToSmpp) {
		this.responseToSmpp = responseToSmpp;
	}

	public String getAdditionalParameter() {
		return additionalParameter;
	}

	public void setAdditionalParameter(String additionalParameter) {
		this.additionalParameter = additionalParameter;
	}

	/**
	 * 
	 * @param msisdn
	 *            - Mobile number of the user
	 * @param providerId
	 *            - Carrier identifier
	 * @param customerId
	 *            - Carrier billing entity identifier
	 * @param transactionId
	 *            - Id for identifying transaction
	 * @param activityType
	 *            - it can be activation, deactivation,renewals, sendSms
	 * @param requestTime
	 *            - Time on which request was received on receiving end point of
	 *            talend job
	 * @param responseTime
	 *            - Time on which response was provided to the talend invoking
	 *            system
	 * @param requestResponseTimeInterval
	 *            - Its the turn around time of request received and response
	 *            provided to the talend invoking system
	 * @param smppMethod
	 *            - smppMethod used
	 * @param rawRequest
	 *            - Parameters in rawrequest
	 * @param rawResponse
	 *            - Response given to carrier
	 * @param requestToSmpp
	 *            - request which is coming to smppservice from talend
	 * @param responseFromSmpp
	 *            - response which is going from smppservice to talend
	 * @param requestFromSmpp
	 *            - request which is going from smppservice to talend
	 * @param responseToSmpp
	 *            - response which is coming to smppservice from talend
	 * @return
	 */
	public static String getLogFormat(Date timeStamp, String msisdn, String providerId, String customerId,
			String transactionId, String activityType, Date requestTime, Date responseTime,
			String requestResponseTimeInterval, String smppMethod, String rawRequest, String rawResponse,
			String requestToSmpp, String responseFromSmpp, String requestFromSmpp, String responseToSmpp,
			String additionalParameters) {

		if (transactionId == null) {
			transactionId = msisdn + "_" + System.nanoTime();
		}

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
		StringBuilder toStringForm = new StringBuilder(
				"\n+-------------+-------------+-------------+-------------+-------------+-------------+-------------+-------------+-------------+-------------+-------------+-------------+\n");
		toStringForm.append("timeStamp[").append(df.format(timeStamp));
		toStringForm.append("] msisdn[").append(msisdn);
		toStringForm.append("] providerId[").append(providerId);
		toStringForm.append("] customerId[").append(customerId);
		toStringForm.append("] transactionId[").append(transactionId);
		toStringForm.append("] activityType[").append(activityType);
		toStringForm.append("] requestTime[").append(df.format(requestTime));
		toStringForm.append("] responseTime[").append(df.format(responseTime));
		toStringForm.append("] requestResponseTimeInterval[").append(requestResponseTimeInterval);
		toStringForm.append("] smppMethod[").append(smppMethod);
		toStringForm.append("] rawRequest[").append(rawRequest);
		toStringForm.append("] rawResponse[").append(rawResponse);
		toStringForm.append("] requestToSmpp[").append(requestToSmpp);
		toStringForm.append("] responseFromSmpp[").append(responseFromSmpp);
		toStringForm.append("] requestFromSmpp[").append(requestFromSmpp);
		toStringForm.append("] responseToSmpp[").append(responseToSmpp);
		toStringForm.append("] additionalParameters[").append(additionalParameters + "]");
		toStringForm.append(
				"\n+-------------+-------------+-------------+-------------+-------------+-------------+-------------+-------------+-------------+-------------+-------------+-------------+\n");

		return toStringForm.toString();
	}

	public String getLogFormat() {

		if (this.transactionId == null) {
			this.transactionId = (this.activityType == null ? "NA" : this.activityType) + "_" + this.msisdn + "_"
					+ System.nanoTime();
		}

		StringBuilder toStringForm = new StringBuilder(
				"\n+-------------+-------------+-------------+-------------+-------------+-------------+-------------+-------------+-------------+-------------+-------------+-------------+\n");
		toStringForm.append("timeStamp[").append(dateFormat.format(this.timeStamp));
		toStringForm.append("] msisdn[").append(this.msisdn);
		toStringForm.append("] providerId[").append(this.providerId);
		toStringForm.append("] customerId[").append(this.customerId);
		toStringForm.append("] transactionId[").append(this.transactionId);
		toStringForm.append("] activityType[").append(this.activityType);
		toStringForm.append("] requestTime[").append(dateFormat.format(this.requestTime));
		toStringForm.append("] responseTime[").append(dateFormat.format(this.responseTime));
		toStringForm.append("] requestResponseTimeInterval[").append(this.requestResponseTimeInterval);
		toStringForm.append("] smppMethod[").append(this.smppMethod);
		toStringForm.append("] rawRequest[").append(this.rawRequest);
		toStringForm.append("] rawResponse[").append(this.rawResponse);
		toStringForm.append("] requestToSmpp[").append(this.requestToSmpp);
		toStringForm.append("] responseFromSmpp[").append(this.responseFromSmpp);
		toStringForm.append("] requestFromSmpp[").append(this.requestFromSmpp);
		toStringForm.append("] responseToSmpp[").append(this.responseToSmpp);
		toStringForm.append("] additionalParameters[").append(this.additionalParameter + "]");
		toStringForm.append(
				"\n+-------------+-------------+-------------+-------------+-------------+-------------+-------------+-------------+-------------+-------------+-------------+-------------+\n");

		return toStringForm.toString();
	}

	public String logData(HttpServletRequest talendRequest, String talendResponse, String rawRequest,
			String rawResponse, Date requestTime, Date responseTime, String msisdn, String transactionId,
			String pricePoint) {

		String requestResponseTimeInterval = (responseTime.getTime() - requestTime.getTime()) + " milli seconds";
		String requestToSmpp = talendRequest.getRequestURL() + "?" + talendRequest.getQueryString() + "";

		if (pricePoint == null) {
			activityType = "";
		} else if ((this.zeroPricePoint).equals(pricePoint)) {
			activityType = sendsmsTxt;
		} else {
			activityType = billingTxt;
		}

		return LoggingBean.getLogFormat(responseTime, msisdn, this.providerId, this.customerId, transactionId,
				activityType, requestTime, responseTime, requestResponseTimeInterval, smppMethod, rawRequest,
				rawResponse, requestToSmpp, talendResponse, requestFromSmpp, responseToSmpp, "additionalParameters");
	}

	public static String logData(DeliveryNotificationTO deliveryNotificationTO, String talendRequestURL,
			String talendResponse, Date listenerStartTime, Date dnReceivedTime, Date talendRequestTime,
			Date talendResponseTime) {
		StringBuilder stringBuilderListener = new StringBuilder(
				"\n+-------------+-------------+-------------+-------------+-------------+-------------+-------------+-------------+-------------+-------------+-------------+-------------+\n");
		stringBuilderListener.append("MSISDN : [").append(deliveryNotificationTO.getMsisdn())
				.append("] Message ID : [");
		stringBuilderListener.append(deliveryNotificationTO.getMessageId()).append("]\n");
		stringBuilderListener.append("Listener Start Time : [").append(dateFormat.format(listenerStartTime));
		stringBuilderListener.append("] \n\tDelivery Notification Received : {")
				.append(deliveryNotificationTO.getResponseDNString());
		stringBuilderListener.append("} \n\tDelivery Notification Response : {")
				.append(deliveryNotificationTO.getResponseToCarrier());
		stringBuilderListener.append("} \nListener End Time : [").append(dateFormat.format(dnReceivedTime));
		stringBuilderListener.append("] \nTalend Request Start Time : [").append(dateFormat.format(talendRequestTime));
		stringBuilderListener.append("] \n\tTalend Request : {").append(talendRequestURL);
		stringBuilderListener.append("} \n\tTalend Response : {").append(talendResponse);
		stringBuilderListener.append("} \nTalend Response Received Time : [")
				.append(dateFormat.format(talendResponseTime)).append("]");
		stringBuilderListener.append(
				"\n+-------------+-------------+-------------+-------------+-------------+-------------+-------------+-------------+-------------+-------------+-------------+-------------+\n");
		return stringBuilderListener.toString();
	}
}
