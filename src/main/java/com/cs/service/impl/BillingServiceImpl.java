package com.cs.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cs.dto.BillRequestDTO;
import com.cs.dto.BillResponseDTO;
import com.cs.dto.CustomerDTO;
import com.cs.dto.ItemResponseDTO;
import com.cs.exception.DataAccessException;
import com.cs.exception.InternalServerException;
import com.cs.exception.ResourceNotFoundException;
import com.cs.model.Bill;
import com.cs.model.Customer;
import com.cs.model.Item;
import com.cs.model.Product;
import com.cs.repository.BillRepository;
import com.cs.repository.CustomerRepository;
import com.cs.repository.ProductRepository;
import com.cs.service.BillingService;
import com.cs.service.CustomerService;

@Service
public class BillingServiceImpl implements BillingService {

	private static final Logger logger = LoggerFactory.getLogger(BillingServiceImpl.class);

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private BillRepository billRepository;

	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private CustomerService customerService;
	
	/*
	 * Used @Transactional on this method to enable container managed transaction provided by Spring Boot.
	 * 
	 * Spring Boot takes care of all the boilerplate code and integrates Hibernate’s and JPA’s transaction handling. 
	 * To activate transaction for SpringMVC, @EnableTransactionManagement is also needed but not for SpringBoot.
	 * 
	 * Default Propagation is REQUIRED :  join an active transaction or to start a new one.
	 * SUPPORTS : join an activate transaction if exists, else continue w/o transaction.
	 * MANDATORY: join an activate transaction if exists, else throw an Exception.
	 * REQUIRES_NEW : suspend an activate transaction if exists, start a new transaction.
	 * NEVER, NOT_SUPPORTED : execute w/o transaction
	 */
	@Override
	@Transactional
	public BillResponseDTO generateBill(BillRequestDTO billRequest) throws InternalServerException, DataAccessException {
		logger.info("Processing request to Calculate Total or Generate Bill");

		BillResponseDTO billingResponse = new BillResponseDTO();
		List<Item> items = new ArrayList<Item>();
		List<ItemResponseDTO> itemPurchased = new ArrayList<ItemResponseDTO>();

		// Fetch customer details corresponding to customer Mobile Number
		Customer customer = null;
		if(billRequest.getCustomerMobileNumber() != null) {
			try {
				customer = customerRepository.findByMobileNumber(billRequest.getCustomerMobileNumber());
			} catch (Exception exception) {
				logger.error("Exception while fetching Customer details by mobileNumber "
						+ billRequest.getCustomerMobileNumber(), exception.getMessage());
				throw new InternalServerException("Customer details could not be fetched for mobileNumber "
						+ billRequest.getCustomerMobileNumber() + " there seems to be an issue with the Database");
			}
			if (customer != null) {
				billingResponse.setCustomerMobileNumber(customer.getMobileNumber());
				billingResponse.setCustomerName(customer.getFirstName() + " " + customer.getLastName());
			}
		}
		
		// List of all the products purchased by customer
		logger.info("Processing request to Calculate Total");
		try {
			List<Product> products = productRepository.findAllById(billRequest.getProductQtyMap().keySet());
			
			// Iterate the ProductQtyMap to calculate the bill
			billRequest.getProductQtyMap().forEach((productCode, quantity) -> {

				// Iterating the products fetched from DB
				products.forEach((product) -> {
					if (product.getProductCode().equals(productCode)) {
						Item item = new Item();
						item.setItemName(product.getProductName());
						item.setQuantity(quantity);
						item.setProduct(product);
						// Calculate total amount
						double discount = (product.getPersentDiscount() / 100) * product.getPrice();
						double discountedPrice = product.getPrice() - discount;
						double itemValue = discountedPrice * quantity;
						//billingResponse.setTotalAmount(billingResponse.getTotalAmount() + (discountedPrice * quantity));
						billingResponse.setTotalAmount(billingResponse.getTotalAmount() + itemValue);
						item.setItemValue(itemValue);
						items.add(item);

						ItemResponseDTO itemDetails = new ItemResponseDTO();
						itemDetails.setItem(product.getProductName());
						itemDetails.setUnitPrice(product.getPrice());
						itemDetails.setPersentDiscount(product.getPersentDiscount());
						itemDetails.setQuantity(quantity);
						itemDetails.setItemValue(itemValue);
						itemPurchased.add(itemDetails);
					}
				});
			});
			billingResponse.setItem(itemPurchased);
		} catch (Exception exception) {
			logger.error("Exception while fetching Product details by productCode "
					+ billRequest.getProductQtyMap().keySet(), exception.getMessage());
			throw new InternalServerException("Product details could not be fetched for productCode "
					+ billRequest.getProductQtyMap().keySet() + " there seems to be an issue with the Database records");
		}

		

		// If the request is for bill generation and not just to calculate item total
		if (billRequest.isGenerateBill()) {
			logger.info("Processing request to Generate Bill");
			
			try {
				Bill finalBill = new Bill();
				finalBill.setBillAmount(billingResponse.getTotalAmount());
				finalBill.setBillingDate(new Date());
				finalBill.setCashierName("ADMIN");
				finalBill.setItems(items);
				items.forEach((item) -> {
					item.setBill(finalBill);
				});

				// Calculate Reward points as 2 percent of total bill amount, only if the
				// customer is registered
				if (customer != null) {
					double pointsEarned = (2 * billingResponse.getTotalAmount()) / 100;
					Long rewardPoints = customer.getRewardPoints() + Math.round(pointsEarned);
					// Update Customer reward points by calling the setter method on Managed Entity
					customer.setRewardPoints(rewardPoints);
					finalBill.setCustomerId(customer.getCustomerId());
				}
				Bill updatedBill = billRepository.save(finalBill);
				billingResponse.setBillId(updatedBill.getBillId());
			} catch (Exception exception) {
				logger.error("Exception while saving billing details.", exception.getMessage());
				throw new DataAccessException("Billing details could not be saved, seems some issue with the Database.");
			}
		}
		
		return billingResponse;
	}

