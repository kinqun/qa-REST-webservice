package com.qa.products.repository;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.qa.products.entity.Product;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase
public class ProductRepositoryTest {

	@Autowired
	ProductRepository prodRepo;
	
	Product prod1;
	Product prod2;
	Product prod3;
	
	List<Product> prodList;
	
	@BeforeEach
	public void setup() {
		prod1 = new Product();
	}
}
