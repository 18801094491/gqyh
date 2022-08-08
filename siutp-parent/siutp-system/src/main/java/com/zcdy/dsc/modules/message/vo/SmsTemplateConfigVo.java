package com.zcdy.dsc.modules.message.vo;

import org.jeecgframework.poi.excel.annotation.Excel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 描述: 短信模板配置vo
 * @author:  songguang.jiao
 * 创建时间:  2020年4月13日 上午10:00:21
 * 版本: V1.0
 */
@Getter
@Setter
@ApiModel(value="SmsTemplateConfigVo",description="短信模板配置vo")
public class SmsTemplateConfigVo {
	
	/**id*/
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**配置名称*/
    @Excel(name="配置名称",width=15)
    @ApiModelProperty(value = "配置名称")
	private java.lang.String templateName;
    /**发送人员*/
    @Excel(name="发送人员",width=15)
    @ApiModelProperty(value = "发送人员")
    private String usersName;
	/**模板code*/
    @Excel(name="短信code",width=15)
    @ApiModelProperty(value = "短信code")
	private java.lang.String templateCode;
	/**模板标签*/
    @Excel(name="短信签名",width=15)
    @ApiModelProperty(value = "短信签名")
	private java.lang.String signName;
	/**启停用状态*/
    @ApiModelProperty(value = "启停用状态")
	private String templateStatus;
    /**启停用状态*/
    @Excel(name="状态",width=15)
    @ApiModelProperty(value = "状态")
	private String templateStatusName;
	/**模块id*/
    @ApiModelProperty(value = "模块id")
	private String moduleId;
    /**发送人员id*/
    @ApiModelProperty(value = "发送人员id")
    private String usersId;
   

}
