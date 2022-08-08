package com.zcdy.dsc.modules.operation.equipment.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 描述: 资产设备
 * @author：  songguang.jiao
 * 创建时间： 2020年2月24日 下午4:39:02 
 * 版本号: V1.0
 */
@Getter
@Setter
@ApiModel(value="EquipmentDropdown",description="资产设备下拉选")
public class EquipmentDropdown {

	//设备id
	@ApiModelProperty(value="设备id")
	private String id;
	
	//设备名称
	@ApiModelProperty(value="设备编号")
	private String equipmentSn;
	
}
