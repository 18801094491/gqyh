package com.zcdy.dsc.modules.collection.gis.vo;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 描述: gis模型绑定图片库
 * @author：  songguang.jiao
 * 创建时间： 2020年3月3日 上午11:29:55 
 * 版本号: V1.0
 */
@Getter
@Setter
@ApiModel(value="GisImageVo",description="gis模型绑定图片库")
public class GisImageVo {

	//更新类型1-单个更新,2-批量更新(默认为1)
	@ApiModelProperty("更新类型1-单个更新,2-批量更新")
	@NotNull(message="更新类型不能为空")
	@NotEmpty(message="更新类型不能为空")
    private String type;
    
	//GisId
	@ApiModelProperty("GisId")
	private String ids;
	
	//图片库id
	@NotNull(message="图片库id不能为空")
	@NotEmpty(message="图片库id不能为空")
	@ApiModelProperty("图片库id")
	private String resId;
	
	//宽度
	@ApiModelProperty("宽度")
	private Integer width;
	
	//高度
	@ApiModelProperty("高度")
	private Integer height;
}
