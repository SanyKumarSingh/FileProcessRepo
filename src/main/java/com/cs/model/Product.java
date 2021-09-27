/**
 * 
 */
package com.cs.model;

import java.io.Serializable;
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
@Table(name = "products")
public class Product implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1867867L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long productCode;
	private String productName;
	private Double price;
	private double persentDiscount = 0;
	
	@OneToMany(mappedBy="product", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
	private List<Item> items;

	public Product() {

	}

	public Product(String productName, Double price, double persentDiscount) {
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

	public double getPersentDiscount() {
		return persentDiscount;
	}

	public void setPersentDiscount(double persentDiscount) {
		this.persentDiscount = persentDiscount;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}
	
}
