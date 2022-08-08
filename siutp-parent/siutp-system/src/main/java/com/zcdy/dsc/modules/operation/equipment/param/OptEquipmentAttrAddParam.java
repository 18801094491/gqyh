package com.zcdy.dsc.modules.operation.equipment.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 设备资产附属属性
 * @author songguang.jiao
 * 2019年12月31日 上午10:04:06
 *
 * descriptions:
 *
 */
@Getter
@Setter
@ApiModel(value="OptEquipmentAttrParam",description="设备资产附属属性")
public class OptEquipmentAttrAddParam {

	//属性字段-英文
    @ApiModelProperty(value = "属性字段-英文")
	private java.lang.String attributeEn;
	//属性名称
    @ApiModelProperty(value = "属性名称")
	private java.lang.String attributeCn;
	//属性值
    @ApiModelProperty(value = "属性值")
	private java.lang.String attributeVal;
	
}
