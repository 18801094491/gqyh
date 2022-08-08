package com.zcdy.dsc.modules.message.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 描述: 事件管理入参
 * @author:  songguang.jiao
 * 创建时间:  2020年4月15日 上午10:15:03
 * 版本: V1.0
 */
@ApiModel(value="SmsEventParam",description="事件管理入参")
@Getter
@Setter
public class SmsEventParam {

	/**id*/
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**事件名称*/
    @ApiModelProperty(value = "事件名称")
	private java.lang.String eventName;
	/**事件code*/
    @ApiModelProperty(value = "事件code")
	private java.lang.String eventCode;
	/**短信模板id*/
    @ApiModelProperty(value = "短信模板id")
	private java.lang.String templateId;
	/**启停用状态1-启用，0-停用*/
    @ApiModelProperty(value = "启停用状态1-启用，0-停用")
	private java.lang.String eventStatus;
}
