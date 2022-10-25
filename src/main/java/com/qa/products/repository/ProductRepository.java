package com.qa.products.repository;

import org.springframework.stereotype.Repository;

import com.qa.products.entity.Product;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

@Repository
@Transactional
public interface ProductRepository extends JpaRepository<Product, Integer> {

	@Query("select p from Product p where p.category = :category")
	List<Product> findByCategory(String category);

	@Query("select p from Product p where p.price = (select min(p.price) from Product p)")
	List<Product> getMinProductPrice();
	
	@Query("select p from Product p where p.price = (select max(p.price) from Product p)")
	List<Product> getMaxProductPrice();

	@Modifying
	@Query("update Product p set p.price= :price , p.category= :category where p.id = :id")
	int updateProductDetail(double price, String category, int id);
}
