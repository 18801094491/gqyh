package com.zcdy.dsc.modules.operation.alarm.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 描述: 地图界面查询所有图标
 * @author：  songguang.jiao
 * 创建时间： 2020年3月9日 上午10:05:48 
 * 版本号: V1.0
 */
@Getter
@Setter
@ApiModel(value="IconListVo",description="地图界面查询所有图标")
public class IconListVo {

	//图标名称
	@ApiModelProperty(value="图标名称")
	private String name;
	
	//图标地址
	@ApiModelProperty(value="图标地址")
	private String url;
}
