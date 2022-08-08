package com.zcdy.dsc.modules.map.entity;

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
 * @Description: 地图-河流信息表
 * @Author: 在信汇通
 * @Date:   2020-12-10
 * @Version: V1.0
 */
@Data
@TableName("map_river_information")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="map_river_information", description="地图-河流信息表")
public class MapRiverInformation {
    
	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**河流名字*/
	@Excel(name = "河流名字", width = 15)
    @ApiModelProperty(value = "河流名字")
	private java.lang.String riverName;
	/**河流枝干等级*/
	@Excel(name = "河流枝干等级", width = 15)
    @ApiModelProperty(value = "河流枝干等级")
	private java.lang.Double riverLevel;
	/**河流开始位置-经度*/
	@Excel(name = "河流开始位置-经度", width = 15)
    @ApiModelProperty(value = "河流开始位置-经度")
	private java.lang.String riverStartLongitude;
	/**河流开始位置-纬度*/
	@Excel(name = "河流开始位置-纬度", width = 15)
    @ApiModelProperty(value = "河流开始位置-纬度")
	private java.lang.String riverStartLatitude;
	/**河流结束位置-经度*/
	@Excel(name = "河流结束位置-经度", width = 15)
    @ApiModelProperty(value = "河流结束位置-经度")
	private java.lang.String riverEndLongitude;
	/**河流结束位置-纬度*/
	@Excel(name = "河流结束位置-纬度", width = 15)
    @ApiModelProperty(value = "河流结束位置-纬度")
	private java.lang.String riverEndLatitude;
	/**河流经纬度信息*/
	@Excel(name = "河流经纬度信息", width = 15)
    @ApiModelProperty(value = "河流经纬度信息")
	private java.lang.Object riverAddressInfo;
	/**添加人员*/
	@Excel(name = "添加人员", width = 15)
    @ApiModelProperty(value = "添加人员")
	private java.lang.String createBy;
	/**添加时间*/
	@Excel(name = "添加时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "添加时间")
	private java.util.Date createTime;
	/**修改人员*/
	@Excel(name = "修改人员", width = 15)
    @ApiModelProperty(value = "修改人员")
	private java.lang.String updateBy;
	/**修改时间*/
	@Excel(name = "修改时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间")
	private java.util.Date updateTime;
	/**标识位:true 有效/false无效*/
	@Excel(name = "标识位:true 有效/false无效", width = 15)
    @ApiModelProperty(value = "标识位:true 有效/false无效")
	private java.lang.String active;
	/**删除标识0-正常,1-已删除*/
	@Excel(name = "删除标识0-正常,1-已删除", width = 15)
    @ApiModelProperty(value = "删除标识0-正常,1-已删除")
	private java.lang.Integer delFlag;
	/**地图绘制线的样式*/
	@Excel(name = "地图绘制线的样式", width = 15)
    @ApiModelProperty(value = "地图绘制线的样式")
	private java.lang.String riverStyle;
	/**地图绘制线的宽度*/
	@Excel(name = "地图绘制线的宽度", width = 15)
    @ApiModelProperty(value = "地图绘制线的宽度")
	private java.lang.Double riverWeight;
	/**地图绘制线的颜色*/
	@Excel(name = "地图绘制线的颜色", width = 15)
    @ApiModelProperty(value = "地图绘制线的颜色")
	private java.lang.String riverColor;
	/**地图绘制线的不透明度*/
	@Excel(name = "地图绘制线的不透明度", width = 15)
    @ApiModelProperty(value = "地图绘制线的不透明度")
	private java.lang.Double riverOpacity;
}
