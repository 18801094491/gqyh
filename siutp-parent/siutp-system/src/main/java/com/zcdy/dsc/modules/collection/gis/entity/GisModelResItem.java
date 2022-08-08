package com.zcdy.dsc.modules.collection.gis.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * 描述: GIS模型库图片地址信息
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-02-26
 * 版本号: V1.0
 */
@Data
@TableName("gis_model_res_item")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="gis_model_res_item", description="GIS模型库图片地址信息")
public class GisModelResItem {
    
	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**模型类别*/
	@Excel(name = "模型类别", width = 15)
    @ApiModelProperty(value = "模型类别")
	private java.lang.String resId;
	/**图片路径地址*/
	@Excel(name = "图片路径地址", width = 15)
    @ApiModelProperty(value = "图片路径地址")
	private java.lang.String imgUrl;
	/**图片类型*/
	@Excel(name = "图片类型", width = 15)
    @ApiModelProperty(value = "图片类型")
	private java.lang.String imageType;
	/**图片宽度*/
	@Excel(name = "图片宽度", width = 15)
    @ApiModelProperty(value = "图片宽度")
	private java.lang.Integer width;
	/**图片高度*/
	@Excel(name = "图片高度", width = 15)
    @ApiModelProperty(value = "图片高度")
	private java.lang.Integer height;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
    @ApiModelProperty(value = "创建人")
	private java.lang.String createBy;
	/**创建时间*/
	@Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
	private java.util.Date createTime;
	/**修改人*/
	@Excel(name = "修改人", width = 15)
    @ApiModelProperty(value = "修改人")
	private java.lang.String updateBy;
	/**修改时间*/
	@Excel(name = "修改时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间")
	private java.util.Date updateTime;
	/**删除标识0-正常,1-已删除*/
	@Excel(name = "删除标识0-正常,1-已删除", width = 15)
    @ApiModelProperty(value = "删除标识0-正常,1-已删除")
	private java.lang.Integer delFlag;
}
