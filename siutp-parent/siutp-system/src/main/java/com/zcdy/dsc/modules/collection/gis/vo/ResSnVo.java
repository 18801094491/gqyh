package com.zcdy.dsc.modules.collection.gis.vo;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

/**
 * 描述: 图片库编号列表
 * @author：  songguang.jiao
 * 创建时间： 2020年3月3日 上午10:44:46 
 * 版本号: V1.0
 */
@Getter
@Setter
@ApiModel(value="ResSnVo",description="图片库编号列表")
public class ResSnVo {

	//图片id
	private String id;
	
	//图片编号
	private String resSn;
	
	
}
