package com.zcdy.dsc.modules.operation.equipment.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 描述: 图片上传
 * @author：  songguang.jiao
 * 创建时间： 2020年2月24日 下午4:01:25 
 * 版本号: V1.0
 */
@Getter
@Setter
@ApiModel(value="EquipmentUpkeepAttachVo上传",description="图片上传")
public class EquipmentUpkeepAttachVo {
	/**缩略图*/
    @ApiModelProperty(value = "缩略图")
	private java.lang.String upkeepThumb;
	/**图片路径*/
    @ApiModelProperty(value = "图片路径")
	private java.lang.String upkeepImage;
}
