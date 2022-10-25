package com.qa.products.service;

import java.util.List;

import com.qa.products.dto.ProductInfoDto;
import com.qa.products.entity.Product;
import com.qa.products.exceptions.ProductAlreadyExistsException;
import com.qa.products.exceptions.ProductNotFoundException;

public interface ProductService {

	public List<Product> getAllProducts();
	public Product getProductById(int id) throws ProductNotFoundException;
	public Product addProduct(Product product) throws ProductAlreadyExistsException;
	public Product updateProduct(Product product) throws ProductNotFoundException;
	public boolean deleteProduct(int id) throws ProductNotFoundException;
	public List<Product> getProductByCateogry(String category);
	public Product updateProductDetail(int id, String category, double price) throws ProductNotFoundException;
	public List<Product> getMinProductPrice();
	public List<Product> getMaxProductPrice();
	
	public List<ProductInfoDto> getAllProductsNameAndPrice();
}
