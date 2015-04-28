/**
 * Copyright (c) 2013 THKAssociates,Inc. All rights reserved.
 * Filename   : ContactServiceTest.java
 * Description: 
 * @author    : Khanh.Pham
 * Created    : Sep 3, 2013
 */
package com.luanvm.coffee;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.context.support.GenericXmlApplicationContext;

import com.luanvm.coffee.domain.Product;
import com.luanvm.coffee.domain.TH_Category;
import com.luanvm.coffee.domain.TH_Table;
import com.luanvm.coffee.helper.Utilities;
import com.luanvm.coffee.service.CategoryService;
import com.luanvm.coffee.service.ProductService;
import com.luanvm.coffee.service.TableService;


public class ContactServiceTest {

	/**
	 * @param args
	 * @throws ParseException 
	 */
	private void testCategory(CategoryService categoryService, String categoryName){
		TH_Category category = new TH_Category();
		category.setCategoryName(categoryName);
		categoryService.save(category);
		
	}
	private void testProductCategory(ProductService productService, CategoryService categoryService){
		Product product = productService.findById(1);
		TH_Category category = categoryService.findById(1);
		ArrayList<TH_Category> categories = new ArrayList<TH_Category>();
		categories.add(category);
		product.setCategories(categories);
		
		productService.save(product);
	}
	
	public static void main(String[] args) throws ParseException {

		GenericXmlApplicationContext ctx = new GenericXmlApplicationContext();
		ctx.load("classpath:jpa-app-context.xml");
		ctx.refresh();
		
		System.out.println("App context initialized successfully");
		
		ProductService productService = ctx.getBean("productService", ProductService.class);
		TableService tableService = ctx.getBean("tableService", TableService.class);
		CategoryService categoryService = ctx.getBean("categoryService", CategoryService.class);
//		List<Product> products = productService.findAll();
		
//		String result = Utilities.jSonSerialization(products);
//		System.out.println(result);
//		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
//		
//		Date startDate = format.parse("08/01/2015");
//		Date endDate = format.parse("09/01/2015");
//		List<TH_Table> tables =  tableService.findTableByDateRange(startDate, endDate);
//		for(TH_Table table : tables){
//			table.setEncounters(null);
//		}
//		//exclude from json serialization
//		ObjectMapper objMapper = new ObjectMapper();
//		String jsonString = Utilities.jSonSerialization(tables);
//
//		System.out.println(jsonString);
//		Product product= new Product();
//		product.setProductName("cafe den");
//		product.setProductPrice(10000);
//		
//		productService.save(product);
//		ContactServiceTest test = new ContactServiceTest();
//		test.testCategory(categoryService, "Nuoc giai khat");
//		test.testProductCategory(productService, categoryService);
//		Product product = productService.findById(1);
//		System.out.println(product);
		
//		TH_Category category = categoryService.findById(1);
		
		
//		List<Integer> ids = new ArrayList<Integer>();
//		ids.add(1);
//		ids.add(2);
//		ids.add(3);
//		List<Product> products = new ArrayList<Product>();
//		List<TH_Category> categories = categoryService.findByIds(ids);
//		for (TH_Category category : categories){
//			List<Product> list = category.getProducts();
//			for(Product product : list){
//				if(!products.contains(product))
//					products.add(product);
//			}
//		}
//		System.out.println(products);
//		System.out.println(category.getProducts());
		List<TH_Table> tables;
		tables = tableService.findAll();
		Iterator<TH_Table> iter = tables.iterator();
		while(iter.hasNext()){
			TH_Table table = iter.next();
			table.setEncounters(null);
			if(table.getTotalMoney() ==0)
				iter.remove();
		}
		System.out.println(tables);
		String result = Utilities.jSonSerialization(tables);
		System.out.println(result);
	}

}
