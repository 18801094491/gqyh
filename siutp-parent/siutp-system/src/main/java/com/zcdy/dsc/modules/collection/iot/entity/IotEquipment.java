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
 * 描述: 模型设备维护
 * @author： 智能无人硬核心项目组
 * 创建时间： 2020-02-26
 * 版本号: V1.0
 */
@Data
@TableName("iot_equipment")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "iot_equipment", description = "模型设备维护")
public class IotEquipment {

	/** id */
	@TableId(type = IdType.UUID)
	@ApiModelProperty(value = "id")
	private java.lang.String id;
	/** 设备编码 */
	@Excel(name = "设备编码", width = 15)
	@ApiModelProperty(value = "设备编码")
	private java.lang.String iotSn;
	/** 设备名称 */
	@Excel(name = "设备名称", width = 15)
	@ApiModelProperty(value = "设备名称")
	private java.lang.String iotName;
	/** 设备类型、类别 */
	@Excel(name = "设备类型、类别", width = 15)
	@ApiModelProperty(value = "设备类型、类别")
	private java.lang.String iotCategory;

	/** 采集周期-秒 */
	@Excel(name = "采集周期-秒", width = 15)
	@ApiModelProperty(value = "采集周期-秒")
	private java.lang.Integer iotCycle;
	/** 设备状态1-启用，2停用 */
	@Excel(name = "设备状态1-启用，2停用", width = 15)
	@ApiModelProperty(value = "设备状态1-启用，2停用")
	private java.lang.String iotStatus;
}
