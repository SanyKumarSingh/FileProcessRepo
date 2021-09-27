package com.cs.service;

import java.util.List;

import com.cs.dto.ProductDTO;
import com.cs.exception.DataAccessException;
import com.cs.exception.InternalServerException;
import com.cs.exception.ResourceNotFoundException;

public interface ProductService {
	
	public List<ProductDTO> getAllProducts() throws InternalServerException ;
	
	public ProductDTO getProductById(Long productCode) throws ResourceNotFoundException, InternalServerException ;
	
	public List<ProductDTO> addProducts(List<ProductDTO> productDTOs);
	
	public ProductDTO updateProduct(Long productCode, ProductDTO productDetails)
			throws ResourceNotFoundException, DataAccessException;

}
