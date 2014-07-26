package com.luanvm.coffee.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.luanvm.coffee.domain.Product;


public interface ProductRepository extends PagingAndSortingRepository<Product, Integer> {
//	List<Product> findAllProduct();
	

}
