package com.luanvm.coffee.web.controller;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.luanvm.coffee.domain.Product;
import com.luanvm.coffee.helper.ProductHepler;
import com.luanvm.coffee.helper.Utilities;
import com.luanvm.coffee.service.ProductService;
import com.luanvm.coffee.web.util.UrlUtil;

@Controller
@RequestMapping(value = "/products")
public class ProductController {
	final Logger logger = LoggerFactory.getLogger(ProductController.class);
	final String UPLOAD_DIRECTORY = "/images/t-shirts/";
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
		
//		List<Product> products = productService.findAll();
//		
//		ProductHepler productHepler = new ProductHepler();
//		String location = httpServletRequest.getSession().getServletContext().getRealPath("/");
//		productHepler.generateProductJson(products, location);
		generateProductFiles(httpServletRequest);
		return "redirect:/products"; 
	}
	
	@RequestMapping(params = "form", method = RequestMethod.GET)
	public String createNewProduct(Model uiModel){
		
		System.out.println("============================== new product");
		Product product = new Product();
		product.setFile(null);
		uiModel.addAttribute("product", product);
		
		List<Product> products =  productService.findAll();
		
		uiModel.addAttribute("products", products);
		
		return "products/new";
	}
	
	@RequestMapping(params = "form", method = RequestMethod.POST)
	public String saveNewProduct(@Valid Product product, BindingResult bindingResult,
			Model uiModel, HttpServletRequest httpServletRequest,
			RedirectAttributes redirectAttributes, Locale locale){
		if (bindingResult.hasErrors()) {
			uiModel.addAttribute("product", product);
			return "products/new";
		}
		
		
        //handle attachments
        String fileName = "";
        fileName = saveImages(httpServletRequest);
		
		product.setPicLocation(fileName);
		redirectAttributes.addFlashAttribute(
				"message","Them SP thanh cong");
		
		
		
		
		productService.save(product);
		generateProductFiles(httpServletRequest);
		uiModel.asMap().clear();
		return "redirect:products?form";
	}
	@RequestMapping(value = "/getproductsjson", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public String getProductsJson(@RequestParam(value="filterscount", required=false) String filterscount
			, @RequestParam(value="groupscount", required=false) String groupscount
			, @RequestParam(value="pagenum", required=false) Integer pagenum
			, @RequestParam(value="pagesize", required=false) Integer pagesize
			, @RequestParam(value="recordstartindex", required=false) Integer recordstartindex
			, @RequestParam(value="recordendindex", required=false) Integer recordendindex){
		List<Product> products =  productService.findAll();
		
		String result = Utilities.jSonSerialization(products);
		return result;
	}
	@RequestMapping(value = "/{id}", params = "form", method = RequestMethod.GET)
    public String updateForm(@PathVariable("id") Integer id, Model uiModel) {
       // uiModel.addAttribute("template", templateService.findById(id));
		Product product = productService.findById(id);
		uiModel.addAttribute("product", product);
        return "products/update";
	}
	@RequestMapping(value = "/{id}", params = "form", method = RequestMethod.POST)
	@Transactional
    public String update(@Valid Product product, @PathVariable Integer id, Model uiModel, 
    		HttpServletRequest httpServletRequest, RedirectAttributes redirectAttributes, Locale locale ) {
		logger.info("Updating product");
        uiModel.asMap().clear();
        product.setProductID(id);
        
        redirectAttributes.addFlashAttribute("message", "Updating Product");        

        //handle attachments
        String fileName = "";
        fileName = saveImages(httpServletRequest);
        product.setPicLocation(fileName);
        if (!fileName.equalsIgnoreCase(""))
        	productService.save(product);
        
		List<Product> products = productService.findAll();
		uiModel.addAttribute("products", products);
       return "redirect:/products";

    }
	@ResponseBody
	@RequestMapping(value = "/{id}", params = "delete", method = RequestMethod.GET)
    public String deleteProduct(@PathVariable("id") String id, Model uiModel) {
       // uiModel.addAttribute("template", templateService.findById(id));
		System.out.println("================== " + id);
		Product product = productService.findById(Integer.valueOf(id));
		productService.deleteProduct(product);
		List<Product> products = productService.findAll();
		uiModel.addAttribute("products", products);
		
		return "success";
       
	}
	private void generateProductFiles(HttpServletRequest request){
		List<Product> products = productService.findAll();
		ProductHepler productHepler = new ProductHepler();
		String location = request.getSession().getServletContext().getRealPath("/");
		productHepler.generateProductJson(products, location);
	}
	private String saveImages(HttpServletRequest httpServletRequest){
		
        MultiValueMap<String, MultipartFile> fileMap = ((MultipartHttpServletRequest)httpServletRequest).getMultiFileMap();
        Iterator<String> fileNameIterator = fileMap.keySet().iterator();
        String fileName = "";
        while(fileNameIterator.hasNext()) {
        	fileName = fileNameIterator.next();
        }
        System.out.println("======== file name is " + fileName);
		MultipartFile file =  ((MultipartHttpServletRequest)httpServletRequest).getFile(fileName);
		
		String mime_type = file.getContentType();
		
		System.out.println("======================= file type is " + mime_type);
		
		fileName = file.getOriginalFilename();
		String filePath = httpServletRequest.getSession().getServletContext().getRealPath("/");
		String resultFile = filePath +  UPLOAD_DIRECTORY + fileName;
		
		File multipartFile = new File(resultFile);
		
		try {
			file.transferTo(multipartFile);
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fileName;
	}
}
