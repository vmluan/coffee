package com.luanvm.coffee.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.luanvm.coffee.domain.TH_Table;

public interface TableService {

	List<TH_Table> findAll();
	
	TH_Table findById(Integer id);
	
	TH_Table save(TH_Table TH_Product);
	
	Page<TH_Table> findAllByPage(Pageable pageable);	
	
}
