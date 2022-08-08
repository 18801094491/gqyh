package com.zcdy.dsc.modules.collection.gis.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 
 *descriptions:gis地图显示位置跟标段信息
 *
 *@author songguang.jiao
 * 2020年1月2日 下午2:28:52
 *
 */
@Setter
@Getter
@ApiModel(value="GisLocationVo",description="gis地图显示位置跟标段信息")
public class GisLocationVo {

	//所属标段
    @ApiModelProperty(value = "所属标段")
	private java.lang.String equipmentSection;
	//所在位置
    @ApiModelProperty(value = "所在位置")
	private java.lang.String equipmentLocation;
}
