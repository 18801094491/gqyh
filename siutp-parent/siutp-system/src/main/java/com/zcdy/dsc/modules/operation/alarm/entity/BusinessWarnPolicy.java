package com.zcdy.dsc.modules.operation.alarm.entity;

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
 * 描述: 报警信息通知策略
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-03-04
 * 版本号: V1.0
 */
@Data
@TableName("business_warn_policy")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="business_warn_policy", description="报警信息通知策略")
public class BusinessWarnPolicy {
    
	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**策略名称*/
	@Excel(name = "策略名称", width = 15)
    @ApiModelProperty(value = "策略名称")
	private java.lang.String name;
	/**告警等级*/
	@Excel(name = "告警等级", width = 15)
    @ApiModelProperty(value = "告警等级")
	private java.lang.String warnLevel;
	/**消息模板*/
	@Excel(name = "消息模板", width = 15)
    @ApiModelProperty(value = "消息模板")
	private java.lang.String smsTemplate;
	/**启停用状态 1-启用，0-停用*/
	@Excel(name = "启停用状态 1-启用，0-停用", width = 15)
    @ApiModelProperty(value = "启停用状态 1-启用，0-停用")
	private java.lang.String workStatus;
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
