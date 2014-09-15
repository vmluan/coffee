package com.luanvm.coffee.domain;

import java.io.Serializable;
import java.util.Date;

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
	private boolean common = false;
	
	@Transient
	MultipartFile file;
	
	@Transient
	String productPriceWrapper;
	
	@Column(name="isdeleted")
	private boolean isDeleted;
	
	@Column(name="lastupdated")
	private Date lastUpdated;
	
	@Column(name ="deleteddate")
	private Date deltedDate;
	
	

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public Date getDeltedDate() {
		return deltedDate;
	}

	public void setDeltedDate(Date deltedDate) {
		this.deltedDate = deltedDate;
	}

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
		return common;
	}

	public void setCommon(boolean common) {
		this.common = common;
	}

	
	public String getProductPriceWrapper() {
		return productPriceWrapper;
	}

	public void setProductPriceWrapper(String productPriceWrapper) {
		this.productPriceWrapper = productPriceWrapper;
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
