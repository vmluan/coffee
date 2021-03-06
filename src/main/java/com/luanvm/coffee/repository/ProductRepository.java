package com.luanvm.coffee.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.luanvm.coffee.domain.Product;


public interface ProductRepository extends PagingAndSortingRepository<Product, Integer> {
//	List<Product> findAllProduct();
	@Query("select c from Product c where c.productName  = :productName")
	List<Product> findProductByName(@Param("productName") String productName);
	
	@Query("select c from Product c where c.isDeleted = false")
	List<Product> findValidProduct();

}
