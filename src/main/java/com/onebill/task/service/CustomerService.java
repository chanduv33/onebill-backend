package com.onebill.task.service;

import java.util.List;


import com.onebill.task.dto.CustomerDetails;
import com.onebill.task.dto.CustomerDocuments;

public interface CustomerService {
	public boolean createCustomer(CustomerDetails details,  CustomerDocuments documents);
	public CustomerDetails updateCustomer(CustomerDetails details, CustomerDocuments documents);
	public List<CustomerDetails> getCustomers();
	public boolean deleteCustomer(int customerId);
	public CustomerDetails getCustomer(String email);
}
