package com.zcdy.dsc.common.system.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 描述: 图片信息,包含宽度跟高度
 * @author：  songguang.jiao
 * 创建时间： 2020年2月27日 上午11:15:34 
 * 版本号: V1.0
 */
@Getter
@Setter
@ApiModel(value="UploadImgResult",description="图片信息,包含宽度跟高度")
public class UploadImgResult {

	//名称
	@ApiModelProperty(value="名称")
	private String fileName;
	
	//路径
	@ApiModelProperty(value="路径")
	private String filePath;
	
	//缩略图
	@ApiModelProperty(value="缩略图")
	private String fileThumbPath;
	
	//宽度
	@ApiModelProperty(value="宽度")
	private String width;
	
	//高度
	@ApiModelProperty(value="高度")
	private String height;
	
}
