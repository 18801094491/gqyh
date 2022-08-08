package com.zcdy.dsc.modules.operation.equipment.entity;

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
 * 描述: 资产设备属性信息
 * @author： 智能无人硬核心项目组
 * 创建时间：   2019-12-20
 * 版本号: V1.0
 */
@Data
@TableName("opt_equipment_attr")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="opt_equipment_attr对象", description="资产设备属性信息")
public class OptEquipmentAttr {
    
	/**id*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**设备id*/
	@Excel(name = "设备id", width = 15)
    @ApiModelProperty(value = "设备id")
	private java.lang.String equipmentId;
	/**属性字段*/
	@Excel(name = "属性字段", width = 15)
    @ApiModelProperty(value = "属性字段")
	private java.lang.String attributeEn;
	/**属性名称*/
	@Excel(name = "属性名称", width = 15)
    @ApiModelProperty(value = "属性名称")
	private java.lang.String attributeCn;
	/**属性值*/
	@Excel(name = "属性值", width = 15)
    @ApiModelProperty(value = "属性值")
	private java.lang.String attributeVal;
}
