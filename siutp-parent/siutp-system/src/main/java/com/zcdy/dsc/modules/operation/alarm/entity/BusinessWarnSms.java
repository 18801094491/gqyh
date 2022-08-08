package com.zcdy.dsc.modules.operation.alarm.entity;

import java.util.Date;

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
 * @Description: 告警发送短信记录
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-04-09
 * 版本号: V1.0
 */
@Data
@TableName("business_warn_sms")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="business_warn_sms", description="告警发送短信记录")
public class BusinessWarnSms {
    
	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**告警等级*/
	@Excel(name = "告警等级", width = 15)
    @ApiModelProperty(value = "告警等级")
	private java.lang.String warnLevel;
	/**告警时间*/
	@Excel(name = "告警时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "告警时间")
	private Date warnTime;
	/**采集设备id*/
	@Excel(name = "告警id", width = 15)
    @ApiModelProperty(value = "告警id")
	private java.lang.String alarmId;
	/**规则id*/
	@Excel(name = "规则id", width = 15)
    @ApiModelProperty(value = "规则id")
	private java.lang.String iotId;
	/**接收人id*/
	@Excel(name = "接收人id", width = 15)
    @ApiModelProperty(value = "接收人id")
	private java.lang.String receiveUser;
	/**创建时间*/
	@Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
	private java.util.Date createTime;
}
