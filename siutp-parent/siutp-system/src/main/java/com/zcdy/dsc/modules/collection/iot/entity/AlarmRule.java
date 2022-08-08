package com.zcdy.dsc.modules.collection.iot.entity;

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
 * 描述: 设备报警规则变量表
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-03-02
 * 版本号: V1.0
 */
@Data
@TableName("iot_equipment_rule_item")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="iot_equipment_rule_item", description="设备报警规则变量表")
public class AlarmRule {
    
	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**报警规则id*/
	@Excel(name = "报警规则id", width = 15)
    @ApiModelProperty(value = "报警规则id")
	private java.lang.String alarmId;

	/**报警规则名称*/
	@Excel(name = "报警规则名称", width = 15)
	@ApiModelProperty(value = "报警规则名称")
	private java.lang.String alarmName;

	/**变量id*/
	@Excel(name = "变量id", width = 15)
    @ApiModelProperty(value = "变量id")
	private java.lang.String variableId;

	/**变量标题*/
	@Excel(name = "变量标题", width = 15)
	@ApiModelProperty(value = "变量标题")
	private java.lang.String variableName;
	
	/**变量名称*/
	@Excel(name = "变量名称", width = 15)
	@ApiModelProperty(value = "变量名称")
	private java.lang.String variableSn;
	/**规则*/
	@Excel(name = "规则", width = 15)
    @ApiModelProperty(value = "规则")
	private java.lang.String alarmRule;

	/**规则名称*/
	@Excel(name = "规则名称", width = 15)
	@ApiModelProperty(value = "规则名称")
	private java.lang.String alarmRuleName;

	/**比较的值*/
	@Excel(name = "比较的值", width = 15)
    @ApiModelProperty(value = "比较的值")
	private java.lang.String alarmValue;
	/**创建时间*/
	@Excel(name = "创建时间", width = 15)
    @ApiModelProperty(value = "创建时间")
	private java.lang.String createTime;
}
