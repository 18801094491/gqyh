package com.zcdy.dsc.modules.collection.gis.vo;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 描述: gis模型库入参
 * @author：  songguang.jiao
 * 创建时间： 2020年2月26日 上午10:18:37 
 * 版本号: V1.0
 */
@Getter
@Setter
@ApiModel(value="gis模型库入参",description="gis模型库入参")
public class GisModelResParamVo {

	/**id*/
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**模型类别*/
    @ApiModelProperty(value = "模型类别")
	private java.lang.String modelTypeCode;
    
    /**模型类别*/
    @ApiModelProperty(value = "图片列表")
    private List<GisModelResImgVo> list;
    
    
	
}
