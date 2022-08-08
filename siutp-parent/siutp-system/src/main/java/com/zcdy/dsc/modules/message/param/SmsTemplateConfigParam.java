package com.zcdy.dsc.modules.message.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 描述: 新增修改入参
 * @author:  songguang.jiao
 * 创建时间:  2020年4月13日 下午2:28:53
 * 版本: V1.0
 */
@Getter
@Setter
@ApiModel(value="SmsTemplateConfigParam",description="新增修改入参")
public class SmsTemplateConfigParam {

	/**id*/
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**模板名称*/
    @ApiModelProperty(value = "模板名称")
	private java.lang.String templateName;
	/**短信模板code*/
    @ApiModelProperty(value = "短信code")
	private java.lang.String templateCode;
	/**短信签名*/
    @ApiModelProperty(value = "签名")
	private java.lang.String signName;
	/**短信内容模板*/
    @ApiModelProperty(value = "短信内容")
	private java.lang.String templateContent;
	/**启停用状态*/
    @ApiModelProperty(value = "启停用状态")
	private String templateStatus;
	/**模块id*/
    @ApiModelProperty(value = "模块id")
	private String moduleId;
    /**用户id*/
    @ApiModelProperty(value = "用户id")
    private String usersId;
	
}
