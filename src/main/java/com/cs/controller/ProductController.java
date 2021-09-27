package com.cs.controller;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cs.dto.ProductDTO;
import com.cs.exception.DataAccessException;
import com.cs.exception.ErrorDetails;
import com.cs.exception.InternalServerException;
import com.cs.exception.InvalidRequestException;
import com.cs.exception.ResourceNotFoundException;
import com.cs.service.ProductService;

@CrossOrigin
@RestController
@RequestMapping("/api/v1")
public class ProductController {

	private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

	@Autowired
	private ProductService productService;

	@GetMapping("/products")
	public List<ProductDTO> getAllProducts() throws InternalServerException {
		logger.info("Received request to fetch all Products");
		return productService.getAllProducts();
	}

	/*@GetMapping("/products/{id}")
	public ProductDTO getProductById(@PathVariable(value = "id") Long productCode)
			throws ResourceNotFoundException, InternalServerException {
		logger.info("Received request to fetch details for productCode : " + productCode);
		ProductDTO product = productService.getProductById(productCode);
		return product;
	}*/
	
	@GetMapping("/products/{id}")
	public ResponseEntity<Object> getProductById(@PathVariable(value = "id") Long productCode) {
		logger.info("Received request to fetch details for productCode : " + productCode);
		ProductDTO product;
		try {
			product = productService.getProductById(productCode);
			return ResponseEntity.ok().body(product);
		} catch (ResourceNotFoundException e) {
			logger.error("Product not found for this id productCode " + productCode);
			ErrorDetails errorDetails = new ErrorDetails(new Date(), "Product not found for this id productCode ", "productCode " + productCode);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
		} catch (InternalServerException e) {
			logger.error("Exception while fetching Product details by productCode " + productCode);
			ErrorDetails errorDetails = new ErrorDetails(new Date(), "Error to fetch Product details", "productCode" + productCode);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
		}
		
	}

	@PostMapping("/products")
	public List<ProductDTO> addProducts(@Valid @RequestBody List<ProductDTO> productDTOs) throws InvalidRequestException {
		logger.info("Received request to add new Products");
		
		List<ProductDTO> products = null;
		if (productDTOs != null && !productDTOs.isEmpty()) {
			for(ProductDTO productDTO : productDTOs) {
				if ((productDTO.getProductName() == null || productDTO.getProductName().isEmpty()) 
						|| productDTO.getPrice() == null) {
					logger.error("ProductName and its Unit Price are mandatory to add new product.");
					throw new InvalidRequestException("ProductName and its Unit Price are mandatory to add new product.");
				}
			}
			products = productService.addProducts(productDTOs);
		} else {
			logger.error("ProductName and its Unit Price are mandatory to add new product.");
			throw new InvalidRequestException("ProductName and its Unit Price are mandatory to add new product.");
		}
		return products;
	}
	
	@PutMapping("/products/{id}")
	public ProductDTO updateProduct(@PathVariable(value = "id") Long productCode,
			@Valid @RequestBody ProductDTO productDetails) throws ResourceNotFoundException, DataAccessException, InternalServerException {
		logger.info("Received request to update existing Product details");
		final ProductDTO updatedProduct = productService.updateProduct(productCode, productDetails);
		return updatedProduct;
	}

}
