package com.zcdy.dsc.modules.settle.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 描述: 客户名称下拉选
 * @author：  songguang.jiao
 * 创建时间：  2020年1月13日 下午2:22:12
 * 版本号: V1.0
 */
@Getter
@Setter
@ApiModel(value="CustomerNameVo",description="客户名称下拉选")
public class CustomerDropdown {

	@ApiModelProperty(value = "id")
	private java.lang.String id;
	//客户名称
	@ApiModelProperty(value="客户名称")
	private String customerName;
}
