package com.zcdy.dsc.modules.message.entity;

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
 * @Description: 事件管理
 * @Author: 智能无人硬核心项目组
 * @Date:   2020-04-14
 * @Version: V1.0
 */
@Data
@TableName("sys_sms_event")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="sys_sms_event", description="事件管理")
public class SmsEvent {
    
	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**事件名称*/
	@Excel(name = "事件名称", width = 15)
    @ApiModelProperty(value = "事件名称")
	private java.lang.String eventName;
	/**事件code*/
	@Excel(name = "事件code", width = 15)
    @ApiModelProperty(value = "事件code")
	private java.lang.String eventCode;
	/**短信配置id*/
	@Excel(name = "短信配置id", width = 15)
    @ApiModelProperty(value = "短信配置id")
	private java.lang.String templateId;
	/**启停用状态1-启用，0-停用*/
	@Excel(name = "启停用状态1-启用，0-停用", width = 15)
    @ApiModelProperty(value = "启停用状态1-启用，0-停用")
	private java.lang.String eventStatus;
	/**创建日期*/
	@Excel(name = "创建日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
	private java.util.Date createTime;
	/**创建人登录名称*/
	@Excel(name = "创建人登录名称", width = 15)
    @ApiModelProperty(value = "创建人登录名称")
	private java.lang.String createBy;
	/**更新日期*/
	@Excel(name = "更新日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
	private java.util.Date updateTime;
	/**更新人登录名称*/
	@Excel(name = "更新人登录名称", width = 15)
    @ApiModelProperty(value = "更新人登录名称")
	private java.lang.String updateBy;
	/**删除状态（0，正常，1已删除）*/
	@Excel(name = "删除状态（0，正常，1已删除）", width = 15)
    @ApiModelProperty(value = "删除状态（0，正常，1已删除）")
	private java.lang.Integer delFlag;
}
