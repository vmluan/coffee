package com.luanvm.coffee.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.luanvm.coffee.domain.Product;
import com.luanvm.coffee.domain.TH_Category;


public interface CategoryRepository extends PagingAndSortingRepository<TH_Category, Integer> {
	@Query("select c from TH_Category c where c.categoryID  IN :ids")
	List<TH_Category> findByIds(@Param("ids") List<Integer> ids);

}
