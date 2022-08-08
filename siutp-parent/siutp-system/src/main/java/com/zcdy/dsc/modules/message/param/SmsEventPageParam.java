package com.zcdy.dsc.modules.message.param;

import com.zcdy.dsc.common.api.param.AbstractPageParam;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 描述: 分页查询参数
 * @author:  songguang.jiao
 * 创建时间:  2020年4月15日 上午10:28:54
 * 版本: V1.0
 */
@Getter
@Setter
@ApiModel(value="SmsEventPageParam",description="分页查询参数")
public class SmsEventPageParam extends AbstractPageParam{

	/**事件名称*/
    @ApiModelProperty(value = "事件名称")
	private java.lang.String eventName;
	/**事件code*/
    @ApiModelProperty(value = "事件code")
	private java.lang.String eventCode;
	
}
