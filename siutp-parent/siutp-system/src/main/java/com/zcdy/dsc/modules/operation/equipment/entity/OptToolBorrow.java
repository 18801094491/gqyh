package com.zcdy.dsc.modules.operation.equipment.entity;

import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 描述: 运维工具借用历史
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-02-05
 * 版本号: V1.0
 */
@Data
@TableName("opt_tool_borrow")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="opt_tool_borrow", description="运维工具借用历史")
public class OptToolBorrow {
    
	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**货品ID*/
	@Excel(name = "货品ID", width = 15)
    @ApiModelProperty(value = "货品ID")
	private java.lang.String goodsId;
	/**申请信息编号*/
	@Excel(name = "申请信息编号", width = 15)
    @ApiModelProperty(value = "申请信息编号")
	private java.lang.String applySn;
	/**借用时间*/
	@Excel(name = "借用时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "借用时间")
	private java.util.Date borrowTime;
	/**是否归还  1-是，0-否*/
	@Excel(name = "是否归还  1-是，0-否", width = 15)
    @ApiModelProperty(value = "是否归还  1-是，0-否")
	private java.lang.String backStatus;
	/**归还时间*/
	@Excel(name = "归还时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "归还时间")
	private java.util.Date backTime;
	/**借用人*/
	@Excel(name = "借用人", width = 15)
    @ApiModelProperty(value = "借用人")
	private java.lang.String userId;
	/**备注信息*/
	@Excel(name = "备注信息", width = 15)
    @ApiModelProperty(value = "备注信息")
	private java.lang.String description;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
    @ApiModelProperty(value = "创建人")
	private java.lang.String createBy;
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
