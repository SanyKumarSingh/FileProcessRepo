package com.cs.test;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.cs.Application;
import com.cs.dto.BillRequestDTO;
import com.cs.dto.BillResponseDTO;
import com.cs.dto.CustomerDTO;
import com.cs.dto.ProductDTO;

/*
 * To Test the application Restful API's
 * 
 * Uses WebEnvironment.RANDOM_PORT (default is WebEnvironment.MOCK) to avoid conflict with other running applications,
 * especially for Continuous Integration (CI) environments where multiple builds run in parallel.
 * 
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApplicationControllerIntegrationTest {
	
	//TestRestTemplate bean will be registered automatically after @SpringBootTest is started
	@Autowired
	private TestRestTemplate restTemplate;

	@LocalServerPort
	private int port;

	private String getRootUrl() {
		return "http://localhost:" + port;
	}

	@Test
	public void contextLoads() {

	}
	
	@Test
	public void testGetAllProducts() {
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<ProductDTO> response = restTemplate.exchange(getRootUrl() + "/products", HttpMethod.GET, entity,
				ProductDTO.class);
		assertNotNull(response);
	}
	
	@Test
	public void testGetProductById() {
		ProductDTO product = restTemplate.getForObject(getRootUrl() + "/products/1", ProductDTO.class);
		assertNotNull(product);
	}
	
	@Test
	public void testAddProducts() {
		List<ProductDTO> productDTOs = new ArrayList<ProductDTO>();
		ProductDTO product = new ProductDTO();
		product.setProductName("Avacado");
		product.setPrice(5.0);
		product.setPersentDiscount(2.0);
		ResponseEntity<ProductDTO> postResponse = restTemplate.postForEntity(getRootUrl() + "/products", productDTOs,
				ProductDTO.class);
		assertNotNull(postResponse);
	}
	
	@Test
	public void testGetBillById() {
		BillResponseDTO bill = restTemplate.getForObject(getRootUrl() + "/bills/1", BillResponseDTO.class);
		assertNotNull(bill);
	}
	
	@Test
	public void testGenerateBill() {
		List<BillRequestDTO> productDTOs = new ArrayList<BillRequestDTO>();
		BillRequestDTO billRequest = new BillRequestDTO();
		billRequest.setCustomerMobileNumber("8336970846");
		billRequest.setGenerateBill(true);
		Map<Long,Integer> productQtyMap = new HashMap<Long,Integer>();
		productQtyMap.put(101L, 2);
		billRequest.setProductQtyMap(productQtyMap);
		productDTOs.add(billRequest);
		ResponseEntity<ProductDTO> postResponse = restTemplate.postForEntity(getRootUrl() + "/generateBill", productDTOs,
				ProductDTO.class);
		assertNotNull(postResponse);
	}

	@Test
	public void testGetAllCustomers() {
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<String> response = restTemplate.exchange(getRootUrl() + "/customers", HttpMethod.GET, entity,
				String.class);
		assertNotNull(response.getBody());
	}

	@Test
	public void testGetCustomerById() {
		CustomerDTO customer = restTemplate.getForObject(getRootUrl() + "/customers/1", CustomerDTO.class);
		System.out.println(customer.getFirstName());
		assertNotNull(customer);
	}

}
