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
 * 描述: 维保记录的图片信息
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-02-24
 * 版本号: V1.0
 */
@Data
@TableName("equipment_upkeep_attach")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="equipment_upkeep_attach", description="维保记录的图片信息")
public class EquipmentUpkeepAttach {
    
	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**缩略图*/
	@Excel(name = "缩略图", width = 15)
    @ApiModelProperty(value = "缩略图")
	private java.lang.String upkeepThumb;
	/**图片路径*/
	@Excel(name = "图片路径", width = 15)
    @ApiModelProperty(value = "图片路径")
	private java.lang.String upkeepImage;
	/**维保记录id*/
	@Excel(name = "维保记录id", width = 15)
    @ApiModelProperty(value = "维保记录id")
	private java.lang.String upkeepId;
}
