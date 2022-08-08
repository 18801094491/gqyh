package com.zcdy.dsc.modules.collection.gis.param;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 设备绑定gis模型入参
 * @author songguang.jiao
 * 2019年12月30日 下午5:13:17
 *
 * descriptions:
 *
 */
@Setter
@Getter
public class GisEquipmentParam {

	//设备ID
	@ApiModelProperty(value="设备ID")
	@NotNull(message="设备ID不能为空")
	@NotBlank(message = "设备ID不能为空")
	private String equipmentId;
	
	//Gis模型Id
	@ApiModelProperty(value="gis模型ID")
	@NotNull(message="gis模型ID不能为空")
	@NotBlank(message = "gis模型ID不能为空")
	private String modelId;
}
