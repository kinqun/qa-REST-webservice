package com.qa.products.controller;

import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.qa.products.dto.ProductInfoDto;
import com.qa.products.entity.Product;
import com.qa.products.exceptions.ProductAlreadyExistsException;
import com.qa.products.exceptions.ProductNotFoundException;
import com.qa.products.service.ProductServiceImpl;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/v1")
public class ProductController {
	@Autowired
	ProductServiceImpl productService;
	
	ResponseEntity<?> responseEntity;
	
	@GetMapping("/product")
	@ApiOperation(value="get a list of all products", notes="returns all products as a list of Product")
	public ResponseEntity<?> getAllProducts() {
		try {
			List<Product> allProducts = this.productService.getAllProducts();
			responseEntity = new ResponseEntity<>(allProducts, HttpStatus.OK);
		}catch(Exception e) {
			responseEntity = new ResponseEntity<>("Internal error occured", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return responseEntity;
	}

	@GetMapping("/product/{id}")
	@ApiOperation(value="get a product as per id", notes="returns a single product with matching id")
	public ResponseEntity<?> getProductById(@Min(0) @PathVariable("id") int id) throws ProductNotFoundException {
		try {
			Product fetchedProduct = this.productService.getProductById(id);
			responseEntity = new ResponseEntity<>(fetchedProduct, HttpStatus.OK);
		}catch(ProductNotFoundException e) {
			throw e;
		}catch(Exception e) {
			responseEntity = new ResponseEntity<>("internal error occured", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return responseEntity;
	}

	@PostMapping("/product")
	@ApiOperation(value="add a product to the database", notes="adds a product to the database and returns product if successful")
	public ResponseEntity<?> addProduct(@Valid @RequestBody Product product) throws ProductAlreadyExistsException {
		try {
			Product addedProduct = this.productService.addProduct(product);
			responseEntity = new ResponseEntity<>(addedProduct,HttpStatus.CREATED);
		}catch(ProductAlreadyExistsException e) {
			throw e;
		}catch(Exception e) {
			responseEntity = new ResponseEntity<>("internal error occured", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return responseEntity;
	}
	
	@PutMapping("/product")
	@ApiOperation(value="update an existing product", notes="updates an existing product and returns the product if successful")
	public ResponseEntity<?> updateProduct(@Valid @RequestBody Product product) throws ProductNotFoundException{
		try {
			Product updatedProduct = this.productService.updateProduct(product);
			responseEntity = new ResponseEntity<>(updatedProduct,HttpStatus.OK);
		}catch(ProductNotFoundException e) {
			throw e;
		}catch(Exception e) {
			responseEntity = new ResponseEntity<>("internal error occured", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return responseEntity;
	}
	
	@DeleteMapping("/product/{id}")
	@ApiOperation(value="deletes an existing product", notes="deletes an existing product, and returns true/false (success/failure)")
	public ResponseEntity<?> deleteProduct(@Min(1 )@PathVariable("id") int id) throws ProductNotFoundException{
		try {
			boolean isDeleted = this.productService.deleteProduct(id);
			if(isDeleted) {
				responseEntity = new ResponseEntity<>("product deleted", HttpStatus.OK);
			}
			
		}catch(ProductNotFoundException e) {
			throw e;
		}catch(Exception e) {
			responseEntity = new ResponseEntity<>("internal error occured", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return responseEntity;
	}
	
	@GetMapping("/product/category/{category}")
	@ApiOperation(value="get products by category", notes="returns list of product filtered by cateogory")

	public ResponseEntity<?> getProductByCategory(@NotNull @Pattern(regexp = "^[a-zA-Z]+$") @PathVariable("category") String category){
		try {
			List<Product> product = this.productService.getProductByCateogry(category);
			responseEntity = new ResponseEntity<>(product, HttpStatus.OK);
		}catch(Exception e) {
			responseEntity = new ResponseEntity<>("internal error occured", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return responseEntity;
	}
	
	@PutMapping("/product/update-product")
	@ApiOperation(value="update existing product's price and category",notes="updates existing product's price and cateogry with matching id")
	public ResponseEntity<?> updateProductDetail(@Valid @RequestBody Product product) throws ProductNotFoundException{
		try {
			Product updated = this.productService.updateProductDetail(product.getId(),product.getCategory(),product.getPrice());
			responseEntity = new ResponseEntity<>(updated, HttpStatus.OK);
		}catch(ProductNotFoundException e) {
			throw e;
		}catch(Exception e) {
			responseEntity = new ResponseEntity<>("interal error occured", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return responseEntity;
	}
	
	@GetMapping("/product/max-price")
	@ApiOperation(value="get product with highest price",notes="return a string of product(s) with the highest price")
	public ResponseEntity<?> getMaxProductPrice(){
		try {
			List<Product> product = this.productService.getMaxProductPrice();
			StringBuilder resStr = new StringBuilder(new String(""));
			product.forEach(p->resStr.append(p.getName()).append(" - ").append(p.getPrice()).append("\n"));
			
			responseEntity = new ResponseEntity<>("product with highest price: \n" + new String(resStr), HttpStatus.OK);
		}catch(Exception e) {
			responseEntity = new ResponseEntity<>("internal error occured", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return responseEntity;
	}
	
	@GetMapping("/product/min-price")
	@ApiOperation(value="get product with lowest price", notes="return a string of product(s) with lowest price")
	public ResponseEntity<?> getMinProductPrice(){
		try {
			List<Product> product = this.productService.getMinProductPrice();
			StringBuilder resStr = new StringBuilder(new String(""));
			product.forEach(p-> resStr.append(p.getName()).append(" - ").append(p.getPrice()).append("\n"));
			
			responseEntity = new ResponseEntity<>("products with lowest price: \n" + new String(resStr), HttpStatus.OK);
		}catch(Exception e) {
			responseEntity = new ResponseEntity<>("internal error occured", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return responseEntity;
	}
	
	@GetMapping("/product/product-info")
	@ApiOperation(value="get a product with id, name, price", notes="returns a dto containing only product id,name,price")
	public ResponseEntity<?> getAllProductsInfo(){
		try {
			List<ProductInfoDto> productDtoList = this.productService.getAllProductsNameAndPrice();
			responseEntity = new ResponseEntity<>(productDtoList, HttpStatus.OK);
		}catch(Exception e) {
			responseEntity = new ResponseEntity<>("internal error occured", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return responseEntity;
	}
}
