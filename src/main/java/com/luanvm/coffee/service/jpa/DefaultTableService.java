
package com.luanvm.coffee.service.jpa;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.luanvm.coffee.domain.TH_Table;
import com.luanvm.coffee.domain.TH_TableStatus;
import com.luanvm.coffee.repository.TableRepository;
import com.luanvm.coffee.service.TableService;

@Service("tableService")
@Repository
@Transactional
public class DefaultTableService implements TableService {

	@Autowired
	private TableRepository tableRepository;

	@Transactional(readOnly=true)
	public List<TH_Table> findAll() {
		return Lists.newArrayList(tableRepository.findAll());
	}

	@Transactional(readOnly=true)
	public TH_Table findById(Integer id) {
		return tableRepository.findOne(id);
	}

	public TH_Table save(TH_Table th_table) {
		return tableRepository.save(th_table);
	}

	@Transactional(readOnly=true)
	public Page<TH_Table> findAllByPage(Pageable pageable) {
		return tableRepository.findAll(pageable);
	}

	@Override
	public List<TH_Table> findTableByDate(Date date) {
		  final SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		  final Calendar calendar = Calendar.getInstance();
		  calendar.setTime(date);
		  calendar.add(Calendar.DAY_OF_YEAR, 1);
		  Date nextDate =  calendar.getTime();
		  
		return tableRepository.findProductByName(date, nextDate);
	}

	@Override
	public List<TH_Table> findTableByDate(Date date, TH_TableStatus status) {
		  final SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		  final Calendar calendar = Calendar.getInstance();
		  calendar.setTime(date);
		  calendar.add(Calendar.DAY_OF_YEAR, 1);
		  Date nextDate =  calendar.getTime();
		  
		return tableRepository.findProductByName(date, nextDate, status);
	}

	@Override
	public List<TH_Table> findTableByDate(Date date, TH_TableStatus status1,
			TH_TableStatus status2) {
		  final SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		  final Calendar calendar = Calendar.getInstance();
		  calendar.setTime(date);
		  calendar.add(Calendar.DAY_OF_YEAR, 1);
		  Date nextDate =  calendar.getTime();
		  
		return tableRepository.findProductByName(date, nextDate, status1, status2);
	}
	
	
}
