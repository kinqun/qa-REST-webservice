package com.qa.products.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qa.products.dto.ProductInfoDto;
import com.qa.products.entity.Product;
import com.qa.products.exceptions.ProductAlreadyExistsException;
import com.qa.products.exceptions.ProductNotFoundException;
import com.qa.products.repository.ProductRepository;

@Service	
public class ProductServiceImpl implements ProductService{
	
	@Autowired
	ProductRepository productRepo;
	
	@Autowired
	ModelMapper modelMapper;

	@Override
	public List<Product> getAllProducts() {
		return productRepo.findAll();
	}

	@Override
	public Product getProductById(int id) throws ProductNotFoundException {
		Optional<Product> existingProduct = this.productRepo.findById(id);
		if(!existingProduct.isPresent()) {
			throw new ProductNotFoundException();
		}else {
			return existingProduct.get();
		}
	}

	@Override
	public Product addProduct(Product product) throws ProductAlreadyExistsException {
		Optional<Product> existingProduct = this.productRepo.findById(product.getId());
		if(existingProduct.isPresent()) {
			throw new ProductAlreadyExistsException();
		}else {
			return this.productRepo.save(product);
		}
	}

	@Override
	public Product updateProduct(Product product) throws ProductNotFoundException {
		Optional<Product> existingProduct = this.productRepo.findById(product.getId());
		if(!existingProduct.isPresent()) {
			throw new ProductNotFoundException();
		}else {
			return this.productRepo.save(product);
		}
	}

	@Override
	public boolean deleteProduct(int id) throws ProductNotFoundException {
		boolean isDeleted = false;
		Optional<Product> existingProduct = this.productRepo.findById(id);
		if(!existingProduct.isPresent()) {
			throw new ProductNotFoundException();
			
		}else {
			this.productRepo.deleteById(id);
			isDeleted = true;
		}
		
		return isDeleted;
	}

	@Override
	public List<Product> getProductByCateogry(String category){
		//this.productRepo.saveand
		return this.productRepo.findByCategory(category);
	}

	@Override
	public Product updateProductDetail(int id, String category, double price) throws ProductNotFoundException {
		Product updatedProduct = null;
		int rows = this.productRepo.updateProductDetail(price, category, id);
		
		if(rows > 0) { 
			updatedProduct = this.productRepo.findById(id).get();
		}else {
			throw new ProductNotFoundException();
		}
		return updatedProduct;
	}

	@Override
	public List<Product> getMinProductPrice() {
		return this.productRepo.getMinProductPrice();
	}
	
	@Override
	public List<Product> getMaxProductPrice() {
		return this.productRepo.getMaxProductPrice();
	}
	
	@Override
	public List<ProductInfoDto> getAllProductsNameAndPrice(){
		/*
		List<ProductInfoDto> productDtoList = new ArrayList<>();
		List<Product> productsList = this.productRepo.findAll();
		productsList.forEach(p->{
			ProductInfoDto product = ProductInfoDto.builder().id(p.getId()).name(p.getName()).price(p.getPrice()).build();
			productDtoList.add(product);
			
		});
		return productDtoList;
		*/
		return this.productRepo.findAll().stream().map(this::mapToProductDto).collect(Collectors.toList());
	}

	
	private ProductInfoDto mapToProductDto(Product product) {
		return this.modelMapper.map(product, ProductInfoDto.class);
	}
}
