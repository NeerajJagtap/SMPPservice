/**
 * 
 */
package com.vuclip.exceptions;

import com.vuclip.api.model.ActivationRequest;
import com.vuclip.api.model.ActivationResponse;
import com.vuclip.api.model.Result;
import com.vuclip.api.model.UserContext;

/**
 * @author
 *
 */
public class LCMDataMapper {

	/**
	 * @param userContext
	 * @param e
	 * @return
	 */
	static protected ActivationResponse prepareActivationExceptionResponse(UserContext userContext, Exception e) {
		ActivationResponse activationResponse = new ActivationResponse();
		Result activationRequestResult = new Result();
		activationRequestResult.setCode(LCMErrorCode.UNKNOWN_ERROR.toString());
		activationRequestResult.setStatus(LCMErrorCode.UNKNOWN_ERROR.getMessageText());
		activationRequestResult.setMessage(e.getMessage());
		activationResponse.setResult(activationRequestResult);
		return activationResponse;
	}

	/*
	 * static protected DeActivationResponse
	 * prepareDeActivationExceptionResponse( UserContext userContext, Exception
	 * e) { DeActivationResponse unSubscriptionResponse = new
	 * DeActivationResponse(); Result unSubscriptionResult = new Result();
	 * unSubscriptionResult.setCode(LCMErrorCode.UNKNOWN_ERROR.toString());
	 * unSubscriptionResult.setStatus(LCMErrorCode.UNKNOWN_ERROR.getMessageText(
	 * )); unSubscriptionResult.setMessage(e.getMessage());
	 * 
	 * unSubscriptionResponse.setResult(unSubscriptionResult); return
	 * unSubscriptionResponse; }
	 */

	static public ActivationResponse prepareActivationFailureResponse(LCMServiceException e) {

		ActivationResponse activationResponse = new ActivationResponse();
		Result activationRequestResult = new Result();
		activationRequestResult.setCode(e.getLcmErrorCode().getCode());
		activationRequestResult.setStatus("FAIL");
		activationRequestResult.setMessage(e.getLcmErrorCode().getMessageText() + " " + e.getMessage());// +
																										// "
																										// "
																										// +e.getMessage());
		activationResponse.setResult(activationRequestResult);
		return activationResponse;
	}

	/**
	 * @param userContext
	 * @param e
	 * @return
	 */

	/*
	 * static protected ActivityResponse prepareActivityExceptionResponse(
	 * UserContext userContext, Exception e) { ActivityResponse activityResponse
	 * = new ActivityResponse();
	 * 
	 * activityResponse.setResponseCode("VE999");
	 * activityResponse.setResponseMessage("Unknown Error");
	 * 
	 * return activityResponse;
	 * 
	 * }
	 * 
	 * 
	 * static protected ActivityResponse prepareActivityExceptionResponse(
	 * UserContext userContext, BlackListServiceException e) { ActivityResponse
	 * activityResponse = new ActivityResponse();
	 * 
	 * activityResponse.setResponseCode(e.getErrorCode().getCode());
	 * activityResponse.setResponseMessage(e.getErrorCode().getMessageText() +
	 * " "+e.getMessage());
	 * 
	 * return activityResponse;
	 * 
	 * }
	 * 
	 * 
	 * static public ActivationResponse prepareActivationFailureResponse(
	 * UserContext userContext, ActivationRequest
	 * activationRequest,LCMServiceException e) {
	 * 
	 * ActivationResponse activationResponse = new ActivationResponse();
	 * activationResponse.setUserId(activationRequest.getUserId());
	 * activationResponse.setTransactionId(activationRequest.getTransactionId())
	 * ;
	 * 
	 * Result activationRequestResult = new Result();
	 * activationRequestResult.setCode(e.getLcmErrorCode().getCode());
	 * activationRequestResult.setStatus("FAIL");
	 * activationRequestResult.setMessage(e.getLcmErrorCode().getMessageText()+
	 * " "+ e.getMessage());//+ " " +e.getMessage());
	 * activationResponse.setResult(activationRequestResult); return
	 * activationResponse; } static public ActivationResponse
	 * prepareActivationFailureResponse( UserContext userContext,
	 * ActivationRequest activationRequest,BlackListServiceException e) {
	 * 
	 * ActivationResponse activationResponse = new ActivationResponse();
	 * activationResponse.setUserId(activationRequest.getUserId());
	 * activationResponse.setTransactionId(activationRequest.getTransactionId())
	 * ;
	 * 
	 * Result activationRequestResult = new Result();
	 * activationRequestResult.setCode(e.getErrorCode().getCode());
	 * activationRequestResult.setStatus("FAIL");
	 * activationRequestResult.setMessage(e.getErrorCode().getMessageText()+ " "
	 * + e.getMessage());//+ " " +e.getMessage());
	 * activationResponse.setResult(activationRequestResult); return
	 * activationResponse; }
	 * 
	 * static public DeActivationResponse prepareDeActivationFailureResponse(
	 * UserContext userContext, ActivityRequest
	 * activityRequest,LCMServiceException e) {
	 * 
	 * DeActivationResponse unSubscriptionResponse = new DeActivationResponse();
	 * unSubscriptionResponse.setUserId(activityRequest.getMsisdn());
	 * unSubscriptionResponse.setTransactionId(activityRequest.getTransactionId(
	 * ));
	 * 
	 * Result activationRequestResult = new Result();
	 * activationRequestResult.setCode(e.getLcmErrorCode().getCode());
	 * activationRequestResult.setStatus("FAIL");
	 * activationRequestResult.setMessage(e.getLcmErrorCode().getMessageText()+
	 * " "+ e.getMessage());//+ " " +e.getMessage());
	 * unSubscriptionResponse.setResult(activationRequestResult); return
	 * unSubscriptionResponse; }
	 */

