package com.zcdy.dsc.modules.operation.work.entity;

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
 * 描述: 班次管理
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-01-09
 * 版本号: V1.0
 */
@Data
@TableName("work_shifts")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="work_shifts对象", description="班次管理")
public class WorkShifts {
    
	/**班次名称*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "班次名称")
	private java.lang.String id;
	/**班次名称*/
	@Excel(name = "班次名称", width = 15)
    @ApiModelProperty(value = "班次名称")
	private java.lang.String shiftsName;
	/**班次描述*/
	@Excel(name = "班次描述", width = 15)
    @ApiModelProperty(value = "班次描述")
	private java.lang.String shiftsDescribe;
	/**上班时间*/
	@Excel(name = "上班时间", width = 15)
    @ApiModelProperty(value = "上班时间")
	private String startTime;
	/**下班时间*/
	@Excel(name = "下班时间", width = 15)
    @ApiModelProperty(value = "下班时间")
	private String overTime;
	/**下班是否需要打卡,0-否，1-是*/
	@Excel(name = "下班是否需要打卡,0-否，1-是", width = 15)
    @ApiModelProperty(value = "下班是否需要打卡,0-否，1-是")
	private java.lang.String offSign;
	/**上班时长，小时*/
	@Excel(name = "上班时长，小时", width = 15)
    @ApiModelProperty(value = "上班时长，小时")
	private java.lang.Integer workHours;
	/**开始休息时间*/
	@Excel(name = "开始休息时间", width =15)
    @ApiModelProperty(value = "开始休息时间")
	private java.lang.String restStartTime;
	/**结束休息时间*/
	@Excel(name = "结束休息时间", width = 15)
    @ApiModelProperty(value = "结束休息时间")
	private String restOverTime;
	/**启停用状态*/
	@Excel(name = "启停用状态", width = 15)
    @ApiModelProperty(value = "启停用状态")
	private java.lang.String shiftsStatus;
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
