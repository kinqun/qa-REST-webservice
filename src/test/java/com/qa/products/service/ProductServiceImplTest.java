package com.qa.products.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import com.qa.products.entity.Product;
import com.qa.products.exceptions.ProductAlreadyExistsException;
import com.qa.products.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {
	@Mock
	private ProductRepository prodRepo;
	
	@Autowired
	@InjectMocks
	private ProductServiceImpl prodService;
	
	Product prod1;
	Product prod2;
	Product prod3;
	
	List<Product> prodList;
	
	@BeforeEach
	public void setUp() {
		prod1 = new Product(1001,"prod1",111.11, "default category");
		prod2 = new Product(1002,"prod2",222.11, "default category");
		prod3 = new Product(1003,"prod3",333.11, "default category");
		prodList = Arrays.asList(prod1,prod2,prod3);
	}
	
	@AfterEach
	public void tearDown() {
		prod1 = prod2 = prod3 = null;
		prodRepo.deleteAll();
		prodList = null;
	}
	
	@Test
	@DisplayName("add-product-test")
	public void givenProductToSave_whenAddProduct_returnAddedProduct() throws ProductAlreadyExistsException {
		when(prodRepo.findById(any())).thenReturn(Optional.ofNullable(null));
		when(prodRepo.save(any())).thenReturn(prod1);
		Product product = prodService.addProduct(prod1);
		assertNotNull(product);
		assertEquals(1001, product.getId());
		assertEquals("prod1", product.getName());
		
		verify(prodRepo,times(1)).findById(any());
		verify(prodRepo,times(1)).save(any());
	}
	
	@Test
	@DisplayName("add-product-throws-exception-test")
	public void givenExistingProductToSave_whenAddProduct_returnThrowProductAlreadyExistingException() {
		when(prodRepo.findById(any())).thenReturn(Optional.of(prod1));
		assertThrows(ProductAlreadyExistsException.class, ()->prodService.addProduct(prod1));
	}
}
