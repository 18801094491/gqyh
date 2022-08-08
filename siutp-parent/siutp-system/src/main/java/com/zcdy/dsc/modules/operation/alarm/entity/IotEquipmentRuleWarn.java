package com.zcdy.dsc.modules.operation.alarm.entity;

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
 * 描述: 告警信息配置
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-04-02
 * 版本号: V1.0
 */
@Data
@TableName("iot_equipment_rule_warn")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="iot_equipment_rule_warn", description="告警信息配置")
public class IotEquipmentRuleWarn {
    
	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**报警等级code*/
	@Excel(name = "报警等级code", width = 15)
    @ApiModelProperty(value = "报警等级code")
	private java.lang.String warnLevelCode;

	@ApiModelProperty(value = "设备类别")
	private java.lang.String type;

}
