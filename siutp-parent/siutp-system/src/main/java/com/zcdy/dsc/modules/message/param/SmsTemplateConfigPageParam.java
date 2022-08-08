package com.zcdy.dsc.modules.message.param;

import com.zcdy.dsc.common.api.param.AbstractPageParam;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 描述: 分页查询参数
 * @author:  songguang.jiao
 * 创建时间:  2020年4月13日 上午10:15:10
 * 版本: V1.0
 */
@Getter
@Setter
@ApiModel(value="分页查询参数")
public class SmsTemplateConfigPageParam extends AbstractPageParam{

	/**模板名称*/
    @ApiModelProperty(value = "模板名称")
	private java.lang.String templateName;
	/**短信模板code*/
    @ApiModelProperty(value = "短信code")
	private java.lang.String templateCode;
	/**启停用状态*/
	 @ApiModelProperty(value = "启停用状态")
	private String templateConfig;
}
