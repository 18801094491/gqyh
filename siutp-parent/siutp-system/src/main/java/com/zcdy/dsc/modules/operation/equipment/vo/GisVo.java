package com.zcdy.dsc.modules.operation.equipment.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author songguang.jiao
 * 2019年12月30日 下午12:01:55
 *
 * descriptions:设备关联gis模型下拉列表
 * 
 *
 */
@Getter
@Setter
public class GisVo {

	
	private String id;
	//名称
	@ApiModelProperty(value="设备名称")
	private String name;
	//类型
	@ApiModelProperty(value="设备类型")
	private String type;
	
}
