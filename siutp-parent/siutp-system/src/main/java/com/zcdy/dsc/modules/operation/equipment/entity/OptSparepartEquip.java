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
 * 描述: 备品备件适用设备信息表
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-02-03
 * 版本号: V1.0
 */
@Data
@TableName("opt_sparepart_equipment")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="opt_sparepart_equipment", description="备品备件适用设备信息表")
public class OptSparepartEquip {
    
	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**备品备件id*/
	@Excel(name = "备品备件id", width = 15)
    @ApiModelProperty(value = "备品备件id")
	private java.lang.String sparepartId;
	/**设备id*/
	@Excel(name = "设备id", width = 15)
    @ApiModelProperty(value = "设备id")
	private java.lang.String equipmentId;
}
