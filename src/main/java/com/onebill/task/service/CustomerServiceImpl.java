package com.onebill.task.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onebill.task.dao.CustomerDAO;
import com.onebill.task.dto.CustomerDetails;
import com.onebill.task.dto.CustomerDocuments;
import com.onebill.task.dto.Response;
import com.onebill.task.validations.ValidateCustomer;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private CustomerDAO dao;

	@Autowired
	private ValidateCustomer validate;

	@Override
	public boolean createCustomer(CustomerDetails details, CustomerDocuments documents) {
		if (validate.validateCustomer(details)) {
			return dao.createCustomer(details, documents);
		} else {
			return false;
		}
	}

	@Override
	public CustomerDetails updateCustomer(CustomerDetails details, CustomerDocuments documents) {
		if (validate.validateCustomer(details)) {
			return dao.updateCustomer(details, documents);
		} else {
			return null;
		}
	}

	@Override
	public List<CustomerDetails> getCustomers() {
		return dao.getCustomers();
	}

	@Override
	public boolean deleteCustomer(int customerId) {
		return dao.deleteCustomer(customerId);
	}

	@Override
	public CustomerDetails getCustomer(String email) {
		return dao.getCustomer(email);
	}
}
