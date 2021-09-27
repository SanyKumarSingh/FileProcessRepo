package com.cs.dto;

import java.io.Serializable;
import java.util.Map;

public class BillRequestDTO implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1867867L;
	
	private String customerMobileNumber;
	
	// Key-productCode Value-Quantity
	private Map<Long,Integer> productQtyMap;
	
	private boolean generateBill;
	
	public String getCustomerMobileNumber() {
		return customerMobileNumber;
	}
	public void setCustomerMobileNumber(String customerMobileNumber) {
		this.customerMobileNumber = customerMobileNumber;
	}
	
	public Map<Long, Integer> getProductQtyMap() {
		return productQtyMap;
	}
	public void setProductQtyMap(Map<Long, Integer> productQtyMap) {
		this.productQtyMap = productQtyMap;
	}
	
	public boolean isGenerateBill() {
		return generateBill;
	}
	public void setGenerateBill(boolean generateBill) {
		this.generateBill = generateBill;
	}
	
	@Override
	public String toString() {
		return "Customer [customerMobileNumber=" + customerMobileNumber +  "]";
	}

}
