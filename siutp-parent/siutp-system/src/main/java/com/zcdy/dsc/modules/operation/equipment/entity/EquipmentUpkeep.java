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
 * 描述: 维保记录
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-02-17
 * 版本号: V1.0
 */
@Data
@TableName("equipment_upkeep")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="equipment_upkeep", description="维保记录")
public class EquipmentUpkeep {
    
	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**资产*/
	@Excel(name = "资产", width = 15)
    @ApiModelProperty(value = "资产")
	private java.lang.String equipmentId;
	/**维保人员id*/
	@Excel(name = "维保人员id", width = 15)
    @ApiModelProperty(value = "维保人员id")
	private java.lang.String upkeepCreator;
	/**维保时间*/
	@Excel(name = "维保时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "维保时间")
	private java.util.Date upkeepTime;
	/**维保内容*/
	@Excel(name = "维保内容", width = 15)
    @ApiModelProperty(value = "维保内容")
	private java.lang.String upkeepContent;
	/**维保原因*/
	@Excel(name = "维保原因", width = 15)
    @ApiModelProperty(value = "维保原因")
	private java.lang.String upkeepReason;
	/**维保结果*/
	@Excel(name = "维保结果", width = 15)
    @ApiModelProperty(value = "维保结果")
	private java.lang.String upkeepResult;
	/**维保类型*/
	@Excel(name = "维保类型", width = 15)
	@ApiModelProperty(value = "维保类型")
	private java.lang.String type;
	/**创建时间*/
	@Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
	private java.util.Date createTime;
	/**修改人*/
	@Excel(name = "修改人", width = 15)
    @ApiModelProperty(value = "修改人")
	private java.lang.String updateBy;
	/**修改时间*/
	@Excel(name = "修改时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间")
	private java.util.Date updateTime;
	/**删除标识0-正常,1-已删除*/
	@Excel(name = "删除标识0-正常,1-已删除", width = 15)
    @ApiModelProperty(value = "删除标识0-正常,1-已删除")
	private java.lang.Integer delFlag;
}
