package com.zcdy.dsc.modules.collection.iot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 描述: 采集设备配置表
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-03-07
 * 版本号: V1.0
 */
@Data
@TableName("iot_equipment_config")
@Accessors(chain = true)
@ApiModel(value="iot_equipment_config", description="采集设备配置表")
public class EquipmentConfig {
    
	/**id*/
	@TableId(type = IdType.UUID)
	@ApiModelProperty(value = "id", hidden=true)
	private String id;
	/**equipmentId*/
    @ApiModelProperty(value = "采集设备id")
	private String equipmentId;
	/**设备类型编码*/
	@ApiModelProperty(value = "采集设备id", hidden=true)
	private String equipmentType;
	/**是否检测质量戳*/
    @ApiModelProperty(value = "是否检测质量戳")
	private String checkQuality;
	/**是否告警*/
    @ApiModelProperty(value = "是否告警")
	private String alarm;
	/**告警等级*/
    @ApiModelProperty(value = "告警等级")
	private String alarmLevel;
	/**告警模板*/
    @ApiModelProperty(value = "告警模板")
	private String alarmModel;
}
