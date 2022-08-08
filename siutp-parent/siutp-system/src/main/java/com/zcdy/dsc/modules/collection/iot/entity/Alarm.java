package com.zcdy.dsc.modules.collection.iot.entity;

import java.util.List;

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
 * 描述: 设备报警规则
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-03-02
 * 版本号: V1.0
 */
@Data
@TableName("iot_equipment_rule")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="iot_equipment_rule", description="设备报警规则")
public class Alarm {
    
	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;

	/**设备id*/
	@Excel(name = "设备id", width = 15)
	@ApiModelProperty(value = "设备id")
	private java.lang.String equipmentId;
	/**设备名称*/
	@Excel(name = "设备名称", width = 15)
	@ApiModelProperty(value = "设备名称")
	private java.lang.String equipmentName;

	/**是否生成告警事件*/
	@Excel(name = "是否生成告警事件", width = 15)
	@ApiModelProperty(value = "是否生成告警事件")
	private java.lang.String alarm;


	/**是否启用质量戳名称*/
	@Excel(name = "是否生成告警事件", width = 15)
	@ApiModelProperty(value = "是否生成告警事件")
	private java.lang.String isAlarmName;

	/**报警规则名称*/
	@Excel(name = "报警规则名称", width = 15)
    @ApiModelProperty(value = "报警规则名称")
	private java.lang.String alarmName;
	/**报警规则*/
	@Excel(name = "报警规则", width = 15)
	@ApiModelProperty(value = "报警规则")
	private List<AlarmRule> alarmRuleList;

	/**报警规则状态code*/
	@Excel(name = "报警规则状态code 0为告警，1为开，2为关", width = 15)
	@ApiModelProperty(value = "报警规则状态code0为告警，1为开，2为关")
	private String alarmRuleType;

	/**报警规则状态value 新增的时候不用传*/
	@Excel(name = "报警规则状态value 新增的时候不用传", width = 15)
	@ApiModelProperty(value = "报警规则状态value 新增的时候不用传")
	private String alarmRuleTypeValue;

	/**规则内容*/
	@Excel(name = "规则内容", width = 15)
	@ApiModelProperty(value = "规则内容")
	private java.lang.String alarmRule;

	/**与或关系*/
	@Excel(name = "与或关系", width = 15)
	@ApiModelProperty(value = "与或关系")
	private java.lang.String andOr;

	/** 报警级别*/
	@Excel(name = " 报警级别", width = 15)
    @ApiModelProperty(value = " 报警级别")
	private java.lang.String alarmLevel;

	/** 报警级别名称*/
	@Excel(name = " 报警级别名称", width = 15)
	@ApiModelProperty(value = " 报警级别名称")
	private java.lang.String alarmLevelName;

	/**报警模板id*/
	@Excel(name = "报警模板id", width = 15)
    @ApiModelProperty(value = "报警模板id")
	private java.lang.String alarmModleId;

	/**告警内容模板*/
	@Excel(name = "告警内容模板", width = 15)
	@ApiModelProperty(value = "告警内容模板")
	private java.lang.String alarmModleName;


	/**创建时间*/
	@Excel(name = "创建时间", width = 15)
    @ApiModelProperty(value = "创建时间")
	private java.lang.String createTime;

	/**停用启用状态*/
	@ApiModelProperty(value = "停用启用状态 查询的时候为Boolean类型")
	private java.lang.Boolean alarmStatus;
	/**停用启用状态Code*/
	@ApiModelProperty(value = "停用启用状态Code 新增的时候用，停用启用0为停用1为启用")
	private java.lang.String alarmStatusCode;

}
