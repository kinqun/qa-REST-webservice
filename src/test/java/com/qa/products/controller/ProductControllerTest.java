package com.qa.products.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
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
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.qa.products.entity.Product;
import com.qa.products.exceptions.ProductAlreadyExistsException;
import com.qa.products.service.ProductService;
import com.qa.products.service.ProductServiceImpl;

@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {
	@Mock
	private ProductServiceImpl prodService;
	
	@Autowired
	@InjectMocks
	private ProductController prodController;
	
	@Autowired
	MockMvc mockMvc;
	
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
		
		mockMvc = MockMvcBuilders.standaloneSetup(prodController).build();
	}
	
	@AfterEach
	public void tearDown() {
		prod1 = prod2 = prod3 = null;
		prodList = null;
	}
	
	@Test
	@DisplayName("add-product-test")
	public void givenProductToSave_whenAddProduct_returnProductAsJSONwithStatusCreated() throws Exception {
		when(prodService.addProduct(any())).thenReturn(prod1);
		mockMvc.perform(post("/api/v1/product")
					.contentType(MediaType.APPLICATION_JSON)
					.content(asJsonString(prod1)))
			   .andExpect(status().isCreated())
			   .andExpect(jsonPath("$.name").value("prod1"));
		
	}
	
	@Test
	@DisplayName("get-all-product-test")
	public void given_whenGetAllProducts_returnProductsList() throws Exception {
		when(prodService.getAllProducts()).thenReturn(prodList);
		mockMvc.perform(get("/api/v1/product").accept(MediaType.APPLICATION_JSON))
			.andDo(MockMvcResultHandlers.print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].name").value("prod1"));
	}
	
	
	@Test
	@DisplayName("get-product-by-id-test")
	public void givenProductId_whenGetProductById_returnProductAsJSONwithStatusOK() throws Exception {
		when(prodService.getProductById(anyInt())).thenReturn(prod1);
		mockMvc.perform(get("/api/v1/product/{id}","1001").accept(MediaType.APPLICATION_JSON))
			.andDo(MockMvcResultHandlers.print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.name").value("prod1"));
	}
	
	
	public static String asJsonString(Object obj) {
		ObjectMapper Obj = new ObjectMapper();
		String jsonStr = null;
		
		try {
			jsonStr = Obj.writeValueAsString(obj);
			System.out.println(jsonStr);
		}catch(IOException e) {
			e.printStackTrace();
		}
		return jsonStr;
	}
	
}
