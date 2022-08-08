package com.zcdy.dsc.modules.collection.gis.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * gis页面绑定变量入参
 * 描述: 
 * @author：  songguang.jiao
 * 创建时间：  2020年1月6日 上午10:06:48
 * 版本号: V1.0
 */
@Setter
@Getter
public class GisIotVo {
	
	//模型id
	
	@ApiModelProperty(value="模型id")
	@NotNull(message="模型ID不能为空")
	@NotBlank(message = "模型ID不能为空")
	private String modelId;
	
	//变量id
	@ApiModelProperty(value="变量id")
	@NotNull(message="变量ID不能为空")
	@NotBlank(message = "变量ID不能为空")
	private String variableId;
	
}
