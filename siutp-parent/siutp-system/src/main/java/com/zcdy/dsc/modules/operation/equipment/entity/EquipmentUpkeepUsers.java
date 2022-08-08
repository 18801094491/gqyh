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
 * 描述: 维保人员信息
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-02-24
 * 版本号: V1.0
 */
@Data
@TableName("equipment_upkeep_users")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="equipment_upkeep_users", description="维保人员信息")
public class EquipmentUpkeepUsers {
    
	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**upkeepUser*/
	@Excel(name = "upkeepUser", width = 15)
    @ApiModelProperty(value = "upkeepUser")
	private java.lang.String upkeepUser;
	/**upkeepId*/
	@Excel(name = "upkeepId", width = 15)
    @ApiModelProperty(value = "upkeepId")
	private java.lang.String upkeepId;
}
