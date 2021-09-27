package com.cs.dto;

import java.io.Serializable;

public class ProductDTO implements Serializable {
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1868647L;
	
	private Long productCode;
	private String productName;
	private Double price;
	private Double persentDiscount;

	public ProductDTO() {

	}

	public ProductDTO(String productName, Double price, Double persentDiscount) {
		this.productName = productName;
		this.price = price;
		this.persentDiscount = persentDiscount;
	}

	public Long getProductCode() {
		return productCode;
	}

	public void setProductCode(Long productCode) {
		this.productCode = productCode;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getPersentDiscount() {
		return persentDiscount;
	}

	public void setPersentDiscount(Double persentDiscount) {
		this.persentDiscount = persentDiscount;
	}
	
}
