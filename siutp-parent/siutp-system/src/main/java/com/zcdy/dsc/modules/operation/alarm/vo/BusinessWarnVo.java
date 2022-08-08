package com.zcdy.dsc.modules.operation.alarm.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 描述: 报警信息列表
 *
 * @author： songguang.jiao
 * 创建时间： 2020年2月18日 上午11:18:28
 * 版本号: V1.0
 */
@Getter
@Setter
@ApiModel(value = "BusinessWarnVo", description = "报警信息列表")
public class BusinessWarnVo {

    /**
     * id
     */
    @TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
    private java.lang.String id;
    private java.lang.String iotId;
    /**
     * 事件编码
     */
    @ApiModelProperty(value = "编码")
    private java.lang.String warnSn;
    /**
     * 告警名称
     */
    @ApiModelProperty(value = "告警名称")
    private java.lang.String warnName;
    /**
     * 告警等级（一般，警告，严重，紧急）
     */
    @ApiModelProperty(value = "告警等级（一般，警告，严重，紧急）")
    private java.lang.String warnLevel;
    /**
     * 告警等级（一般，警告，严重，紧急）
     */
    @ApiModelProperty(value = "告警等级（一般，警告，严重，紧急）")
    private java.lang.String warnLevelCode;
    /**
     * 告警类型(采集告警，工单告警，应急事件告警，计划任务告警)
     */
    @ApiModelProperty(value = "告警类型(采集告警，工单告警，应急事件告警，计划任务告警)")
    private java.lang.String warnType;
    /**
     * 告警类型(采集告警，工单告警，应急事件告警，计划任务告警)
     */
    @ApiModelProperty(value = "告警类型(采集告警，工单告警，应急事件告警，计划任务告警)")
    private java.lang.String warnTypeCode;
    /**
     * 告警时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "告警时间")
    private java.util.Date warnTime;
    /**
     * 操作时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "操作时间")
    private java.util.Date operateTime;

    /**
     * 确认状态，0-待确认，1-已确认
     */
    @ApiModelProperty(value = "确认状态，0-待确认，1-已确认")
    private String confirmStatus;

    /**
     * 告警状态，0-初始化，1-未处理，2-已处理，3-已关闭
     */
    @ApiModelProperty(value = "告警状态，0-初始化，1-未处理，2-已处理，3-已关闭")
    private java.lang.String warnStatus;
    /**
     * 告警状态，0-初始化，1-未处理，2-已处理，3-已关闭
     */
    @ApiModelProperty(value = "告警状态，0-初始化，1-未处理，2-已处理，3-已关闭")
    private java.lang.String warnStatusCode;
    /**
     * 告警产生渠道(系统，手工)
     */
    @ApiModelProperty(value = "告警产生渠道(系统，手工)")
    private java.lang.String warnWay;
    /**
     * 告警产生渠道(系统，手工)
     */
    @ApiModelProperty(value = "告警产生渠道(系统，手工)")
    private java.lang.String warnWayCode;
    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人")
    private java.lang.String operator;
    /**
     * 操作人id
     */
    @ApiModelProperty(value = "操作人id")
    private java.lang.String operatorId;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private java.lang.String description;
    /**
     * 持续时长
     */
    @ApiModelProperty(value = "持续时长")
    private java.lang.String duration;


    /**
     * 告警内容
     */
    @ApiModelProperty(value = "告警内容")
    private String warnContent;
    /**
     * 所属资产
     */
    @ApiModelProperty(value = "所属资产")
    private String equipmentType;
    /**
     * 匹配规则内容
     */
    @ApiModelProperty(value = "匹配规则内容")
    private String ruleContent;

    /**
     * 设备id
     */
    @ApiModelProperty("设备id")
    private String optId;

    /**
     * 设备标段+位置
     */
    @ApiModelProperty("设备标段+位置")
    private String equ;
    @ApiModelProperty("告警数量")
    private Integer num;
}
