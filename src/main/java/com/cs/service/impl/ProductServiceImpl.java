package com.cs.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cs.dto.ProductDTO;
import com.cs.exception.DataAccessException;
import com.cs.exception.InternalServerException;
import com.cs.exception.ResourceNotFoundException;
import com.cs.model.Product;
import com.cs.repository.ProductRepository;
import com.cs.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {
	
	private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

	@Autowired
	private ProductRepository productRepository;

	@Override
	public List<ProductDTO> getAllProducts() throws InternalServerException {
		logger.info("Processing request to fetch all Products");
		List<Product> products = null;
		try {
			// Fetch all the enlisted products
			products = productRepository.findAll();
		} catch (Exception exception) {
			logger.error("Exception while fetching All Product details.", exception.getMessage());
			throw new InternalServerException("Product details could not be fetched.");
		}
		List<ProductDTO> productDTOs = new ArrayList<ProductDTO>();
		// Iterating the Products fetched from DB
		products.forEach((product) -> {
			ProductDTO productDTO = new ProductDTO();
			productDTO.setProductCode(product.getProductCode());
			productDTO.setProductName(product.getProductName());
			productDTO.setPrice(product.getPrice());
			productDTO.setPersentDiscount(product.getPersentDiscount());
			productDTOs.add(productDTO);
		});
		return productDTOs;
	}
	
	@Override
	public ProductDTO getProductById(Long productCode) throws ResourceNotFoundException, InternalServerException {
		logger.info("Processing request to fetch details for productCode : " + productCode);
		
		// findById() of CrudRepository returns Optional instance so alternatively
		// orElseThrow() method of Optional class could be used to throw exception.
		Optional<Product> product = null;
		try {
			product = productRepository.findById(productCode);
		} catch (Exception exception) {
			logger.error("Exception while fetching Product details by productCode " + productCode,
					exception.getMessage());
			throw new InternalServerException("Product details could not be fetched.");
		}		
		ProductDTO productDTO = null;
		if (product.isPresent()) {
			productDTO = new ProductDTO();
			productDTO.setProductCode(product.get().getProductCode());
			productDTO.setProductName(product.get().getProductName());
			productDTO.setPrice(product.get().getPrice());
			productDTO.setPersentDiscount(product.get().getPersentDiscount());
		} else {
			logger.info("Product not found for this id productCode " + productCode);
			throw new ResourceNotFoundException("Product not found for this id productCode " + productCode);
		}
		return productDTO;
	}

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
	public List<ProductDTO> addProducts(List<ProductDTO> productDTOs) {
		
		List<Product> products = new ArrayList<Product>();
		productDTOs.forEach((productDTO) -> {
			Product product = new Product();
			product.setProductName(productDTO.getProductName());
			product.setPrice(productDTO.getPrice());
			product.setPersentDiscount(productDTO.getPersentDiscount());
			products.add(product);
		});
		
		// Save Product details and store the managed entity to fetch the productId 
		List<Product> addedProducts = productRepository.saveAll(products);
		
		productDTOs.clear();
		addedProducts.forEach((product) -> {
			ProductDTO productDTO = new ProductDTO();
			productDTO.setProductCode(product.getProductCode());
			productDTO.setProductName(product.getProductName());
			productDTO.setPrice(product.getPrice());
			productDTO.setPersentDiscount(product.getPersentDiscount());
			productDTOs.add(productDTO);
		});
		return productDTOs;
	}
	
	
	@Override
	public ProductDTO updateProduct(Long productCode, ProductDTO productDTO)
			throws ResourceNotFoundException, DataAccessException {
		logger.info("Processing request to update existing Product details");

		Product product = productRepository.findById(productCode).orElseThrow(() -> {
			logger.info("Product not found for this productCode :: " + productCode);
			return new ResourceNotFoundException("Product not found for this productCode :: " + productCode);
		});

		// Update the applicable customer details
		if (productDTO.getPrice() != null) {
			product.setPrice(productDTO.getPrice());
		}
		if (productDTO.getPersentDiscount() != null) {
			product.setPersentDiscount(productDTO.getPersentDiscount());
		}

		try {
			productRepository.save(product);
		} catch (Exception exception) {
			logger.error("Exception while updating Product details by productCode " + productCode,
					exception.getMessage());
			throw new DataAccessException("Product details could not be updated.");
		}
		productDTO.setProductCode(product.getProductCode());
		productDTO.setPrice(product.getPrice());
		productDTO.setProductName(product.getProductName());
		productDTO.setPersentDiscount(product.getPersentDiscount());
		
		return productDTO;
	}

}
