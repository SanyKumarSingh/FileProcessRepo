package com.cs.dto;

import java.io.Serializable;
import java.util.List;

public class BillResponseDTO implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1854867L;

	private String customerName;
	private String customerMobileNumber;
	private Long billId;
	private List<ItemResponseDTO> item;
	private double totalAmount;
	
	public String getCustomerMobileNumber() {
		return customerMobileNumber;
	}
	public void setCustomerMobileNumber(String customerMobileNumber) {
		this.customerMobileNumber = customerMobileNumber;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public List<ItemResponseDTO> getItem() {
		return item;
	}
	public void setItem(List<ItemResponseDTO> item) {
		this.item = item;
	}
	public double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}
	public Long getBillId() {
		return billId;
	}
	public void setBillId(Long billId) {
		this.billId = billId;
	}
	
}
