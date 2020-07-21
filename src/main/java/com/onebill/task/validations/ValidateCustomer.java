package com.onebill.task.validations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.springframework.stereotype.Component;

import com.onebill.task.dto.CustomerDetails;
import com.onebill.task.dto.Emails;
import com.onebill.task.dto.MobileNumbers;

@Component
public class ValidateCustomer {

	public static Set<String> errors ;

	public boolean validateCustomer(CustomerDetails customer) {
		errors = new HashSet<String>();
		ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
		Validator validator = validatorFactory.getValidator();
		Set<ConstraintViolation<CustomerDetails>> contrains = validator.validate(customer);
		for (ConstraintViolation<CustomerDetails> constraintViolation : contrains) {
			errors.add(constraintViolation.getMessage());
		}
		if (customer.getDateOfBirth() == null || customer.getDateOfBirth().isAfter(LocalDate.now())) {
			System.out.println(customer.getDateOfBirth());
			errors.add("Date of Birth should not be null please check the date");
		}
		if (customer.getEmails().size() != 0) {
			boolean isNotValid = false;
			for (Emails email : customer.getEmails()) {
				Set<ConstraintViolation<Emails>> emailCons = validator.validate(email);
				if (emailCons.size() > 0) {
					isNotValid = true;
				}
			}
			if (isNotValid) {
				errors.add("Please Check Email Pattern");
			}
		}
		if (customer.getPhoneNumbers().size() != 0) {
			boolean isNotValid = false;
			for (MobileNumbers number : customer.getPhoneNumbers()) {
				System.out.println(number.getMobileNumber());
				if(number.getMobileNumber() != null) {
					String regExp = "(0/91)?[7-9][0-9]{9}";
					Pattern pattern = Pattern.compile(regExp);
					Matcher matcher = pattern.matcher(number.getMobileNumber().toString());
					if(matcher.matches()) {
						isNotValid = true;
					}
					if (!isNotValid) {
						errors.add("Please Enter Valid Number");
					}
				}	
			}
		}
		if (errors.size() > 0) {
			return false;
		} else {
			return true;
		}
	}
}
