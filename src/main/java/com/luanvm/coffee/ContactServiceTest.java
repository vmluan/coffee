/**
 * Copyright (c) 2013 THKAssociates,Inc. All rights reserved.
 * Filename   : ContactServiceTest.java
 * Description: 
 * @author    : Khanh.Pham
 * Created    : Sep 3, 2013
 */
package com.luanvm.coffee;

import java.util.List;

import org.springframework.context.support.GenericXmlApplicationContext;

import com.luanvm.coffee.domain.TH_Table;
import com.luanvm.coffee.service.TableService;


public class ContactServiceTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		GenericXmlApplicationContext ctx = new GenericXmlApplicationContext();
		ctx.load("classpath:jpa-app-context.xml");
		ctx.refresh();
		
		System.out.println("App context initialized successfully");
		
		TableService tableService = ctx.getBean("tableService", TableService.class);
		
		List<TH_Table> tables = tableService.findAll();
		
		for (TH_Table table: tables) {
			System.out.println(table);
		}

	}

}
