package com.zcdy.dsc.modules.collection.gis.entity;

import org.jeecgframework.poi.excel.annotation.Excel;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 描述: GIS模型类型对应导航配置
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-03-27
 * 版本号: V1.0
 */
@Data
@TableName("gis_model_nav")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="GisModelNav", description="GIS模型类型对应导航配置")
public class GisModelNav {
    
	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**模型类型*/
	@Excel(name = "模型类型", width = 15)
    @ApiModelProperty(value = "模型类型")
	private java.lang.String modelType;
	/**模型导航图片*/
	@Excel(name = "模型导航图片", width = 15)
    @ApiModelProperty(value = "模型导航图片")
	private java.lang.String modelThumb;
	/**导航名称*/
	@Excel(name = "导航名称", width = 15)
    @ApiModelProperty(value = "导航名称")
	private java.lang.String navName;
	/**默认是否展示数据*/
	@Excel(name = "默认是否展示数据", width = 15)
    @ApiModelProperty(value = "默认是否展示数据")
	private java.lang.String dataShow;
	/**启停用状态1-启用，0-停用*/
	@Excel(name = "启停用状态1-启用，0-停用", width = 15)
    @ApiModelProperty(value = "启停用状态1-启用，0-停用")
	private java.lang.String navStatus;
}
