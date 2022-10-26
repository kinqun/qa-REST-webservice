package com.qa.products.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.qa.products.controller.ProductController;
import com.qa.products.entity.Product;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
public class ProductRepositoryTest {

	@Autowired
	ProductRepository prodRepo;
	
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
	@DisplayName("save-product-test")
	public void givenProductToSave_whenAddProduct_returnProduct() {
		Product product = prodRepo.save(prod1);
		assertNotNull(product);
		assertEquals("prod1", product.getName());
		assertEquals("default category", product.getCategory());
	}
	
	@Test
	@DisplayName("get-all-products-test")
	public void given_whenGetProductById_returnProductList() {
		prodRepo.save(prod1);
		prodRepo.save(prod2);
		prodRepo.save(prod3);
		
		List<Product> products = prodRepo.findAll();
		//assertEquals(3, products.size());
		assertEquals("prod1", products.get(0).getName());
		assertEquals("prod2", products.get(1).getName());
		assertEquals("prod3", products.get(2).getName());
	}
	
	@Test
	@DisplayName("get-product-by-id-test")
	public void givenProductId_whenGetProductById_returnOptionalProduct() {
		prodRepo.save(prod1);
		Optional<Product> product = prodRepo.findById(1001);
		assertThat(product).isNotEmpty();
		assertEquals("prod1", product.get().getName());
	}
	
	@Test
	@DisplayName("get-product-by-non-existing-id-test")
	public void givenProductInvalidId_whenGetProductById_returnEmptyOptional() {
		prodRepo.save(prod1);
		Optional<Product> product = prodRepo.findById(1002);
		assertThat(product).isEmpty();
	}
}
