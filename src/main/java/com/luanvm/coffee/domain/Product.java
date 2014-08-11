package com.luanvm.coffee.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.web.multipart.MultipartFile;
@Entity
@Table(name = "th_product", catalog = "coffee")
public class Product implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "productid", nullable = false)
	private Integer productID;
	
	@Column(name = "productname", unique=true)
	private String productName;
	
	@Column(name = "price")
	private long productPrice;
	
	@Column(name = "piclocation")
	private String picLocation;
	
	@Column(name = "iscommon")
	private boolean isCommon;
	
	@Transient
	MultipartFile file;
	
	
	

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	public Integer getProductID() {
		return productID;
	}

	public void setProductID(Integer productID) {
		this.productID = productID;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public long getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(long productPrice) {
		this.productPrice = productPrice;
	}

	public String getPicLocation() {
		return picLocation;
	}

	public void setPicLocation(String picLocation) {
		this.picLocation = picLocation;
	}

	public boolean isCommon() {
		return isCommon;
	}

	public void setCommon(boolean isCommon) {
		this.isCommon = isCommon;
	}
	


	@Override
	public String toString(){
		return "'" + productName + "':" + "{"
				+ "pic: " +"'" + picLocation + "'"
				+","
				+ "price: " + String.valueOf(productPrice)
				+ "}";
	}
}
