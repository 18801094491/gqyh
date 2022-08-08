package com.zcdy.dsc.modules.collection.gis.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 描述: GisModelNavVo类
 * @author：  songguang.jiao
 * 创建时间：  2020年4月7日 上午10:51:32
 * 版本号: V1.0
 */
@Getter
@Setter
@ApiModel(value="GisModelNavVo",description="GisModelNavVo类")
public class GisModelNavVo {

	/**id*/
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**模型类型*/
    @ApiModelProperty(value = "模型类型")
	private java.lang.String modelType;
    /**模型类型*/
    @ApiModelProperty(value = "模型类型")
	private java.lang.String modelTypeName;
	/**模型导航图片*/
    @ApiModelProperty(value = "模型导航图片")
	private java.lang.String modelThumb;
	/**导航名称*/
    @ApiModelProperty(value = "导航名称")
	private java.lang.String navName;
	/**默认是否展示数据1-是,0-否*/
    @ApiModelProperty(value = "默认是否展示数据1-是,0-否")
	private java.lang.String dataShow;
    /**默认是否展示数据*/
    @ApiModelProperty(value = "默认是否展示数据")
	private java.lang.String dataShowName;
	/**启停用状态1-启用，0-停用*/
    @ApiModelProperty(value = "启停用状态1-启用，0-停用")
	private java.lang.String navStatus;
    /**启停用状态*/
    @ApiModelProperty(value = "启停用状态")
	private java.lang.String navStatusName;
}
