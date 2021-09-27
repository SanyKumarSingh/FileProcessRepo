package com.cs.controller;

import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cs.dto.BillRequestDTO;
import com.cs.dto.BillResponseDTO;
import com.cs.exception.DataAccessException;
import com.cs.exception.InternalServerException;
import com.cs.exception.InvalidRequestException;
import com.cs.exception.ResourceNotFoundException;
import com.cs.service.BillingService;

@CrossOrigin
@RestController
@RequestMapping("/api/v1")
public class BillController {

	private static final Logger logger = LoggerFactory.getLogger(BillController.class);

	@Autowired
	private BillingService billingService;

	@PostMapping("/generateBill")
	public BillResponseDTO generateBill(@Valid @RequestBody BillRequestDTO billRequest)
			throws InternalServerException, DataAccessException, InvalidRequestException {
		logger.info("Received request to Calculate Total or Generate Bill");

		BillResponseDTO finalBill = null;
		if (billRequest.getProductQtyMap() != null && !billRequest.getProductQtyMap().isEmpty()) {
			finalBill = billingService.generateBill(billRequest);
		} else {
			logger.error("ProductCode and Quantity are mandatory to calculate total or generate bill.");
			throw new InvalidRequestException(
					"ProductCode and Quantity are mandatory to calculate total or generate bill.");
		}
		return finalBill;
	}
	
	/*
	 * flag values could be daily, weekly, monthly, annually
	 */
	@GetMapping("/revenue/{flag}")
	public Map<String, Double> calculateRevenue(@PathVariable(value = "flag") String flag) throws ResourceNotFoundException {
		logger.info("Received request to calculate revenue of " + flag + "Bills");
		return billingService.calculateRevenue(flag);
	}
	
	@GetMapping("/bills/{id}")
	public BillResponseDTO getBillById(@PathVariable(value = "id") Long billId)
			throws ResourceNotFoundException, InternalServerException {
		logger.info("Received request to fetch details for billId : " + billId);
		BillResponseDTO bill = billingService.getBillById(billId);
		return bill;
	}

}
