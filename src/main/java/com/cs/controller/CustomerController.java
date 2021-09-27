package com.cs.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cs.dto.CustomerDTO;
import com.cs.exception.DataAccessException;
import com.cs.exception.ErrorDetails;
import com.cs.exception.InternalServerException;
import com.cs.exception.InvalidRequestException;
import com.cs.exception.ResourceNotFoundException;
import com.cs.service.CustomerService;

@CrossOrigin
@RestController
@RequestMapping("/api/v1")
public class CustomerController {

	private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

	@Autowired
	private CustomerService customerService;

	@GetMapping("/customers")
	public List<CustomerDTO> getAllCustomers() throws InternalServerException {
		logger.info("Received request to fetch all Customer");
		return customerService.getAllCustomers();
	}
	
	@GetMapping("/customers/{id}")
	public ResponseEntity<Object> getCustomerById(@PathVariable(value = "id") Long customerId) {
		logger.info("Received request to fetch details for customerId : " + customerId);
		CustomerDTO customer;
		try {
			customer = customerService.getCustomerById(customerId);
			return ResponseEntity.ok().body(customer);
		} catch (ResourceNotFoundException exception) {
			logger.error("Customer not found for this id :: " + customerId);
			ErrorDetails errorDetails = new ErrorDetails(new Date(), "Customer not found for this id", "Customer Id" + customerId);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
		} catch (InternalServerException exception) {
			logger.error("Error to fetch Customert details by Id:" + customerId);
			ErrorDetails errorDetails = new ErrorDetails(new Date(), "Error to fetch Customer details", "Customer Id" + customerId);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
		}
	}

	/*@GetMapping("/customers/{id}")
	public CustomerDTO getCustomerById(@PathVariable(value = "id") Long customerId)
			throws ResourceNotFoundException, InternalServerException {
		logger.info("Received request to fetch details for customerId : " + customerId);
		CustomerDTO customer = customerService.getCustomerById(customerId);
		return customer;
	}*/

	@PostMapping("/customers")
	public CustomerDTO createCustomer(@Valid @RequestBody CustomerDTO customer)
			throws DataAccessException, InvalidRequestException {
		logger.info("Received request to create new Customer");
		
		CustomerDTO customerDTO = null;
		if ((customer.getFirstName() != null && !customer.getFirstName().isEmpty())
				&& (customer.getLastName() != null && !customer.getLastName().isEmpty())
				&& (customer.getMobileNumber() != null && !customer.getMobileNumber().isEmpty())
				&& (customer.getEmailId() != null && !customer.getEmailId().isEmpty())) {
			try {
				customerDTO = customerService.getCustomerByMobileNumber(customer.getMobileNumber());
				if (customerDTO != null) {
					logger.error("Customer mobile already exist!");
					throw new InvalidRequestException("Customer mobile already exist!");
				} else {
					customerDTO = customerService.createCustomer(customer);
				}
			} catch (InternalServerException exception) {
				logger.error("Customer details could not be saved, there seems to be an issue with the Database", exception.getMessage());
				throw new DataAccessException("Customer details could not be saved, there seems to be an issue with the Database");
			}
		} else {
			logger.error("Customer firstname, lastname, mobile, email are mandatory to create new Customer");
			throw new InvalidRequestException(
					"Customer firstname, lastname, mobile, email are mandatory to create new Customer");
		}
		return customerDTO;
	}

	@PutMapping("/customers/{id}")
	public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable(value = "id") Long customerId,
			@Valid @RequestBody CustomerDTO customerDetails) throws ResourceNotFoundException, DataAccessException, InternalServerException {
		logger.info("Received request to update existing Customer details");
		final CustomerDTO updatedCustomer = customerService.updateCustomer(customerId, customerDetails);
		return ResponseEntity.ok(updatedCustomer);
	}

	@DeleteMapping("/customers/{id}")
	public Map<String, Boolean> deleteCustomer(@PathVariable(value = "id") Long customerId)
			throws ResourceNotFoundException, InternalServerException, DataAccessException {
		logger.info("Received request to delete existing Customer details");
		return customerService.deleteCustomer(customerId);
	}
}
