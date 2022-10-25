package com.qa.products.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name="product_db")
public class Product {
	@Id
	@SequenceGenerator(name="mySeq", initialValue=1000, allocationSize = 1, sequenceName="productdb")
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator="mySeq")
	//@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="product_id")
	private int id;
	
	@Column(name="product_name")
	@NotNull
	@Size(min=2,max=45,message="product name must be between 2-45 characters")
	@Pattern(regexp="^[\\w+\\s]+$", message="invalid product name")
	private String name;
	
	@Column(name="product_price")
	@NotNull
	@Min(0)
	private double price;
	
	@Column(name="product_category")
	@NotNull
	@Size(min=2, max=25, message="product category must be between 2-25")
	@Pattern(regexp="^[\\w+-: ]+$", message="invalid product category")
	private String category;
}
