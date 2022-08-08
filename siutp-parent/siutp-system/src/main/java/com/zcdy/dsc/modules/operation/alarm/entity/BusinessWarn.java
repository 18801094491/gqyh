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
 * 描述: 告警信息表
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-02-18
 * 版本号: V1.0
 */
@Data
@TableName("business_warn")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="business_warn", description="告警信息")
public class BusinessWarn {
    
	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	private String id;
	/**时间编号*/
	@Excel(name = "事件编号", width = 15)
    @ApiModelProperty(value = "事件编号")
	private String warnSn;
	/**告警名称*/
	@Excel(name = "告警名称", width = 15)
    @ApiModelProperty(value = "告警名称")
	private String warnName;
	/**告警内容*/
	@Excel(name = "告警内容", width = 15)
	@ApiModelProperty(value = "告警内容")
	private String warnContent;
	/**告警等级（一般，警告，严重，紧急）*/
	@Excel(name = "告警等级（一般，警告，严重，紧急）", width = 15)
    @ApiModelProperty(value = "告警等级（一般，警告，严重，紧急）")
	private String warnLevel;
	/**告警类型(采集告警，工单告警，应急事件告警，计划任务告警)*/
	@Excel(name = "告警类型(采集告警，工单告警，应急事件告警，计划任务告警)", width = 15)
    @ApiModelProperty(value = "告警类型(采集告警，工单告警，应急事件告警，计划任务告警)")
	private String warnType;
	
	/**
	 * 时间分类，区别于类型,0-不分类(历史数据无法细化分类)，1-离线，2-数据告警，3-未知
	 */
	@ApiModelProperty(value = "0-不分类,1-离线，2-数据告警，3-未知")
	private String warnClassification;
	
	/**告警时间*/
	@Excel(name = "告警时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "告警时间")
	private Date warnTime;
	
	/**
	 * 确认状态，0-待确认，1-已确认
	 */
	@Excel(name = "确认状态，0-待确认，1-已确认")
    @ApiModelProperty(value = "确认状态，0-待确认，1-已确认")
	private String confirmStatus;
	/**告警状态，0-初始化，1-未处理，2-已处理，3-已关闭*/
	@Excel(name = "告警状态，0-初始化，1-未处理，2-已处理，3-已关闭", width = 15)
    @ApiModelProperty(value = "告警状态，0-初始化，1-未处理，2-已处理，3-已关闭")
	private String warnStatus;
	/**告警产生渠道(系统，手工)*/
	@Excel(name = "告警产生渠道(系统，手工)", width = 15)
    @ApiModelProperty(value = "告警产生渠道(系统，手工)")
	private String warnWay;
	/**操作时间*/
	@Excel(name = "操作时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "操作时间")
	private Date operateTime;
	/**持续时长(operate_time和warn_time差值)*/
	@Excel(name = "持续时长(operate_time和warn_time差值)", width = 15)
    @ApiModelProperty(value = "持续时长(operate_time和warn_time差值)")
	private String duration;
	/**操作人id*/
	@Excel(name = "操作人id", width = 15)
    @ApiModelProperty(value = "操作人id")
	private String operator;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
	private String description;
	
	/**采集设备id*/
	@Excel(name = "采集设备id", width = 15)
    @ApiModelProperty(value = "采集设备id")
	private String iotId;
	
	/**规则id*/
	@Excel(name = "规则id", width = 15)
    @ApiModelProperty(value = "规则id")
	private String ruleId;
	
	/**匹配规则内容*/
	@Excel(name = "匹配规则内容", width = 15)
    @ApiModelProperty(value = "匹配规则内容")
	private String ruleContent;
	
	/**处理渠道*/
	@ApiModelProperty(value = "处理渠道", notes="0-系统处理，1-人工处理")
	private String processRoute;
}
