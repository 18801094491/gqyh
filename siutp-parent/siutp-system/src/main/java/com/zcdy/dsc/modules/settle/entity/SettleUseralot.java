package com.zcdy.dsc.modules.settle.entity;

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
 * 描述: 用户报装信息
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-02-11
 * 版本号: V1.0
 */
@Data
@TableName("settle_useralot")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="settle_useralot", description="用户报装信息")
public class SettleUseralot {
    
	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**lotSn*/
	@Excel(name = "lotSn", width = 15)
    @ApiModelProperty(value = "lotSn")
	private java.lang.String lotSn;
	/**客户名称*/
	@Excel(name = "客户id", width = 15)
    @ApiModelProperty(value = "客户id")
	private java.lang.String customerId;
	/**报装时间*/
	@Excel(name = "报装时间", width = 20, format = "yyyy-MM-dd HH:mm")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm")
    @ApiModelProperty(value = "报装时间")
	private java.util.Date lotTime;
	/**报装内容*/
	@Excel(name = "报装内容", width = 15)
    @ApiModelProperty(value = "报装内容")
	private java.lang.String lotContent;
	/**任务分配人员id*/
	@Excel(name = "任务分配人员id", width = 15)
    @ApiModelProperty(value = "任务分配人员id")
	private java.lang.String taskId;
	/**实施人联系电话*/
	@Excel(name = "实施人联系电话", width = 15)
    @ApiModelProperty(value = "实施人联系电话")
	private java.lang.String telephone;
	/**安装状态，数据字典*/
	@Excel(name = "安装状态，数据字典", width = 15)
    @ApiModelProperty(value = "安装状态，数据字典")
	private java.lang.String installState;
	/**安装时间*/
	@Excel(name = "安装时间", width = 20, format = "yyyy-MM-dd HH:mm")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm")
    @ApiModelProperty(value = "安装时间")
	private java.util.Date installTime;
	/**用途*/
	@Excel(name = "用途", width = 15)
    @ApiModelProperty(value = "用途")
	private java.lang.String purpose;
	/**审核验收意见*/
	@Excel(name = "审核验收意见", width = 15)
    @ApiModelProperty(value = "审核验收意见")
	private java.lang.String accepidea;
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
