package com.zcdy.dsc.modules.message.vo;

import org.jeecgframework.poi.excel.annotation.Excel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 描述: 事件管理vo类
 * @author:  songguang.jiao
 * 创建时间:  2020年4月15日 上午10:33:56
 * 版本: V1.0
 */
@Getter
@Setter
@ApiModel(value="SmsEventVo",description="事件管理vo类")
public class SmsEventVo {

	/**id*/
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
    @ApiModelProperty(value = "短信配置id")
	private java.lang.String templateId;
    /**短信配置名称*/
    @Excel(name = "配置名称", width = 15)
    @ApiModelProperty(value = "短信配置名称")
	private java.lang.String templateName;
	/**启停用状态1-启用，0-停用*/
    @ApiModelProperty(value = "启停用状态1-启用，0-停用")
	private java.lang.String eventStatus;
	/**启停用状态1-启用，0-停用*/
	@Excel(name = "启停用状态", width = 15)
    @ApiModelProperty(value = "启停用状态")
	private java.lang.String eventStatusName;
}
