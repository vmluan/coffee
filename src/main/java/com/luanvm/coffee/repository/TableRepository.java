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
	
//	@Query("select c.tableID, c.customerName, c.tableAcr, c.tableNumber, sum(c.totalMoney), c.openTime, c.status"
//			+ " from TH_Table c where c.openTime  between :tradeDate and :nextTradeDate group by c.tableAcr order by c.status asc, c.openTime desc ")
//	List<Object[]> findTableByDate(@Param("tradeDate") Date tradeDate, @Param("nextTradeDate") Date nextTradeDate );
	
	@Query("select c"
			+ " from TH_Table c where c.openTime  between :tradeDate and :nextTradeDate group by c.tableAcr order by c.status asc, c.openTime desc ")
	List<TH_Table> findTableByDate(@Param("tradeDate") Date tradeDate, @Param("nextTradeDate") Date nextTradeDate );
	
	@Query("select c from TH_Table c where c.openTime  between :tradeDate and :nextTradeDate and c.status = :status order by c.status asc, c.openTime desc")
	List<TH_Table> findTableByDate(@Param("tradeDate") Date tradeDate, @Param("nextTradeDate") Date nextTradeDate, 
			@Param("status") TH_TableStatus status);
	@Query("select c from TH_Table c where c.openTime  between :tradeDate and :nextTradeDate and (c.status = :status1 or c.status =:status2) order by c.status asc, c.openTime desc")
	List<TH_Table> findTableByDate(@Param("tradeDate") Date tradeDate, @Param("nextTradeDate") Date nextTradeDate, 
			@Param("status1") TH_TableStatus status1, @Param("status2") TH_TableStatus status2);
	
	@Query("select c from TH_Table c where c.openTime  between :tradeDate and :nextTradeDate and c.tableNumber = :tableNumber order by  c.status asc, c.openTime desc")
	List<TH_Table> findTableBuyTableNumber(@Param("tableNumber") String tableNumber,@Param("tradeDate") Date tradeDate, @Param("nextTradeDate") Date nextTradeDate);
	
	@Query("select c from TH_Table c where c.openTime  between :tradeDate and :nextTradeDate and c.tableNumber = :tableNumber and c.status = :status order by c.openTime desc")
	List<TH_Table> findTableByTableNumberAndStatus(@Param("tableNumber") String tableNumber,@Param("tradeDate") Date tradeDate, @Param("nextTradeDate") Date nextTradeDate
			, @Param("status") TH_TableStatus status);
		
	@Query("select c from TH_Table c where c.openTime  between :tradeDate and :nextTradeDate and c.tableAcr = :tableAcr and c.status = :status order by c.openTime desc")
	List<TH_Table> findTableByTableAcrAndStatus(@Param("tableAcr") String tableAcr,@Param("tradeDate") Date tradeDate, @Param("nextTradeDate") Date nextTradeDate
			, @Param("status") TH_TableStatus status);	
}
