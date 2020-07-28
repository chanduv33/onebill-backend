package com.onebill.task.controllers;

import java.io.IOException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onebill.task.dto.CustomerDetails;
import com.onebill.task.dto.CustomerDocuments;
import com.onebill.task.dto.Response;
import com.onebill.task.exceptions.EmailAlreadyExistsException;
import com.onebill.task.exceptions.MobileNumberAlreadyExistsException;
import com.onebill.task.service.CustomerService;
import com.onebill.task.validations.ValidateCustomer;

@Validated
@RestController
@CrossOrigin(origins = "*", allowCredentials = "true", allowedHeaders = "*", exposedHeaders = "Access-Control-Allow-Origin")
public class CustomerController {

	@Autowired
	private CustomerService service;

	@PostMapping(path = "/createCustomer", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response> createCustomer(@RequestParam("files") MultipartFile[] multipartFile,
		@RequestParam("customer") String customer, @RequestParam("fileNames") String[] fileNames)
			throws IOException {
		 CustomerDetails details = new ObjectMapper().readValue(customer, CustomerDetails.class);
		 
		CustomerDocuments documents = new CustomerDocuments();
		if (multipartFile.length == 1) {
			List<String> names = Arrays.asList(fileNames);
			System.out.println(names.get(0));
			if (names.get(0).contentEquals("addressProof")) {
				System.out.println(names.get(0));
				documents.setAddressProof(multipartFile[0].getBytes());
				documents.setAddressProofFileName(multipartFile[0].getOriginalFilename());
			} else if (names.get(0).contentEquals("idProof")) {
				System.out.println(names.get(0));
				documents.setIdentityProof(multipartFile[0].getBytes());
				documents.setIdentityProofFileName(multipartFile[0].getOriginalFilename());
			}
		} else {
			for (int i = 0; i < multipartFile.length; i++) {
				if (i == 0) {
					documents.setAddressProof(multipartFile[i].getBytes());
					documents.setAddressProofFileName(multipartFile[i].getOriginalFilename());
				}
				if (i == 1) {
					documents.setIdentityProof(multipartFile[i].getBytes());
					documents.setIdentityProofFileName(multipartFile[i].getOriginalFilename());
				}
			}
		}
		
		try {
			if (service.createCustomer(details, documents)) {
				return new ResponseEntity<Response>(new Response("Customer Details Saved Successfully"), HttpStatus.OK);
			} else if(ValidateCustomer.errors.size() > 0) {
				Response response = new Response();
				response.setErrors(ValidateCustomer.errors);
				response.setMessage("Failed to Save Customer Details");
				return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
			}
		} catch (EmailAlreadyExistsException exp) {
			return new ResponseEntity<Response>(new Response(exp.getMessage()), HttpStatus.BAD_REQUEST);
		} catch (MobileNumberAlreadyExistsException exp) {
			return new ResponseEntity<Response>(new Response(exp.getMessage()), HttpStatus.BAD_REQUEST);
		} catch (Exception exp) {
			return new ResponseEntity<Response>(new Response(exp.getMessage()), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<Response>(new Response("Failed to Save Customer Details"), HttpStatus.BAD_REQUEST);
	}

	@PutMapping(path = "/updateCustomer", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response> updateCustomer(@RequestParam("files") MultipartFile[] multipartFile,
			@RequestParam("customer") String customer, @RequestParam("fileNames") String[] fileNames)
			throws IOException {
		CustomerDetails details = new ObjectMapper().readValue(customer, CustomerDetails.class);
		CustomerDocuments documents = new CustomerDocuments();
		if (multipartFile.length == 1 ) {			
			List<String> names = Arrays.asList(fileNames);
			if (names.get(0).contentEquals("addressProof")) {
				documents.setAddressProof(multipartFile[0].getBytes());
				documents.setAddressProofFileName(multipartFile[0].getOriginalFilename());
			} else if ( names.get(0).contentEquals("idProof")) {
				documents.setIdentityProof(multipartFile[0].getBytes());
				documents.setIdentityProofFileName(multipartFile[0].getOriginalFilename());
			}
		} else {
			for (int i = 0; i < multipartFile.length; i++) {
				if (i == 0) {
					documents.setAddressProof(multipartFile[i].getBytes());
					documents.setAddressProofFileName(multipartFile[i].getOriginalFilename());
				}
				if (i == 1) {
					documents.setIdentityProof(multipartFile[i].getBytes());
					documents.setIdentityProofFileName(multipartFile[i].getOriginalFilename());
				}
			}
		}
		if (service.updateCustomer(details, documents) != null) {
			return new ResponseEntity<Response>(new Response("Customer Details Updated Successfully"), HttpStatus.OK);
		} else if(ValidateCustomer.errors.size() > 0) {
			Response response = new Response();
			response.setErrors(ValidateCustomer.errors);
			response.setMessage("Failed to Save Customer Details");
			return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
		} else {
			return new ResponseEntity<Response>(new Response("Customer Details Not Updated"), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping(path = "/getCustomers", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response> getCustomers() {
		List<CustomerDetails> customers = service.getCustomers();
		Response response = new Response();
		if (customers.size() != 0) {
			response.setMessage("Customers Details Loaded Successfully");
			response.setCustomers(customers);
			return new ResponseEntity<Response>(response, HttpStatus.OK);
		} else {
			response.setMessage("No Customers to Load");
			return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
		}
	}

	@DeleteMapping(path = "/deleteCustomer/{customerId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response> deleteCustomer(@PathVariable int customerId) {
		Response response = new Response();
		if (service.deleteCustomer(customerId)) {
			response.setMessage("Customers Deleted Successfully");
			return new ResponseEntity<Response>(response, HttpStatus.OK);
		} else {
			response.setMessage("Customers Not Found");
			return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
		}
	}

}
