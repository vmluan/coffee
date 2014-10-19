package com.luanvm.coffee.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Index;

@Entity
@Table(name = "th_table", catalog = "coffee")
public class TH_Table implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "tableid", nullable = false)
	private Integer tableID;
	
	@Column(name = "customername")
	String customerName;
	
	//sample value is ban1201410191234
	@Index(name = "tableacr")
	private String tableAcr;
	
	@Column(name = "tablenumber")
	private String tableNumber;
	
	@Column(name = "totalmoney")
	private long totalMoney;
	
	@Column(name = "opentime")
	private Date openTime;
	
	@Column(name = "closedtime")
	private Date closedTime;
	
	@Column(name = "status")
	private TH_TableStatus status;
	
	

	public String getTableAcr() {
		return tableAcr;
	}

	public void setTableAcr(String tableAcr) {
		this.tableAcr = tableAcr;
	}

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "table_encounter", joinColumns = { @JoinColumn(name = "tableid") }
		, inverseJoinColumns = { @JoinColumn(name = "encounterid") })
	List<TH_Encounter> encounters; //

	public Integer getTableID() {
		return tableID;
	}

	public void setTableID(Integer tableID) {
		this.tableID = tableID;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}


	public List<TH_Encounter> getEncounters() {
		return encounters;
	}

	public void setEncounters(List<TH_Encounter> encounters) {
		this.encounters = encounters;
	}

	public String getTableNumber() {
		return tableNumber;
	}

	public void setTableNumber(String tableNumber) {
		this.tableNumber = tableNumber;
	}

	public long getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(long totalMoney) {
		this.totalMoney = totalMoney;
	}

	public Date getOpenTime() {
		return openTime;
	}

	public void setOpenTime(Date openTime) {
		this.openTime = openTime;
	}

	public Date getClosedTime() {
		return closedTime;
	}

	public void setClosedTime(Date closedTime) {
		this.closedTime = closedTime;
	}

	public TH_TableStatus getStatus() {
		return status;
	}

	public void setStatus(TH_TableStatus status) {
		this.status = status;
	}
	
	
	

}
