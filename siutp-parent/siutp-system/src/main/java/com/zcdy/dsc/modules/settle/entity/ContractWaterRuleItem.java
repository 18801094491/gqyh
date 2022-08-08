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
 * @Description: 合同水价规则详情表
 * @Author: 智能无人硬核心项目组
 * @Date:   2020-04-22
 * @Version: V1.0
 */
@Data
@TableName("settle_contract_water_rule_item")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="settle_contract_water_rule_item", description="合同水价规则详情表")
public class ContractWaterRuleItem {
    
	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "规则详情id")
	private java.lang.String id;
	/**合同id*/
	@Excel(name = "规则id", width = 15)
    @ApiModelProperty(value = "规则id")
	private java.lang.String ruleId;
	/**开始时间*/
	@Excel(name = "开始时间", width = 20, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "开始时间(yyyy-MM-dd)")
	private java.util.Date startTime;
	@Excel(name = "价格", width =15)
    @ApiModelProperty(value = "价格")
	private String price;
	/**截止时间*/
	@Excel(name = "截止时间", width = 20, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "截止时间(yyyy-MM-dd)")
	private java.util.Date endTime;
}
