package com.vuclip.validators;

import com.vuclip.api.model.UserStatusRequest;
import com.vuclip.constants.Constants;
import com.vuclip.exceptions.BadRequestException;
import com.vuclip.exceptions.LCMErrorCode;
import com.vuclip.exceptions.LCMServiceException;

public class UserStatusRequestValidator extends BaseValidator{
	
	
	public boolean isUserStatusRequestValid(UserStatusRequest userStatusRequest) throws LCMServiceException{
		
		boolean isUserStatusRequestValid = true;
		
		boolean isUserIdValid = isMandatoryStringParamValid(userStatusRequest.getUserId(),Constants.MAX_CHAR_USER_ID, "User ID");
		/*boolean isEpocTimeValid = isMandatoryLongParamValid(userStatusRequest.getEpocTime(), "EPOC Time");
		boolean isAuthKeyValid = isMandatoryStringParamValid(userStatusRequest.getAuthKey(),Constants.MAX_CHAR_AUTH_KEY, "Auth Key");
		boolean isCustomerIdValid = isMandatoryIntParamValid(userStatusRequest.getCustomerId(), "Customer ID");
		*/
		
		//!isEpocTimeValid || !isAuthKeyValid ||	!isCustomerIdValid
		if(!isUserIdValid){
			isUserStatusRequestValid = false;
			throw new LCMServiceException(LCMErrorCode.BAD_REQUEST,new BadRequestException(resultMessage.toString()));
		}
		
		return isUserStatusRequestValid;
		
	}
	
}
