package com.cs.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.controller.CustomerController;
import com.cs.dto.CustomerDTO;
import com.cs.exception.DataAccessException;
import com.cs.exception.InternalServerException;
import com.cs.exception.ResourceNotFoundException;
import com.cs.model.Customer;
import com.cs.repository.CustomerRepository;
import com.cs.service.CustomerService;

@Service
public class CustomerServiceImpl implements CustomerService {

	private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

	@Autowired
	private CustomerRepository customerRepository;

	@Override
	public List<CustomerDTO> getAllCustomers() throws InternalServerException {
		logger.info("Processing request to fetch all Customer");
		List<Customer> customers = null;
		try {
			// Fetch all the registered customers
			customers = customerRepository.findAll();
		} catch (Exception exception) {
			logger.error("Exception while fetching All Customers details.", exception.getMessage());
			throw new InternalServerException("Customer details could not be fetched.");
		}
		List<CustomerDTO> customerDTOs = new ArrayList<CustomerDTO>();
		// Iterating the Customers fetched from DB
		customers.forEach((customer) -> {
			CustomerDTO customerDTO = new CustomerDTO();
			customerDTO.setCustomerId(customer.getCustomerId());
			customerDTO.setFirstName(customer.getFirstName());
			customerDTO.setLastName(customer.getLastName());
			customerDTO.setMobileNumber(customer.getMobileNumber());
			customerDTO.setEmailId(customer.getEmailId());
			customerDTO.setRewardPoints(customer.getRewardPoints());
			customerDTOs.add(customerDTO);
		});
		return customerDTOs;
	}

	@Override
	public CustomerDTO getCustomerById(Long customerId) throws ResourceNotFoundException, InternalServerException {
		logger.info("Processing request to fetch details for customerId : " + customerId);

		// findById() of CrudRepository returns Optional instance so alternatively
		// orElseThrow() method of Optional class could be used to throw exception.
		Optional<Customer> customer = null;
		try {
			customer = customerRepository.findById(customerId);
		} catch (Exception exception) {
			logger.error("Exception while fetching Customer details by customerId " + customerId,
					exception.getMessage());
			throw new InternalServerException("Customer details could not be fetched.");
		}

		CustomerDTO customerDTO = null;
		if (customer.isPresent()) {
			customerDTO = new CustomerDTO();
			customerDTO.setCustomerId(customer.get().getCustomerId());
			customerDTO.setFirstName(customer.get().getFirstName());
			customerDTO.setLastName(customer.get().getLastName());
			customerDTO.setMobileNumber(customer.get().getMobileNumber());
			customerDTO.setEmailId(customer.get().getEmailId());
			customerDTO.setRewardPoints(customer.get().getRewardPoints());
		} else {
			logger.info("Customer not found for this id :: " + customerId);
			throw new ResourceNotFoundException("Customer not found for this id :: " + customerId);
		}

		return customerDTO;
	}

	@Override
	public CustomerDTO createCustomer(CustomerDTO customerDTO) throws DataAccessException {
		logger.info("Processing request to create new Customer");
		Customer customer = new Customer();
		customer.setFirstName(customerDTO.getFirstName());
		customer.setLastName(customerDTO.getLastName());
		customer.setMobileNumber(customerDTO.getMobileNumber());
		customer.setEmailId(customerDTO.getEmailId());

		// Save customer details and store the managed entity to fetch the customerId
		// and RewardPoints
		Customer updatedCustomer = null;
		try {
			updatedCustomer = customerRepository.save(customer);
		} catch (Exception exception) {
			logger.error("Exception while saving new customer details.", exception.getMessage());
			throw new DataAccessException("Customer details could not be saved.");
		}

		customerDTO.setCustomerId(updatedCustomer.getCustomerId());
		customerDTO.setRewardPoints(updatedCustomer.getRewardPoints());
		return customerDTO;
	}

	@Override
	public CustomerDTO updateCustomer(Long customerId, CustomerDTO customerDTO)
			throws ResourceNotFoundException, DataAccessException {
		logger.info("Processing request to update existing Customer details");

		Customer customer = customerRepository.findById(customerId).orElseThrow(() -> {
			logger.info("Customer not found for this id :: " + customerId);
			return new ResourceNotFoundException("Customer not found for this id :: " + customerId);
		});

		// Update the applicable customer details
		if (customerDTO.getEmailId() != null && !customerDTO.getEmailId().isEmpty()) {
			customer.setEmailId(customerDTO.getEmailId());
		}
		if (customerDTO.getLastName() != null && !customerDTO.getLastName().isEmpty()) {
			customer.setLastName(customerDTO.getLastName());
		}
		if (customerDTO.getFirstName() != null && !customerDTO.getFirstName().isEmpty()) {
			customer.setFirstName(customerDTO.getFirstName());
		}
		if (customerDTO.getMobileNumber() != null && !customerDTO.getMobileNumber().isEmpty()) {
			customer.setMobileNumber(customerDTO.getMobileNumber());
		}

		try {
			customerRepository.save(customer);
		} catch (Exception exception) {
			logger.error("Exception while updating customer details by customerId " + customerId,
					exception.getMessage());
			throw new DataAccessException("Customer details could not be updated.");
		}
		customerDTO.setCustomerId(customer.getCustomerId());
		customerDTO.setRewardPoints(customer.getRewardPoints());
		customerDTO.setFirstName(customer.getFirstName());
		customerDTO.setLastName(customer.getLastName());
		customerDTO.setMobileNumber(customer.getMobileNumber());
		customerDTO.setEmailId(customer.getEmailId());
		return customerDTO;
	}

	@Override
	public Map<String, Boolean> deleteCustomer(Long customerId)
			throws ResourceNotFoundException, DataAccessException {
		logger.info("Processing request to delete existing Customer details");
		
		Customer customer = customerRepository.findById(customerId).orElseThrow(() -> {
			logger.info("Customer not found for this id :: " + customerId);
			return new ResourceNotFoundException("Customer not found for this id :: " + customerId);
		});

		try {
			customerRepository.delete(customer);
		} catch (Exception exception) {
			logger.error("Exception while deleting customer details by customerId " + customerId,
					exception.getMessage());
			throw new DataAccessException("Customer details could not be deleted at the moment.");
		}
		Map<String, Boolean> response = new HashMap<>();
		response.put("Customer Deleted", Boolean.TRUE);
		return response;
	}

	@Override
	public CustomerDTO getCustomerByMobileNumber(String mobileNumber) throws InternalServerException {
		logger.info("Processing request to fetch Customer details for mobileNumber : " + mobileNumber);
		
		CustomerDTO customerDTO = null;
		Customer customer = null;
		try {
			customer = customerRepository.findByMobileNumber(mobileNumber);
			if (customer != null) {
				customerDTO = new CustomerDTO();
				customerDTO.setCustomerId(customer.getCustomerId());
				customerDTO.setFirstName(customer.getFirstName());
				customerDTO.setLastName(customer.getLastName());
				customerDTO.setMobileNumber(customer.getMobileNumber());
				customerDTO.setEmailId(customer.getEmailId());
				customerDTO.setRewardPoints(customer.getRewardPoints());
			}
		} catch(Exception exception) {
			logger.error("Exception while fetching Customer details by mobileNumber " + mobileNumber,
					exception.getMessage());
			throw new InternalServerException("Customer details could not be fetched for mobileNumber " + mobileNumber);
		}
		return customerDTO;
	}
}
