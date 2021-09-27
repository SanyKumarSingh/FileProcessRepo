package com.cs;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.cs.dto.CustomerDTO;

/*
 * Main Program to Test the Feature
 * 
 */
public class SpringRestClient {

	private static final String GET_PRODUCTS_ENDPOINT_URL = "http://localhost:8080/api/v1/products";
	
	private static final String GET_CUSTOMERS_ENDPOINT_URL = "http://localhost:8080/api/v1/customers";
	private static final String GET_CUSTOMER_ENDPOINT_URL = "http://localhost:8080/api/v1/customers/{id}";
	private static final String CREATE_CUSTOMER_ENDPOINT_URL = "http://localhost:8080/api/v1/customers";
	private static final String UPDATE_CUSTOMER_ENDPOINT_URL = "http://localhost:8080/api/v1/customers/{id}";
	private static final String DELETE_CUSTOMER_ENDPOINT_URL = "http://localhost:8080/api/v1/customers/{id}";
	
	
	
	private static RestTemplate restTemplate = new RestTemplate();

	public static void main(String[] args) {
		SpringRestClient springRestClient = new SpringRestClient();
		
		// Step1: get all product
		springRestClient.getProducts();
		
		// Step1: first create a new customer
		springRestClient.createCustomer();
		
		// Step 2: get new created customer from step1
		springRestClient.getCustomerById();
		
		// Step3: get all customer
		springRestClient.getCustomers();
		
		// Step4: Update customer with id = 1
		springRestClient.updateCustomer();
		
		// Step5: Delete customer with id = 1
		springRestClient.deleteCustomer();
		
	}

	private void getCustomers() {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
		ResponseEntity<String> result = restTemplate.exchange(GET_CUSTOMERS_ENDPOINT_URL, HttpMethod.GET, entity,
				String.class);
		System.out.println(result);
	}

	private void getCustomerById() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", "1");
		RestTemplate restTemplate = new RestTemplate();
		CustomerDTO result = restTemplate.getForObject(GET_CUSTOMER_ENDPOINT_URL, CustomerDTO.class, params);
		System.out.println(result);
	}

	private void createCustomer() {
		CustomerDTO newCustomer = new CustomerDTO("admin", "admin", "8336970846", "admin@gmail.com");
		RestTemplate restTemplate = new RestTemplate();
		CustomerDTO result = restTemplate.postForObject(CREATE_CUSTOMER_ENDPOINT_URL, newCustomer, CustomerDTO.class);
		System.out.println(result);
	}

	private void updateCustomer() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", "1");
		CustomerDTO updatedCustomer = new CustomerDTO("admin123", "admin123", "8336970846", "admin123@gmail.com");
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.put(UPDATE_CUSTOMER_ENDPOINT_URL, updatedCustomer, params);
	}

	private void deleteCustomer() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", "1");
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.delete(DELETE_CUSTOMER_ENDPOINT_URL, params);
	}
	
	private void getProducts() {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
		ResponseEntity<String> result = restTemplate.exchange(GET_PRODUCTS_ENDPOINT_URL, HttpMethod.GET, entity,
				String.class);
		System.out.println(result);
	}

}
