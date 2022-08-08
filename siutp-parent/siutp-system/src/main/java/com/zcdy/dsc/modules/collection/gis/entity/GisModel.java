package com.zcdy.dsc.modules.collection.gis.entity;

import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 描述: GIS模型
 * @author： 智能无人硬核心项目组
 * 创建时间：   2019-12-23
 * 版本号: V1.0
 */
@Data
@TableName("gis_model")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@ApiModel(value="gis_model对象", description="GIS模型")
public class GisModel {
    
	/**id*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**模型名称*/
	@Excel(name = "模型名称", width = 15)
    @ApiModelProperty(value = "模型名称")
	private java.lang.String modelName;
	/**模型类型，参考字典表*/
    @ApiModelProperty(value = "模型类型Code")
	private java.lang.String modelTypeCode;
	/**正常开启图片地址*/
	@Excel(name = "开启状态图片", width = 15)
    @ApiModelProperty(value = "开启状态图片")
	private java.lang.String modelOnImg;
	/**关闭图片地址*/
	@Excel(name = "关闭状态图片", width = 15)
	@ApiModelProperty(value = "关闭状态图片")
	private java.lang.String modelOffImg;
	/**正常状态地址*/
	@Excel(name = "正常状态图片", width = 15)
	@ApiModelProperty(value = "正常状态图片")
	private java.lang.String modelImg;
	/**数据异常使用的图片地址*/
	@Excel(name = "数据异常使用的图片地址", width = 15)
	@ApiModelProperty(value = "数据异常使用的图片地址")
	private java.lang.String modelWaringImg;
	/**高度*/
	@Excel(name = "高度", width = 15)
	@ApiModelProperty(value = "高度")
	private Integer height;
	/**宽度*/
	@Excel(name = "宽度", width = 15)
	@ApiModelProperty(value = "宽度")
	private Integer width;
	/**模型偏移量*/
	@Excel(name = "模型偏移量", width = 15)
    @ApiModelProperty(value = "模型偏移量")
	private java.lang.String modelOffset;
	/**模型显示位置,，0-不偏移，1-上，2-右，3-下，4-左*/
	@Excel(name = "模型显示位置", width = 15)
    @ApiModelProperty(value = "模型显示位置,，0-不偏移，1-上，2-右，3-下，4-左")
	private java.lang.String modelPosition;
	//层级
	@Excel(name = "模型显示层级", width = 16)
    @ApiModelProperty(value = "层级")
	private Integer modelLevel;
	/**经度*/
	@Excel(name = "经度", width = 15)
    @ApiModelProperty(value = "经度")
	private java.lang.String longitude;
	/**纬度*/
	@Excel(name = "纬度", width = 15)
    @ApiModelProperty(value = "纬度")
	private java.lang.String latitude;
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
