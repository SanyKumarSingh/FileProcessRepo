package com.cs.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "bills")
public class Bill implements Serializable {
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1657675L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long billId;
	private Date billingDate;
	private Double billAmount;
	private String cashierName;
	private Long customerId;
	
	@OneToMany(mappedBy="bill", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
	private List<Item> items;
	
	public Bill() {

	}

	public Bill(Date billingDate, double billAmount, String cashierName) {
		this.billingDate = billingDate;
		this.billAmount = billAmount;
		this.cashierName = cashierName;
	}

	public Long getBillId() {
		return billId;
	}

	public void setBillId(Long billId) {
		this.billId = billId;
	}

	public Date getBillingDate() {
		return billingDate;
	}

	public void setBillingDate(Date billingDate) {
		this.billingDate = billingDate;
	}

	public Double getBillAmount() {
		return billAmount;
	}

	public void setBillAmount(Double billAmount) {
		this.billAmount = billAmount;
	}

	public String getCashierName() {
		return cashierName;
	}

	public void setCashierName(String cashierName) {
		this.cashierName = cashierName;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

}
