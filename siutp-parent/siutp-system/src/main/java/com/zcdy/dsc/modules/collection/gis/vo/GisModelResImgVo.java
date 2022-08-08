package com.zcdy.dsc.modules.collection.gis.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 描述: gis模型图片地址
 * @author：  songguang.jiao
 * 创建时间： 2020年2月27日 上午10:19:51 
 * 版本号: V1.0
 */
@Getter
@Setter
@ApiModel(value="GisModelResImgVo",description=" gis模型图片地址")
public class GisModelResImgVo {

	/**图片路径地址*/
    @ApiModelProperty(value = "图片路径地址")
	private java.lang.String imgUrl;
	/**图片类型*/
    @ApiModelProperty(value = "图片类型")
	private java.lang.String imageType;
	/**图片宽度*/
    @ApiModelProperty(value = "图片宽度")
	private java.lang.Integer width;
	/**图片高度*/
    @ApiModelProperty(value = "图片高度")
	private java.lang.Integer height;
}
