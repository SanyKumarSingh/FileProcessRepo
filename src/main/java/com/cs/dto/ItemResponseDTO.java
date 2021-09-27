package com.cs.dto;

import java.io.Serializable;

public class ItemResponseDTO implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1854867L;
	
	private String item;
	private double unitPrice;
	private double persentDiscount;
	private int quantity;
	private Double itemValue;
	
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	public double getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}
	public double getPersentDiscount() {
		return persentDiscount;
	}
	public void setPersentDiscount(double persentDiscount) {
		this.persentDiscount = persentDiscount;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public Double getItemValue() {
		return itemValue;
	}
	public void setItemValue(Double itemValue) {
		this.itemValue = itemValue;
	}
}
