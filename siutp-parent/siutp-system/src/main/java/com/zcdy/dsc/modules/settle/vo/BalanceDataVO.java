package com.zcdy.dsc.modules.settle.vo;

import java.util.Date;

import lombok.ToString;

/**
 * @author: Roberto
 * 创建时间:2020年4月26日 下午2:41:57
 * 描述: <p></p>
 */
@ToString
public class BalanceDataVO {

	private String id;
	
	private String equipmentSection;
	
	private String equipmentLocation;
	
	private String equipmentSn;
	
	private String balanceRoute;
	
	private String amount;
	
	private String contractSn;
	
	private String contractName;
	
	private String customerName;
	
	private Date balanceDate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEquipmentSection() {
		return equipmentSection;
	}

	public void setEquipmentSection(String equipmentSection) {
		this.equipmentSection = equipmentSection;
	}

	public String getEquipmentLocation() {
		return equipmentLocation;
	}

	public void setEquipmentLocation(String equipmentLocation) {
		this.equipmentLocation = equipmentLocation;
	}

	public String getEquipmentSn() {
		return equipmentSn;
	}

	public void setEquipmentSn(String equipmentSn) {
		this.equipmentSn = equipmentSn;
	}

	public String getBalanceRoute() {
		return balanceRoute;
	}

	public void setBalanceRoute(String balanceRoute) {
		this.balanceRoute = balanceRoute;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getContractSn() {
		return contractSn;
	}

	public void setContractSn(String contractSn) {
		this.contractSn = contractSn;
	}

	public String getContractName() {
		return contractName;
	}

	public void setContractName(String contractName) {
		this.contractName = contractName;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Date getBalanceDate() {
		return balanceDate;
	}

	public void setBalanceDate(Date balanceDate) {
		this.balanceDate = balanceDate;
	}
}
