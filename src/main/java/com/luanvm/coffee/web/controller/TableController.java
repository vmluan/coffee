package com.luanvm.coffee.web.controller;

import java.util.Date;
import java.util.Locale;
import java.util.Set;

import javax.xml.ws.Response;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.itextpdf.text.pdf.hyphenation.TernaryTree.Iterator;
import com.luanvm.coffee.domain.Product;
import com.luanvm.coffee.domain.TH_Encounter;
import com.luanvm.coffee.domain.TH_Table;
import com.luanvm.coffee.domain.TH_TableStatus;
import com.luanvm.coffee.repository.TableRepository;
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
	public String getProductPage(Model ciModel,@RequestParam(value="lang", required=false)String id) {
		Locale locale = LocaleContextHolder.getLocale();
		
		if(StringUtils.isNotEmpty(id)){
			if(id.equalsIgnoreCase(com.luanvm.coffee.helper.Constants.VIETNAMESE))
				locale.setDefault(new Locale(id));
		}
		return "tables/new";
	}
	
	@Transactional
	@RequestMapping(params = "form", method = RequestMethod.POST)
	public String saveTable(Model ciModel,@RequestBody final  TH_Table table) {
		Locale locale = LocaleContextHolder.getLocale();
		System.out.println("===========================================");
		Set<TH_Encounter> encounters = table.getEncounters();
		if (encounters != null){
			for (TH_Encounter encounter : encounters){
			
				System.out.println("===========" + encounter.getQuantity());
				Product product = productService.findById(encounter.getProduct().getProductID());
				encounter.setProduct(product);
				encounter.setEncounterTime(new Date());
				encounterService.save(encounter);
				
			}
			table.setEncounters(encounters);
			table.setOpenTime(new Date());
			table.setStatus(TH_TableStatus.DRINKING);
			
			tableService.save(table);
		}
		return "tables/new";
	}	
	

}
