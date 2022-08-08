package com.zcdy.dsc.modules.collection.iot.vo;

import org.jeecgframework.poi.excel.annotation.Excel;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IotEquipmentVo {
	/** id */
	@TableId(type = IdType.UUID)
	@ApiModelProperty(value = "id")
	private java.lang.String id;
	/** 设备编码 */
	@Excel(name = "设备编码", width = 15)
	@ApiModelProperty(value = "设备编码")
	private java.lang.String iotSn;
	@ApiModelProperty(value = "资产编码")
	private java.lang.String equipmentSn;
	@ApiModelProperty(value = "资产名称")
	private java.lang.String optCategoryName;
	/** 设备名称 */
	@Excel(name = "设备名称", width = 15)
	@ApiModelProperty(value = "设备名称")
	private java.lang.String iotName;
	/** 设备类型、类别 */
	@Excel(name = "设备类型、类别", width = 15)
	@ApiModelProperty(value = "设备类型、类别")
	private java.lang.String iotCategory;

	/** 设备类型名称 */
	@Excel(name = "设备类型名称", width = 20)
	@ApiModelProperty(value = "设备类型名称")
	private java.lang.String iotCategoryName;
	/** 采集周期-秒 */
	@Excel(name = "采集周期-秒", width = 15)
	@ApiModelProperty(value = "采集周期-秒")
	private java.lang.Integer iotCycle;
	/** 设备状态1-启用，2停用 */
	@Excel(name = "设备状态1-启用，2停用", width = 15)
	@ApiModelProperty(value = "设备状态1-启用，2停用")
	private java.lang.String iotStatus;
	//是否绑定
	@ApiModelProperty(value = "是否绑定")
	private String bindStatus;
}
