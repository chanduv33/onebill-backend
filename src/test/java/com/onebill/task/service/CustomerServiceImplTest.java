package com.onebill.task.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import com.onebill.task.dao.CustomerDAO;
import com.onebill.task.dto.Addresses;
import com.onebill.task.dto.CustomerDetails;
import com.onebill.task.dto.CustomerDocuments;
import com.onebill.task.dto.Emails;
import com.onebill.task.dto.MobileNumbers;

@ContextConfiguration(classes = {SpringBootTest.class})
@WebMvcTest
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class CustomerServiceImplTest {
	
	@InjectMocks
	CustomerService service = new CustomerServiceImpl();
	
	@Mock
	CustomerDAO dao;
	
	private static CustomerDetails customer;
	
	
	@BeforeAll
	static void  sampleCustomer() {
		customer = new CustomerDetails();
		customer.setCustomerId(1);
		customer.setFirstName("John");
		customer.setLastName("Whick");
		LocalDate dateOfBirth = LocalDate.parse("1983-02-11");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); 
		dateOfBirth = LocalDate.parse(formatter.format(dateOfBirth));
        customer.setDateOfBirth(dateOfBirth);
		List<Addresses> addresses = new ArrayList<Addresses>();
		Addresses addr1 = new Addresses();
		addr1.setCountry("Inida");
		addr1.setCity("Kurnool");
		addr1.setLine1("Kummari Street");
		addr1.setLine2("One Town");
		addr1.setPincode(132323);
		addr1.setState("Andhra Pradesh");
		addr1.setCustomer(customer);
		addresses.add(addr1);
		List<MobileNumbers> phoneNumbers = new ArrayList<MobileNumbers>();
		MobileNumbers number = new MobileNumbers();
		number.setMobileNumber(8388263760l);
		number.setCustomer(customer);
		phoneNumbers.add(number);
		List<Emails> emails = new ArrayList<Emails>();
		Emails email = new Emails();
		email.setEmail("john@gmail.com");
		email.setCustomer(customer);
		emails.add(email);
		customer.setAddresses(addresses);
		customer.setEmails(emails);
		customer.setPhoneNumbers(phoneNumbers);	
	}
	

	@Test
	void testCreateCustomer() {
		when(dao.createCustomer(any(CustomerDetails.class), any(CustomerDocuments.class))).thenReturn(true);
		boolean actual=service.createCustomer(customer, new CustomerDocuments());
		assertEquals(true, actual);
	}
	
	
	@Test
	void negativeTestCreateCustomer() {
		when(dao.createCustomer(any(CustomerDetails.class), any(CustomerDocuments.class))).thenReturn(false);
		boolean actual=service.createCustomer(customer, new CustomerDocuments());
		assertEquals(false, actual);
	}
	
	@Test
	void testgetCustomers() {
		List<CustomerDetails> customers = new ArrayList<CustomerDetails>();
		customers.add(customer);
		when(dao.getCustomers()).thenReturn(customers);
		List<CustomerDetails> customersDetails = service.getCustomers();
		assertEquals(customers, customersDetails);
	}
	
	@Test
	void negativeTestgetCustomers() {
		List<CustomerDetails> customers = new ArrayList<CustomerDetails>();
		customers.add(customer);
		when(dao.getCustomers()).thenReturn(null);
		List<CustomerDetails> customersDetails = service.getCustomers();
		assertNotEquals(customers, customersDetails);
	}
	
	@Test
	void testGetCustomer() {
		when(dao.getCustomer("john@gmail.com")).thenReturn(customer);
		CustomerDetails cust = service.getCustomer("john@gmail.com");
		assertNotNull(cust);
	}
	
	@Test
	void testUpdateCustomer() {
		CustomerDetails cust = new CustomerDetails();
		cust.setCustomerId(1);
		cust.setFirstName("Jonas");
		cust.setLastName("Tiedeman");
		LocalDate dateOfBirth = LocalDate.parse("1993-02-11");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); 
		dateOfBirth = LocalDate.parse(formatter.format(dateOfBirth));
		cust.setDateOfBirth(dateOfBirth);
		when(dao.updateCustomer(cust, cust.getDocuments())).thenReturn(cust);
		CustomerDetails actual = service.updateCustomer(cust, cust.getDocuments());
		assertEquals(cust.getFirstName(), actual.getFirstName());
	}
	
	@Test
	void testDeleteCustomer() {
		when(dao.deleteCustomer(customer.getCustomerId())).thenReturn(true);
		boolean actual = service.deleteCustomer(customer.getCustomerId());
		assertEquals(true, actual);
	}
	
	void negativeTestDeleteCustomer() {
		when(dao.deleteCustomer(customer.getCustomerId())).thenReturn(false);
		boolean actual = service.deleteCustomer(customer.getCustomerId());
		assertEquals(false, actual);
	}

}
