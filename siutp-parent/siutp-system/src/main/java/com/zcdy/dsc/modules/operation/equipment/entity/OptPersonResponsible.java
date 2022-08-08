package com.zcdy.dsc.modules.operation.equipment.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @description: 设备资产责任人
 * @author: 智能无人硬核心项目组
 * @date:   2020-06-18
 * @version: V1.0
 */
@Data
@TableName("opt_equipment_person_responsible")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="opt_equipment_person_responsible", description="设备资产责任人")
public class OptPersonResponsible {
    
	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**设备id*/
	@Excel(name = "设备id", width = 15)
    @ApiModelProperty(value = "设备id")
	private java.lang.String equipmentId;
	/**责任人id*/
	@Excel(name = "责任人id", width = 15)
    @ApiModelProperty(value = "责任人id")
	private java.lang.String userId;
}
