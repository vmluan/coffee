package com.luanvm.coffee.web.controller;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.hibernate.StaleObjectStateException;
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
import org.springframework.web.bind.annotation.ResponseBody;

import com.luanvm.coffee.domain.Product;
import com.luanvm.coffee.domain.TH_Encounter;
import com.luanvm.coffee.domain.TH_Table;
import com.luanvm.coffee.domain.TH_TableStatus;
import com.luanvm.coffee.helper.Utilities;
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
	//@Transactional
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
			existingTable.setOpenTime(new Date()); // set Open Time when
													// ordering the first
													// records
			existingTable.setStatus(TH_TableStatus.DRINKING);
			tableService.save(existingTable);
		}else{
			table.setEncounters(null);
			tableService.save(table);
		}
			
		if (encounters != null){
			for (TH_Encounter encounter : encounters){
			
				System.out.println("===========" + encounter.getQuantity());
				//Product product = productService.findById(encounter.getProduct().getProductID());
				Product product = productService.findByName(encounter.getProduct().getProductName());
				totalMoney = (encounter.getQuantity() * product.getProductPrice());
				encounter.setProduct(product);
				encounter.setEncounterTime(new Date());
				encounter.setProductPrice(product.getProductPrice());
				if(existingTable != null)
					encounter.setTable(existingTable);
				else
					encounter.setTable(table);
				encounterService.save(encounter);
				
			}
			if (existingTable != null) {
				existingTable.setEncounters(encounters);
				//existingTable.setTotalMoney(existingTable.getTotalMoney() + totalMoney);
				existingTable.setTotalMoney(totalMoney + existingTable.getTotalMoney());
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
		httpServletRequest.setAttribute("tableAcr", table.getTableAcr());
		return "tables/update";
	}
	
	@Transactional
	@RequestMapping(value = "/{id}", params = "form", method = RequestMethod.POST)
	public String saveForm(@PathVariable("id") Integer id, Model ciModel
			,@RequestBody final  TH_Table table
			, HttpServletRequest httpServletRequest) {
		
		System.out.println("====================================== save form");

		boolean isValid = false;
		boolean re_update = true;
		while (re_update) {
			try {
				isValid = savingTable( table);
				re_update = false;
				;
			} catch (StaleObjectStateException e) {
				System.out
						.println("========== Row was updated or deleted by another transaction");
				re_update = true;
			} catch (Exception e) {
				re_update = true;
				System.out
						.println("=============== Exception when saving table");

			}
		}
		if(!isValid){ // do not allow updating paid table
					 // Should show error message
			httpServletRequest.setAttribute("table_is_paid", true);
			System.out.println("======= table is paid");
			return "tables/updateerror";
		}
		httpServletRequest.setAttribute("sysDate", new Date().getTime());
		return "tables/update";
	}
	private boolean savingTable(TH_Table table){
		TH_Table existingTable = tableService.findById(table.getTableID());
		if(existingTable != null && existingTable.getStatus() != TH_TableStatus.DRINKING ){
			return false;
		}
		List<TH_Encounter> encounters = table.getEncounters();
		//update encounters. Actually, we dont update encounters. we just create new encounters and set them for the table
		// what happen with old encounter records? it will a garbage. Need to fix it!!!!!
		
		long totalMoney = 0;
		if (encounters != null && existingTable != null){
			
			totalMoney = existingTable.getTotalMoney();
			for (TH_Encounter encounter : encounters){
			
				Product product = productService.findByName(encounter.getProduct().getProductName());
				totalMoney = totalMoney + (encounter.getQuantity() * product.getProductPrice());
				encounter.setProduct(product);
				encounter.setEncounterTime(new Date());
				encounter.setTable(existingTable);
				encounter.setProductPrice(product.getProductPrice());
				encounterService.save(encounter);
				
			}
			//update table first
			
			
			
			existingTable.setTableNumber(table.getTableNumber());
			existingTable.setEncounters(encounters);
			if(existingTable.getTotalMoney() == 0 || existingTable.getStatus() == null){
				existingTable.setOpenTime(new Date()); // set Open Time when
				// ordering the first drink
				existingTable.setStatus(TH_TableStatus.DRINKING);
				System.out.println("============ table acr = " + existingTable.getTableAcr() == null);
				if(existingTable.getTableAcr() == null)
					existingTable.setTableAcr(table.getTableAcr());
			}
			existingTable.setTotalMoney(totalMoney);
			if (table.getStatus() != null)
				existingTable.setStatus(table.getStatus());

		}
		existingTable.setCustomerName(table.getCustomerName());
		tableService.save(existingTable);
		return true;
		
	}
	
	@RequestMapping(value = "/{tableacr}", params = "tableacr", method = RequestMethod.GET)
	public String showTable(@PathVariable("tableacr") String tableNumber, Model uiModel, HttpServletRequest httpServletRequest) {
		
		List<TH_Table> tables = null;
		httpServletRequest.setAttribute("sysDate", new Date().getTime());
		
		String tableAcr = httpServletRequest.getParameter("tableacr") ;
		System.out.println("================= newtable = " + httpServletRequest.getParameter("newtable"));
		boolean isNewTable = Boolean.valueOf(httpServletRequest.getParameter("newtable"));
		
		System.err.println("========= tableAcr = " + tableAcr);
		
		if (!isNewTable)
			tables = tableService.findTableBuyTableNumber(tableNumber);
		
		if (tables != null && tables.size() > 0) {

			System.out.println("==================== new method: table id = "
					+ tables.get(0).getTableID());

//			List<TH_Encounter> encounters = tables.get(0).getEncounters();

			uiModel.addAttribute("table", tables.get(0));
			tableAcr = tables.get(0).getTableAcr();
			httpServletRequest.setAttribute("tableAcr", tableAcr);
		}
		else{ 
			//There is no order in this table today. start creating the first one
			//OR create another table.
			System.out.println("============== new table today = " + tableNumber);
			httpServletRequest.setAttribute("tableNumber", tableNumber);
			tableAcr = tableNumber.replace(" ",  "")+"_" + String.valueOf(new Date().getTime());
			httpServletRequest.setAttribute("tableAcr", tableAcr);
			
			//create an empty table. It is to avoid creating many tables when user double or triple click on drinks
			TH_Table newTable = new TH_Table();
			newTable.setTableNumber(tableNumber);
			newTable.setTableAcr(tableAcr);
			newTable.setOpenTime(new Date());
			newTable.setStatus(TH_TableStatus.DRINKING);
			uiModel.addAttribute("table", newTable);
			
			tableService.save(newTable);
			
			return "tables/new";
		}
		
		return "tables/update";
	}
	@RequestMapping(value = "/tablelist", method = RequestMethod.GET)
	public String getListTable(){
		return "tablelist";
	}
	@RequestMapping(value = "/tablelistjson", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public String getProductsJson(@RequestParam(value="filterscount", required=false) String filterscount
			, @RequestParam(value="groupscount", required=false) String groupscount
			, @RequestParam(value="pagenum", required=false) Integer pagenum
			, @RequestParam(value="pagesize", required=false) Integer pagesize
			, @RequestParam(value="recordstartindex", required=false) Integer recordstartindex
			, @RequestParam(value="recordendindex", required=false) Integer recordendindex
			, @RequestParam(value="startdate", required=true) String startDateString
			, @RequestParam(value="endDate", required=true) String endDateString){
		Date startDate = Utilities.parseDate(startDateString);
		Date endDate = Utilities.parseDate(endDateString);
		endDate = Utilities.getLastTimeOfDate(endDate);
		List<TH_Table> tables =  tableService.findTableByDateRange(startDate, endDate);
		
		String result = Utilities.jSonSerialization(tables);
		return result;
	}	

}
