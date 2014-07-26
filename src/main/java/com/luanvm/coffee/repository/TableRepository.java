package com.luanvm.coffee.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.luanvm.coffee.domain.TH_Table;


public interface TableRepository extends PagingAndSortingRepository<TH_Table, Integer> {
	

}
