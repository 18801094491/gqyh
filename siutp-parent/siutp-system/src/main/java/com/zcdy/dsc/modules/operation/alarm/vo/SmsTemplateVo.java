package com.zcdy.dsc.modules.operation.alarm.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 描述: 提醒模板下拉列表
 * @author：  songguang.jiao
 * 创建时间： 2020年3月6日 下午3:39:59 
 * 版本号: V1.0
 */
@Getter
@Setter
@ApiModel(value="SmsTemplateVo",description="提醒模板下拉列表")
public class SmsTemplateVo {
	
	//模板code
	@ApiModelProperty(value="ApiModelProperty")
	private String code;
	
	//模板名称
	@ApiModelProperty(value="模板名称")
	private String templateName;

}
