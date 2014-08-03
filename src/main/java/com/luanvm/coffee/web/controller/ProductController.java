package com.luanvm.coffee.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.luanvm.coffee.domain.Product;
import com.luanvm.coffee.helper.ProductHepler;
import com.luanvm.coffee.service.ProductService;

@Controller
@RequestMapping(value = "/products")
public class ProductController {
	final Logger logger = LoggerFactory.getLogger(ProductController.class);

	@Autowired
	MessageSource messageSource;
	
	@Autowired
	private ProductService productService;

	@RequestMapping(method = RequestMethod.GET)
	public String getProductPage(Model ciModel,@RequestParam(value="lang", required=false)String id) {
		Locale locale = LocaleContextHolder.getLocale();
		
		if(StringUtils.isNotEmpty(id)){
			if(id.equalsIgnoreCase(com.luanvm.coffee.helper.Constants.VIETNAMESE))
				locale.setDefault(new Locale(id));
		}
		List<Product> products = productService.findAll();
		ciModel.addAttribute("products", products);
		return "products/list";
	}
	
	@RequestMapping(value = "/setjson")
	public String generateJsonFile(HttpServletRequest httpServletRequest){
		
		List<Product> products = productService.findAll();
		
		ProductHepler productHepler = new ProductHepler();
		String location = httpServletRequest.getSession().getServletContext().getRealPath("/");
		productHepler.generateProductJson(products, location);
		return "products/list"; 
	}
	

}
