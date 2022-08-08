package com.zcdy.dsc.modules.system.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 类型分类下拉选
 * @author songguang.jiao
 * 2019年12月30日 上午10:40:08
 *
 * descriptions:
 *
 */
@Setter
@Getter
@ApiModel(value="com.zcdy.dsc.modules.system.vo.SysCateDropdown", description="分类信息")
public class SysCateDropdown {

	//分类code
	@ApiModelProperty(name="code", value="编码")
	private String code;
	//分类名称
	@ApiModelProperty(name="title", value="内容")
	private String title;
	
}
