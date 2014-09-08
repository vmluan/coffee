package com.luanvm.coffee.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.luanvm.coffee.domain.Product;
import com.luanvm.coffee.domain.TH_Table;
import com.luanvm.coffee.domain.TH_TableStatus;


public interface TableRepository extends PagingAndSortingRepository<TH_Table, Integer> {
	
	@Query("select c from TH_Table c where c.openTime  between :tradeDate and :nextTradeDate ")
	List<TH_Table> findProductByName(@Param("tradeDate") Date tradeDate, @Param("nextTradeDate") Date nextTradeDate );
	
	@Query("select c from TH_Table c where c.openTime  between :tradeDate and :nextTradeDate and c.status = :status")
	List<TH_Table> findProductByName(@Param("tradeDate") Date tradeDate, @Param("nextTradeDate") Date nextTradeDate, 
			@Param("status") TH_TableStatus status);
	@Query("select c from TH_Table c where c.openTime  between :tradeDate and :nextTradeDate and (c.status = :status1 or c.status =:status2)")
	List<TH_Table> findProductByName(@Param("tradeDate") Date tradeDate, @Param("nextTradeDate") Date nextTradeDate, 
			@Param("status1") TH_TableStatus status1, @Param("status2") TH_TableStatus status2);
	
}