	@Override
	public BillResponseDTO getBillById(Long billId) throws ResourceNotFoundException, InternalServerException {
		logger.info("Processing request to fetch details for billId : " + billId);

		// findById() of CrudRepository returns Optional instance so alternatively
		// orElseThrow() method of Optional class could be used to throw exception.
		Optional<Bill> bill = null;
		try {
			bill = billRepository.findById(billId);
		} catch (Exception exception) {
			logger.error("Exception while fetching Billing details by billId " + billId,
					exception.getMessage());
			throw new InternalServerException("Billing details could not be fetched.");
		}

		BillResponseDTO billResponseDTO = null;
		if (bill.isPresent()) {
			billResponseDTO = new BillResponseDTO();
			// Set Bill details
			billResponseDTO.setBillId(bill.get().getBillId());
			billResponseDTO.setTotalAmount(bill.get().getBillAmount());
			
			// Set Item Details
			List<ItemResponseDTO> items = new ArrayList<ItemResponseDTO>();
			bill.get().getItems().forEach((item) -> {
				ItemResponseDTO itemResponse =  new ItemResponseDTO();
				itemResponse.setItem(item.getItemName());
				itemResponse.setPersentDiscount(item.getProduct().getPersentDiscount());
				itemResponse.setItemValue(item.getItemValue());
				itemResponse.setQuantity(item.getQuantity());
				itemResponse.setUnitPrice(item.getProduct().getPrice());
				items.add(itemResponse);
			});
			billResponseDTO.setItem(items);
			
			// Fetch and Set customer details if exist
			if(bill.get().getCustomerId() != null) {
				CustomerDTO customer = customerService.getCustomerById(bill.get().getCustomerId());
				billResponseDTO.setCustomerName(customer.getFirstName() + " " + customer.getLastName());
				billResponseDTO.setCustomerMobileNumber(customer.getMobileNumber());
			}
			
		} else {
			logger.info("Bill not found for this id :: " + billId);
			throw new ResourceNotFoundException("Bill not found for this id :: " + billId);
		}
		return billResponseDTO;
	}

	@Override
	public Map<String, Double> calculateRevenue(String flag) throws ResourceNotFoundException {
		logger.info("Processing request to calculate revenue of " + flag + "Bills");
		
		Double totalRevenue = null; 
		if(flag.toUpperCase().equals("DAILY")) {
			totalRevenue = billRepository.calculateDailyRevenue();
		}
		if(totalRevenue == null) {
			logger.info("No Bill found to calculate "+ flag + " revenue" );
			throw new ResourceNotFoundException("No Bill found to calculate "+ flag + " revenue" );
		}
		Map<String, Double> response = new HashMap<>();
		response.put(flag.toUpperCase() + " Revenue : ", totalRevenue);
		return response;
	}

}
