package com.zcdy.dsc.modules.operation.equipment.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * 描述： 水表结算记录
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-04-24
 * 版本： V1.0
 */
@Data
@TableName("meter_balance")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="meter_balance", description="水表结算记录")
public class MeterBalance {
    
	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	
	/**合同id*/
	@Excel(name = "合同id", width = 15)
    @ApiModelProperty(value = "合同id")
	private java.lang.String contractId;
	
	/**客户信息id*/
	@Excel(name = "客户信息id", width = 15)
    @ApiModelProperty(value = "客户信息id")
	private java.lang.String customerId;
	
	/**资产设备id*/
	@Excel(name = "资产设备id", width = 15)
    @ApiModelProperty(value = "资产设备id")
	private java.lang.String equipmentId;
	
	/**结算日期*/
	@Excel(name = "结算日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "结算日期")
	private java.util.Date balanceDate;
	
	/**计算时间*/
	@Excel(name = "计算时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "计算时间")
	private java.util.Date balanceTime;
	
	/**结算渠道，0-系统自动结算，1-人工结算*/
	@Excel(name = "结算渠道，0-系统自动结算，1-人工结算", width = 15)
    @ApiModelProperty(value = "结算渠道，0-系统自动结算，1-人工结算")
	private java.lang.String balanceRoute;
	
	/**如果是人工结算，结算员用户id*/
	@Excel(name = "如果是人工结算，结算员用户id", width = 15)
    @ApiModelProperty(value = "如果是人工结算，结算员用户id")
	private java.lang.String balanceUser;
	
	/**结算总额，单位分*/
	@Excel(name = "结算总额，单位分", width = 15)
    @ApiModelProperty(value = "结算总额，单位分")
	private java.lang.String amount;
	
}
