package com.zcdy.dsc.modules.message.vo;

import org.jeecgframework.poi.excel.annotation.Excel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 描述: 短信模板配置下拉选
 * @author:  songguang.jiao
 * 创建时间:  2020年4月14日 上午9:57:00
 * 版本: V1.0
 */
@Getter
@Setter
@ApiModel(value="SmsTemplateDropdown",description="短信模板配置下拉选")
public class SmsTemplateDropdown {

	/**id*/
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**配置名称*/
    @Excel(name="配置名称",width=15)
    @ApiModelProperty(value = "配置名称")
	private java.lang.String templateName;
	
}