	/**
	 * @param userContext
	 * @param e
	 * @return
	 */
	/*
	 * static protected ActivityResponse prepareActivityFailureResponse(
	 * UserContext userContext, LCMServiceException e) {
	 * 
	 * ActivityResponse activityResponse = new ActivityResponse();
	 * 
	 * activityResponse.setResponseCode(e.getLcmErrorCode().getCode());
	 * activityResponse.setResponseMessage(e.getLcmErrorCode().getMessageText()+
	 * " "+ e.getMessage());
	 * 
	 * return activityResponse; } static protected ActivityResponse
	 * prepareActivityFailureResponse( UserContext userContext,
	 * BlackListServiceException e) {
	 * 
	 * ActivityResponse activityResponse = new ActivityResponse();
	 * 
	 * activityResponse.setResponseCode(e.getErrorCode().getCode());
	 * activityResponse.setResponseMessage(e.getErrorCode().getMessageText()+
	 * " "+ e.getMessage());
	 * 
	 * return activityResponse; }
	 */

	/**
	 * @param userContext
	 * @return
	 */
	static protected ActivationResponse prepareActivationResponse(UserContext userContext,
			ActivationRequest activationRequest) {

		ActivationResponse activationResponse = new ActivationResponse();
		Result activationRequestResult = new Result();
		activationResponse.setUserId(activationRequest.getUserId());
		activationRequestResult.setCode(LCMErrorCode.CONSENT_REDIRECT.getCode());
		activationRequestResult.setStatus("OK");
		activationRequestResult.setMessage(LCMErrorCode.CONSENT_REDIRECT.getMessageText());
		activationResponse.setResult(activationRequestResult);
		// activationResponse.setCgURL("http://www.vuclip.com");
		// if(activationRequest!=null)
		// activationResponse.setUserStatusList(getUserStatus(activationRequest));

		return activationResponse;
	}
	/*
	 * static protected DeActivationResponse
	 * prepareDeActivationResponse(UserContext userContext,ActivityRequest
	 * activityRequest) {
	 * 
	 * DeActivationResponse activityResponse = new DeActivationResponse();
	 * Result unSubscriptionRequestResult = new Result();
	 * activityResponse.setTransactionId(activityRequest.getTransactionId());
	 * activityResponse.setUserId(activityRequest.getMsisdn()); //
	 * activationResponse.setAmount(userContext.getBillingPackage().getPrice().
	 * toString()); unSubscriptionRequestResult.setCode("VE000");
	 * unSubscriptionRequestResult.setStatus("OK");
	 * unSubscriptionRequestResult.setMessage("Success");
	 * activityResponse.setResult(unSubscriptionRequestResult);
	 * activityResponse.setUserStatus(Populator.
	 * getUserStatusFromUserSubscription(userContext.getFinalUserSubscription())
	 * ); // activationResponse.setCgURL("http://www.vuclip.com"); //
	 * if(activationRequest!=null) //
	 * activationResponse.setUserStatusList(getUserStatus(activationRequest));
	 * 
	 * return activityResponse; }
	 * 
	 * 
	 * static protected ActivityResponse prepareActivityResponse(UserContext
	 * userContext,ActivityRequest activityRequest) {
	 * 
	 * ActivityResponse activityResponse = new ActivityResponse();
	 * activityResponse.setResponseCode(LCMErrorCode.SUCCESS.getCode());
	 * activityResponse.setResponseMessage(LCMErrorCode.SUCCESS.getMessageText()
	 * ); return activityResponse; }
	 * 
	 * static public DeActivationResponse prepareDeActivationFailureResponse(
	 * LCMServiceException e) {
	 * 
	 * DeActivationResponse unSubscriptionResponse = new DeActivationResponse();
	 * Result activationRequestResult = new Result();
	 * activationRequestResult.setCode(e.getLcmErrorCode().getCode());
	 * activationRequestResult.setStatus("FAIL");
	 * activationRequestResult.setMessage(e.getLcmErrorCode().getMessageText()+
	 * " "+ e.getMessage());//+ " " +e.getMessage());
	 * unSubscriptionResponse.setResult(activationRequestResult); return
	 * unSubscriptionResponse; }
	 * 
	 * static public UserStatusResponse prepareUserStatusFailureResponse(
	 * LCMServiceException e) {
	 * 
	 * UserStatusResponse userStatusResponse = new UserStatusResponse(); Result
	 * activationRequestResult = new Result();
	 * activationRequestResult.setCode(e.getLcmErrorCode().getCode());
	 * activationRequestResult.setStatus("FAIL");
	 * activationRequestResult.setMessage(e.getLcmErrorCode().getMessageText()+
	 * " "+ e.getMessage());//+ " " +e.getMessage());
	 * userStatusResponse.setResult(activationRequestResult); return
	 * userStatusResponse; }
	 */
}
