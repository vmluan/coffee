package com.luanvm.coffee.web.controller;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.luanvm.coffee.domain.Product;
import com.luanvm.coffee.domain.TH_Encounter;
import com.luanvm.coffee.domain.TH_Table;
import com.luanvm.coffee.domain.TH_TableStatus;
import com.luanvm.coffee.service.EncounterService;
import com.luanvm.coffee.service.ProductService;
import com.luanvm.coffee.service.TableService;

@Controller
@RequestMapping(value = "/quanlyban")
public class TableController {
	final Logger logger = LoggerFactory.getLogger(TableController.class);

	@Autowired
	MessageSource messageSource;
	
	@Autowired
	private TableService tableService;
	
	@Autowired
	private EncounterService encounterService;
	
	@Autowired
	private ProductService productService;
	
	@RequestMapping(method = RequestMethod.GET)
	public String getProductPage(Model ciModel,@RequestParam(value="lang", required=false)String id,
			HttpServletRequest httpServletRequest) {
		Locale locale = LocaleContextHolder.getLocale();
		
		if(StringUtils.isNotEmpty(id)){
			if(id.equalsIgnoreCase(com.luanvm.coffee.helper.Constants.VIETNAMESE))
				locale.setDefault(new Locale(id));
		}
		String tableid = httpServletRequest.getParameter("tableid");
		if(tableid != null)
			httpServletRequest.setAttribute("tableNumber", tableid);
		System.out.println("============== table id = " + tableid);
		httpServletRequest.setAttribute("sysDate", new Date().getTime());
		return "tables/new";
	}
	//create new table with encounters
	@Transactional
	@RequestMapping(params = "form", method = RequestMethod.POST)
	public String saveTable(Model ciModel,@RequestBody final  TH_Table table,
			HttpServletRequest httpServletRequest) {
		Locale locale = LocaleContextHolder.getLocale();
		System.out.println("===========================================");
		List<TH_Encounter> encounters = table.getEncounters();
		long totalMoney = 0;
		
		// because i have changed the GUI to submit every order when user clicks on every drink.
		// we need to update that encounter to current existing table that is opening.
		String tableNumber = table.getTableNumber();
		TH_Table existingTable = null;
		List<TH_Table> existingTables = tableService.findOpeningTableByTableNumber(tableNumber);
		if (existingTables != null && existingTables.size() >0){
			existingTable = existingTables.get(0);
		}
		if (encounters != null){
			for (TH_Encounter encounter : encounters){
			
				System.out.println("===========" + encounter.getQuantity());
				//Product product = productService.findById(encounter.getProduct().getProductID());
				Product product = productService.findByName(encounter.getProduct().getProductName());
				totalMoney = totalMoney + (encounter.getQuantity() * product.getProductPrice());
				encounter.setProduct(product);
				encounter.setEncounterTime(new Date());
				encounterService.save(encounter);
				
			}
			if (existingTable != null) {
				existingTable.setEncounters(encounters);
				existingTable.setTotalMoney(existingTable.getTotalMoney() + totalMoney);
				tableService.save(existingTable);
			} else {
				table.setEncounters(encounters);
				table.setOpenTime(new Date());
				table.setStatus(TH_TableStatus.DRINKING);
				table.setTotalMoney(totalMoney);
				httpServletRequest
						.setAttribute("sysDate", new Date().getTime());
				tableService.save(table);
			}
		}
		return "tables/new";
	}
	// update

	@RequestMapping(value = "/{id}", params = "form", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Integer id, Model uiModel, HttpServletRequest httpServletRequest) {
		
		TH_Table table = tableService.findById(id);
		
		
		List<TH_Encounter> encounters = table.getEncounters();
		
		uiModel.addAttribute("table", table);
		uiModel.addAttribute("encounters", encounters);
		httpServletRequest.setAttribute("sysDate", new Date().getTime());
		return "tables/update";
	}
	@Transactional
	@RequestMapping(value = "/{id}", params = "form", method = RequestMethod.POST)
	public String saveForm(@PathVariable("id") Integer id, Model ciModel
			,@RequestBody final  TH_Table table
			, HttpServletRequest httpServletRequest) {
		
		System.out.println("====================================== save form");
		
		//we need to update table and its encounters
		

		
		
		
		List<TH_Encounter> encounters = table.getEncounters();
		//update encounters. Actually, we dont update encounters. we just create new encounters and set them for the table
		// what happen with old encounter records? it will a garbage. Need to fix it!!!!!
		
		long totalMoney = 0;
		if (encounters != null){
			for (TH_Encounter encounter : encounters){
			
				Product product = productService.findByName(encounter.getProduct().getProductName());
				totalMoney = totalMoney + (encounter.getQuantity() * product.getProductPrice());
				encounter.setProduct(product);
				encounter.setEncounterTime(new Date());
				encounterService.save(encounter);
				
			}
			//update table first
			TH_Table existingTable = tableService.findById(table.getTableID());
			
			existingTable.setCustomerName(table.getCustomerName());
			existingTable.setTableNumber(table.getTableNumber());
			existingTable.setEncounters(encounters);
			existingTable.setTotalMoney(totalMoney);
			if (table.getStatus() != null)
				existingTable.setStatus(table.getStatus());
			
			tableService.save(existingTable);
		
		}
		
		httpServletRequest.setAttribute("sysDate", new Date().getTime());
		return "tables/update";
	}
	
	@RequestMapping(value = "/{tablenumber}", params = "tablenumber", method = RequestMethod.GET)
	public String showTable(@PathVariable("tablenumber") String tableNumber, Model uiModel, HttpServletRequest httpServletRequest) {
		
		List<TH_Table> table = tableService.findTableBuyTableNumber(tableNumber);
		httpServletRequest.setAttribute("sysDate", new Date().getTime());
		if (table != null && table.size() > 0) {

			System.out.println("==================== new method: table id = "
					+ table.get(0).getTableID());

			List<TH_Encounter> encounters = table.get(0).getEncounters();

			uiModel.addAttribute("table", table.get(0));
			uiModel.addAttribute("encounters", encounters);
		}
		else{ //There is no order in this table today. start creating the first one
			System.out.println("============== new table today = " + tableNumber);
			httpServletRequest.setAttribute("tableNumber", tableNumber);
			return "tables/new";
		}
		
		return "tables/update";
	}

}
