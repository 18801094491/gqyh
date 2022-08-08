package com.zcdy.dsc.modules.collection.gis.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * 描述: 模型设备
 * @author： 智能无人硬核心项目组
 * 创建时间：   2019-12-17
 * 版本号: V1.0
 */
@Data
@TableName("gis_equipment")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="gis_equipment对象", description="模型设备")
public class GisEquipment {
    
	/**id*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**资产id*/
	@Excel(name = "资产id", width = 15)
    @ApiModelProperty(value = "资产id")
	private java.lang.String equipmentId;
	/**模型id*/
	@Excel(name = "模型id", width = 15)
    @ApiModelProperty(value = "模型id")
	private java.lang.String modelId;
}
