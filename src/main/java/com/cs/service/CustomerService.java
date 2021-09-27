package com.cs.service;

import java.util.List;
import java.util.Map;

import com.cs.dto.CustomerDTO;
import com.cs.exception.DataAccessException;
import com.cs.exception.InternalServerException;
import com.cs.exception.ResourceNotFoundException;

public interface CustomerService {

	public List<CustomerDTO> getAllCustomers() throws InternalServerException;

	public CustomerDTO getCustomerById(Long customerId) throws ResourceNotFoundException, InternalServerException;
	
	public CustomerDTO getCustomerByMobileNumber(String mobileNumber) throws InternalServerException;

	public CustomerDTO createCustomer(CustomerDTO customer) throws DataAccessException;

	public CustomerDTO updateCustomer(Long customerId, CustomerDTO customerDetails)
			throws ResourceNotFoundException, DataAccessException, InternalServerException;

	public Map<String, Boolean> deleteCustomer(Long customerId)
			throws ResourceNotFoundException, InternalServerException, DataAccessException;

}
