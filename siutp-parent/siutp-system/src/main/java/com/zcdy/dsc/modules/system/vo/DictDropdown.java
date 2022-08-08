package com.zcdy.dsc.modules.system.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;


/**
 * 描述: 下拉选数据字典
 * @author：  songguang.jiao
 * 创建时间：  2020年1月16日 下午7:52:59
 * 版本号: V1.0
 */
@Getter
@Setter
@ApiModel(value="DictDropdown",description="页面下拉选数据字典")
public class DictDropdown {

	@ApiModelProperty(value="字典值")
	private String title;

	@ApiModelProperty(value="字典code")
	private String code;
}
