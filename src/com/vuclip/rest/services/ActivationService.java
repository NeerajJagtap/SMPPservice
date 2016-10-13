package com.vuclip.rest.services;

import javax.ws.rs.BeanParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.vuclip.api.model.ActivationRequest;
import com.vuclip.api.model.ActivationResponse;
import com.vuclip.api.model.CustomerDetails;
import com.vuclip.api.model.Result;
import com.vuclip.exceptions.LCMDataMapper;
import com.vuclip.exceptions.LCMErrorCode;
import com.vuclip.exceptions.LCMServiceException;
import com.vuclip.validators.ActivationRequestValidator;


@Service
public class ActivationService {

	
	private static final Logger logger = LoggerFactory.getLogger(ActivationService.class);
	
	@GET
	@POST
	@Produces("application/json, application/xml")
	public ActivationResponse processActivationPost(@BeanParam ActivationRequest activationRequest){
		
		if(logger.isDebugEnabled()){
			logger.debug("Before LCM Process ActivationRequest  : "+activationRequest);
		}
		ActivationResponse activationResponse = null;
		ActivationRequestValidator activationRequestValidator = new ActivationRequestValidator();
		try{
			if(activationRequestValidator.isRequestValid(activationRequest)){
				//activationResponse = lcmapi.initialize(activationRequest);
				
				System.out.println("Request received : "+activationRequest);
				logger.debug("Request is valid");
				activationResponse = initialize(activationRequest);
			}

		}
		catch(LCMServiceException ex){
			
			logger.error("Message : "+ex.getMessage()+"\nStackTrace: "+ExceptionUtils.getStackTrace(ex));
			activationResponse = LCMDataMapper.prepareActivationFailureResponse(ex);
		}catch(Exception ex){
			
			logger.error("Message : "+ex.getMessage()+"\nStackTrace: "+ExceptionUtils.getStackTrace(ex));
			activationResponse = LCMDataMapper.prepareActivationFailureResponse(new LCMServiceException(LCMErrorCode.UNKNOWN_ERROR,ex));
		}	
		
		if(logger.isDebugEnabled()){
			logger.debug("After lcm Process ActivationRequest  : "+activationRequest);
		}
		return activationResponse;
	}
	
	
	
	ActivationResponse initialize(ActivationRequest activationRequest){
		ActivationResponse activationResponse = new ActivationResponse();
		Result result = new Result();
		result.setCode(LCMErrorCode.ACCEPTED.getCode());
		result.setMessage(LCMErrorCode.ACCEPTED.getMessageText());
		result.setStatus("OK");
		
		CustomerDetails customer = new CustomerDetails();
		customer.setCustomerId(16575);
		customer.setCustomerName("Devendra");
		
		activationResponse.setUserId(activationRequest.getUserId());
		activationResponse.setResult(result);
		activationResponse.setCustomerDetails(customer);
		
		return activationResponse;
	}
	
}