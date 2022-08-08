package com.zcdy.dsc.modules.settle.vo;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.util.StringUtils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.ToString;

/**
 * @author: Roberto
 * 创建时间:2020年4月26日 下午2:41:57
 * 描述: <p>已结算表底记录信息</p>
 */
@ToString
@ApiModel(value="com.zcdy.dsc.modules.settle.vo.MeterCustomerRecordVO", description="已结算表底记录信息")
public class MeterCustomerRecordVO {

	//结算记录id
	@ApiModelProperty("表底记录id")
	private String id;
	
	@ApiModelProperty("所属标段")
	private String equipmentSection;
	
	@ApiModelProperty("放置位置")
	private String equipmentLocation;
	
	@ApiModelProperty("资产编号")
	private String equipmentSn;
	
	//总金额
	@ApiModelProperty("总金额")
	private String amount;
	
	//水价
	@ApiModelProperty("水价")
	private String price;
	
	//用量
	@ApiModelProperty("用量")
	private String flowAmount;
	
	@ApiModelProperty("客户名称")
	private String customerName;
	
	//抄表日期
	@ApiModelProperty("抄表日期")
	private Date recordTime;
	
	//结算日期
	@ApiModelProperty("结算日期")
	private Date balanceTime;
	
	//水表（客户）区域
	@ApiModelProperty("水表（客户）区域")
	private String districtTitle;

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

	public String getAmount() {
		if(StringUtils.isEmpty(this.price) || StringUtils.isEmpty(this.flowAmount)){
			this.amount = "--";
		}else{
			BigDecimal bgPrice = new BigDecimal(this.price);
			BigDecimal bgFa = new BigDecimal(this.flowAmount);
			this.amount = bgPrice.multiply(bgFa).setScale(2).toEngineeringString();
		}
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getFlowAmount() {
		return flowAmount;
	}

	public void setFlowAmount(String flowAmount) {
		this.flowAmount = flowAmount;
	}

	public Date getRecordTime() {
		return recordTime;
	}

	public void setRecordTime(Date recordTime) {
		this.recordTime = recordTime;
	}

	public Date getBalanceTime() {
		return balanceTime;
	}

	public void setBalanceTime(Date balanceTime) {
		this.balanceTime = balanceTime;
	}

	public String getDistrictTitle() {
		return districtTitle;
	}

	public void setDistrictTitle(String districtTitle) {
		this.districtTitle = districtTitle;
	}
}
