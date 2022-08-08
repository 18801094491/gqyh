package com.zcdy.dsc.modules.operation.alarm.vo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.jeecgframework.poi.excel.annotation.Excel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 描述: 告警通知策略新增修改入参
 * @author：  songguang.jiao
 * 创建时间： 2020年3月4日 上午10:27:23 
 * 版本号: V1.0
 */
@Getter
@Setter
@ApiModel(value="PolicyParamVo",description="告警通知策略新增修改入参")
public class PolicyParamVo {
	/**id*/
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**策略名称*/
    @NotBlank(message="策略名称不能为空")
   	@NotNull(message="策略名称不能为空")
    @ApiModelProperty(value = "策略名称")
	private java.lang.String name;
    
    /**消息模板*/
	@Excel(name = "消息模板", width = 15)
    @ApiModelProperty(value = "消息模板")
	private java.lang.String smsTemplate;
    
    //通知角色
    @ApiModelProperty(value = "通知角色")
    private String rolesId;
    
    //通知用户
    @ApiModelProperty(value = "通知用户")
    private String usersId;
    
    //通知班组
    @ApiModelProperty(value = "通知班组")
    private String workTeamsId;
	/**告警等级*/
    @NotBlank(message="告警等级不能为空")
	@NotNull(message="告警等级不能为空")
    @ApiModelProperty(value = "告警等级")
	private java.lang.String warnLevel;
    
    //启用状态
    @ApiModelProperty(value = "启用状态")
    @NotBlank(message="启用状态不能为空")
   	@NotNull(message="启用状态不能为空")
    private String workStatus;
}
