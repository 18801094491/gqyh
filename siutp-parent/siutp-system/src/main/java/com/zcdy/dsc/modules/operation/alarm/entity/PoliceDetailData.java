package com.zcdy.dsc.modules.operation.alarm.entity;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 描述: 告警策略详情数据
 * @author：  songguang.jiao
 * 创建时间： 2020年3月5日 下午7:40:47 
 * 版本号: V1.0
 */
@Getter
@Setter
public class PoliceDetailData {

	/**id*/
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**策略名称*/
    @ApiModelProperty(value = "策略名称")
	private java.lang.String name;
    
    //通知角色
    @ApiModelProperty(value = "通知角色")
    private String rolesId;
    
    //通知用户
    @ApiModelProperty(value = "通知用户")
    private String usersId;
    
    //用户信息结果集
    List<BusinessWarnPolicyUsers> list;
    
    //通知班组
    @ApiModelProperty(value = "通知班组")
    private String workTeamsId;
	/**告警等级*/
    @ApiModelProperty(value = "告警等级")
	private java.lang.String warnLevel;
    /**告警等级code*/
    @ApiModelProperty(value = "告警等级code")
	private java.lang.String warnLevelCode;
    
    //启用状态
    @ApiModelProperty(value = "启用状态")
    private String workStatus;
    //启用状态code
    @ApiModelProperty(value = "启用状态code")
    private String workStatusCode;
    /**消息模板*/
    @ApiModelProperty(value = "消息模板")
  	private java.lang.String smsTemplate;
    /**消息模板code*/
    @ApiModelProperty(value = "消息模板code")
  	private java.lang.String smsTemplateCode;
    
	
}
