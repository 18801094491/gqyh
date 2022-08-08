package com.zcdy.dsc.modules.settle.entity;

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
 * @Description: 合同水价规则
 * @Author: 智能无人硬核心项目组
 * @Date:   2020-04-22
 * @Version: V1.0
 */
@Data
@TableName("settle_contract_water_rule")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="settle_contract_water_rule", description="合同水价规则")
public class ContractWaterRule {
    
	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "规则id")
	private java.lang.String id;
	/**合同id*/
	@Excel(name = "合同id", width = 15)
    @ApiModelProperty(value = "合同id")
	private java.lang.String contractId;
	/**规则名称*/
	@Excel(name = "规则名称", width = 15)
    @ApiModelProperty(value = "规则名称")
	private java.lang.String ruleName;
	/**基准水价*/
	@Excel(name = "基准水价", width = 15)
    @ApiModelProperty(value = "基准水价")
	private java.lang.String benchPrice;
	/**规则类型  1-阶梯计价 2-手工计价*/
	@Excel(name = "规则类型  1-阶梯计价 2-手工计价", width = 15)
    @ApiModelProperty(value = "规则类型  1-阶梯计价 2-手工计价")
	private java.lang.String ruleType;
	/**阶梯增幅*/
	@Excel(name = "阶梯增幅", width = 15)
    @ApiModelProperty(value = "阶梯增幅")
	private java.lang.String setUp;
	/**阶梯时间*/
	@Excel(name = "阶梯时间(月)", width = 15)
    @ApiModelProperty(value = "阶梯时间(月)")
	private java.lang.String setTime;
	/**创建人，派单人*/
	@Excel(name = "创建人，派单人", width = 15)
    @ApiModelProperty(value = "创建人，派单人")
	private java.lang.String createBy;
	/**创建时间*/
	@Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
	private java.util.Date createTime;
	/**修改人*/
	@Excel(name = "修改人", width = 15)
    @ApiModelProperty(value = "修改人")
	private java.lang.String updateBy;
	/**修改时间*/
	@Excel(name = "修改时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间")
	private java.util.Date updateTime;
	/**规则状态1-有效  0-失效*/
	@Excel(name = "规则状态1-有效  0-失效", width = 15)
    @ApiModelProperty(value = "规则状态1-有效  0-失效")
	private java.lang.Short invalidStatus;
	
}
