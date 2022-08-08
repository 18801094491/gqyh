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
 * 描述: 模型设备变量绑定
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-02-26
 * 版本号: V1.0
 */
@Data
@TableName("iot_equipment_variable")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="iot_equipment_variable", description="模型设备变量绑定")
public class IotEquipmentVariable {
    
	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**采集设备id*/
	@Excel(name = "采集设备id", width = 15)
    @ApiModelProperty(value = "采集设备id")
	private java.lang.String iotId;
	/**变量id*/
	@Excel(name = "变量id", width = 15)
    @ApiModelProperty(value = "变量id")
	private java.lang.String variableId;
}
