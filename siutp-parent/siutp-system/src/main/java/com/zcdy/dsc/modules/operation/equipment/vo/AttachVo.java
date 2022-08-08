package com.zcdy.dsc.modules.operation.equipment.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 描述: 维保记录 详情 维保记录上传的图片信息
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-02-17
 * 版本号: V1.0
 */
@Data
@ApiModel(value="AttachVo", description="图片信息")
public class AttachVo {
	//图片路径
	@ApiModelProperty(value="图片路径")
	private String upkeepImage;
	//图片路径
	@ApiModelProperty(value="缩略图图片路径")
	private String upkeepThumb;
}
