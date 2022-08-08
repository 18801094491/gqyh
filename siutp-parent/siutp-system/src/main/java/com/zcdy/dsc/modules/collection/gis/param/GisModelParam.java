package com.zcdy.dsc.modules.collection.gis.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 *  GIS模型新增入参
 * @author songguang.jiao
 * 2019年12月24日 下午3:47:22
 *
 * descriptions:
 *
 */
@Getter
@Setter
@ApiModel(value="GisModelParam对象", description="GIS模型新增入参")
public class GisModelParam {
    /**id*/
    @ApiModelProperty(value = "id")
    private java.lang.String id;
	//模型名称
    @ApiModelProperty(value = "模型名称")
	private java.lang.String modelName;
	//模型类型
    @ApiModelProperty(value = "模型类型")
	private java.lang.String modelType;
    //正常模型图标地址
    @ApiModelProperty(value = "正常模型图标地址")
	private java.lang.String modelOnImg;
    //模型关闭图标地址
    @ApiModelProperty(value = "模型关闭图标地址")
    private java.lang.String modelOffImg;
	//数据异常使用的图片地址
    @ApiModelProperty(value = "数据异常使用的图片地址")
	private java.lang.String modelWaringImg;
	//模型偏移量
    @ApiModelProperty(value = "模型偏移量")
	private java.lang.String modelOffset;
	//模型显示位置,1-上，2-右，3-下，4-左
    @ApiModelProperty(value = "1-上，2-右，3-下，4-左")
	private java.lang.String modelPosition;
	//经度
    @ApiModelProperty(value = "经度")
	private java.lang.String longitude;
	//纬度
    @ApiModelProperty(value = "纬度")
	private java.lang.String latitude;
    //层级
    @ApiModelProperty(value = "层级")
  	private Integer modelLevel;
	
}
