package com.cs.service;

import java.util.Map;

import com.cs.dto.BillRequestDTO;
import com.cs.dto.BillResponseDTO;
import com.cs.exception.DataAccessException;
import com.cs.exception.InternalServerException;
import com.cs.exception.ResourceNotFoundException;

public interface BillingService {
	/*
	 * This method will generate bill for the Items purchased by the customer.
	 * 
	 * Based on Item cost and quantity of the item amount will be calculated.
	 * The applicable persentDiscount on each product will also be calculated.
	 * 
	 * Calculate customer Reward points as 2 percent of total bill amount
	 */
	public BillResponseDTO generateBill(BillRequestDTO bill) throws InternalServerException, DataAccessException ;
	
	public Map<String, Double> calculateRevenue(String flag) throws ResourceNotFoundException;
	
	public BillResponseDTO getBillById(Long billId) throws ResourceNotFoundException, InternalServerException ;

}