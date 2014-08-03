package com.luanvm.coffee.helper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import com.luanvm.coffee.domain.Product;

public class ProductHepler {
	
	private static String productSamples;
	
	
	
	public void generateProductJson(List<Product> products, String location){
		 BufferedWriter writer = null;
			
		   try {
	            //create a js file
	            String fileName = "products.js";
	            File logFile = new File(location + fileName);

	            // This will output the full path where the file will be written to...
	            System.out.println("==============================luan");
	            System.out.println(logFile.getCanonicalPath());

	            writer = new BufferedWriter(new FileWriter(logFile));
	            
	            
	            
	            String productString = products.toString();
	            
	           productString = '{' + productString.substring(1, productString.length() -1)
	        		   			+ '}';
	           
	           String outputString = "var sampleProducts =" +productString;
	            writer.write(outputString);
	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	            try {
	                // Close the writer regardless of what happens...
	                writer.close();
	            } catch (Exception e) {
	            }
	        }
		   
		   
	}



	public static String getProductSamples() {
		return productSamples;
	}



	public static void setProductSamples(String productSamples) {
		ProductHepler.productSamples = productSamples;
	}
	
	
	public void generateProductSamples(List<Product> products)
	{
        String productString = products.toString();
       productString = '{' + productString.substring(1, productString.length() -1)
    		   			+ '}';
       this.productSamples = "var sampleProducts =" +productString;
	}
	
	

}
