package com.vuclip.hash;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.vuclip.contacts.Contact;


/**
 * @author SivaLabs
 *
 */
@Component("hashDataFormFormValidator")
public class HashDataFormValidator implements Validator
{
	
	@Override
	public boolean supports(Class<?> clazz)
	{
		return Contact.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object model, Errors errors)
	{
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "customerNo","required.customerNo", "Customer msisdn is required.");
	}

}
