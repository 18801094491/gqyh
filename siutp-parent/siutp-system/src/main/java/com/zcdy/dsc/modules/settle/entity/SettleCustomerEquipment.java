package com.zcdy.dsc.modules.settle.entity;

import org.jeecgframework.poi.excel.annotation.Excel;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 描述: 用户水表关联表
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-01-08
 * 版本号: V1.0
 */
@Data
@TableName("settle_customer_equipment")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="settle_customer_equipment对象", description="用户水表关联表")
public class SettleCustomerEquipment {
    
	/**id*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**customerId*/
	@Excel(name = "customerId", width = 15)
    @ApiModelProperty(value = "customerId")
	private java.lang.String customerId;
	/**equipmentId*/
	@Excel(name = "equipmentId", width = 15)
    @ApiModelProperty(value = "equipmentId")
	private java.lang.String equipmentId;
	/**equipmentSn*/
	@Excel(name = "equipmentSn", width = 15)
    @ApiModelProperty(value = "equipmentSn")
	private java.lang.String equipmentSn;
	/**bindStatus*/
	@Excel(name = "bindStatus", width = 2)
	@ApiModelProperty(value = "bindStatus")
	private java.lang.String bindStatus;
	/**bindTime*/
	@Excel(name = "bindTime", width = 2)
	@ApiModelProperty(value = "bindTime")
	private java.lang.String bindTime;
	/**unbindTime*/
	@Excel(name = "unbindTime", width = 2)
	@ApiModelProperty(value = "unbindTime")
	private java.lang.String unbindTime;

}
