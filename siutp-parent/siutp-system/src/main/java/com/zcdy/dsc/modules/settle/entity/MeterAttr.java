package com.zcdy.dsc.modules.settle.entity;

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
 * @Description: 水表附属信息
 * @Author: 智能无人硬核心项目组
 * @Date:   2020-04-26
 * @Version: V1.0
 */
@Data
@TableName("settle_meter_attr")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="settle_meter_attr", description="水表附属信息")
public class MeterAttr {
    
	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**设备id*/
	@Excel(name = "设备id", width = 15)
    @ApiModelProperty(value = "设备id")
	private java.lang.String equipmentId;
	/**是否月结 1-是,0-否*/
	@Excel(name = "是否月结 1-是,0-否", width = 15)
    @ApiModelProperty(value = "是否月结 1-是,0-否")
	private java.lang.String monthBalance;
}
