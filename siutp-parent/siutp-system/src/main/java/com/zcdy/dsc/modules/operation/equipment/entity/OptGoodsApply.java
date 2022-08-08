package com.zcdy.dsc.modules.operation.equipment.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 描述: 货物申领申请信息
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-02-05
 * 版本号: V1.0
 */
@Data
@TableName("opt_goods_apply")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="opt_goods_apply", description="货物申领申请信息")
public class OptGoodsApply {
    
	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**applySn*/
	@Excel(name = "applySn", width = 15)
    @ApiModelProperty(value = "applySn")
	private java.lang.String applySn;
	/**货物类型*/
	@Excel(name = "货物类型", width = 15)
    @ApiModelProperty(value = "货物类型")
	private java.lang.String dataType;
	/**期望领用时间*/
	@Excel(name = "期望领用时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "期望领用时间")
	private java.util.Date wantedTime;
	/**期望领用时间*/
	@Excel(name = "预计归还时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "预计归还时间")
	private java.util.Date expectbackTime;
	/**紧急程度，数据字典*/
	@Excel(name = "紧急程度，数据字典", width = 15)
    @ApiModelProperty(value = "紧急程度，数据字典")
	private java.lang.String emergentLevel;
	/**审核状态*/
	@Excel(name = "审核状态", width = 15)
    @ApiModelProperty(value = "审核状态")
	private java.lang.String verifyStatus;
	/**备注信息*/
	@Excel(name = "申请事由", width = 15)
    @ApiModelProperty(value = "申请事由")
	private java.lang.String reason;
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
	/**处理状态 1-已处理，0-未处理*/
	@Excel(name = "处理状态 1-已处理，0-未处理", width = 15)
    @ApiModelProperty(value = "处理状态 1-已处理，0-未处理")
	private java.lang.String dealStatus;
}
