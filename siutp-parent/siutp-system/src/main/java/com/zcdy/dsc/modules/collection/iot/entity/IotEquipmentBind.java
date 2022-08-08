package com.zcdy.dsc.modules.collection.iot.entity;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

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
 * 描述: 采集设备绑定资产表
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-03-07
 * 版本号: V1.0
 */
@Data
@TableName("iot_opt_equipment")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="iot_opt_equipment", description="采集设备绑定资产表")
public class IotEquipmentBind {
    
	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**optId*/
	@Excel(name = "optId", width = 15)
    @ApiModelProperty(value = "资产Id")
	@NotEmpty(message="资产Id不能为空")
	@NotNull(message="资产Id不能为空")
	private java.lang.String optId;
	/**iotId*/
	@Excel(name = "iotId", width = 15)
	@NotEmpty(message="采集设备Id不能为空")
	@NotNull(message="采集设备Id不能为空")
    @ApiModelProperty(value = "采集设备Id")
	private java.lang.String iotId;
}
