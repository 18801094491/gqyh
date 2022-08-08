package com.zcdy.dsc.modules.operation.alarm.vo;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 告警信息视图对象
 * 
 * @author Roberto
 * @date 2020/05/14
 */
@Setter
@Getter
@ToString
@ApiModel(value = "AlarmResultVO", description = "告警信息")
public class AlarmResultVO {
    
    @ApiModelProperty("事件id")
    private String id;

    // 设别类型名称
    @ApiModelProperty("设别类型名称")
    private String optTypeName;

    // 告警等级
    @ApiModelProperty("告警等级")
    private String alarmLevel;
    
    // 告警等级
    @ApiModelProperty("告警等级编码")
    private String alarmLevelCode;

    // 告警时间
    @ApiModelProperty("告警时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date occurTime;

    // 告警内容
    @ApiModelProperty("告警内容")
    private String alarmContent;

    // 规则详情
    @ApiModelProperty("规则详情")
    private String ruleContent;
}
