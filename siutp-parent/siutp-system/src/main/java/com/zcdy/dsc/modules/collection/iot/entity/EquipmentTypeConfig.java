package com.zcdy.dsc.modules.collection.iot.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 描述: 采集设备按类型配置
 * @author： Roberto
 * 创建时间：   2020-03-07
 * 版本号: V1.0
 */
@Getter
@Setter
@ApiModel(value="EquipmentTypeConfig", description="采集设备配置表")
public class EquipmentTypeConfig {
    
    @ApiModelProperty(value = "设备类型")
	private String typeCode;
    
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
