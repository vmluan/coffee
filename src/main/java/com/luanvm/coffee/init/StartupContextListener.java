package com.luanvm.coffee.init;

import java.util.List;

import javax.servlet.ServletContextEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ContextLoaderListener;

import com.luanvm.coffee.domain.Product;
import com.luanvm.coffee.domain.TH_Table;
import com.luanvm.coffee.helper.ProductHepler;
import com.luanvm.coffee.service.ProductService;
import com.luanvm.coffee.service.TableService;

@Component
public class StartupContextListener extends ContextLoaderListener{
	
	@Override
	public void contextInitialized(ServletContextEvent event){
		super.contextInitialized(event);
		System.out.println("=========================== application is on start up");
		ProductHepler.getProductSamples();
		String location = event.getServletContext().getRealPath("/");
		generateProductFiles(location);
	}
	private void generateProductFiles(String location){
		GenericXmlApplicationContext ctx = new GenericXmlApplicationContext();
		ctx.load("classpath:jpa-app-context.xml");
		ctx.refresh();
		
		System.out.println("App context initialized successfully");
		ProductService productService = ctx.getBean("productService", ProductService.class);
		
		
		List<Product> products = productService.findAll();
		ProductHepler productHepler = new ProductHepler();
		productHepler.generateProductJson(products, location);
		
		ctx.destroy();
	}
}
