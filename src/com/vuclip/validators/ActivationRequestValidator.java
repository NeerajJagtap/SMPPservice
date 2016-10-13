package com.vuclip.validators;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vuclip.api.model.ActivationRequest;
import com.vuclip.constants.Constants;
import com.vuclip.exceptions.BadRequestException;
import com.vuclip.exceptions.LCMErrorCode;
import com.vuclip.exceptions.LCMServiceException;

public class ActivationRequestValidator extends BaseValidator{
	
	private static final Logger logger = LoggerFactory.getLogger(ActivationRequestValidator.class);

	public boolean isRequestValid(ActivationRequest activationRequest) throws LCMServiceException{
		
		boolean isUserStatusRequestValid = true;
		boolean isUserIdValid = isMandatoryStringParamValid(activationRequest.getUserId(),Constants.MAX_CHAR_USER_ID, "User ID");
		
	/*	
		boolean isItemTypeIdNumber = true;
		boolean isCallBackURLValid = true;
		boolean isAdNetworkTransactionIdValid = true;
		boolean isAdNetworkIDValid = true;
		boolean isCGURLValid = true;
		*/
		
		if(!isUserIdValid){
			
			isUserStatusRequestValid = false;
			if(logger.isDebugEnabled()){
				logger.debug("ActivationRequest Validator message : "+resultMessage);
			}
			throw new LCMServiceException(LCMErrorCode.BAD_REQUEST,new BadRequestException(resultMessage.toString()));
		}
		if(logger.isDebugEnabled()){
			logger.debug("ActivationRequest validator Result   : "+isUserStatusRequestValid);
		}
		return isUserStatusRequestValid;
		
	}
	

}
