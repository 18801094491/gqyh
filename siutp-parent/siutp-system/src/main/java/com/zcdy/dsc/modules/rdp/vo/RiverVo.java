package com.zcdy.dsc.modules.rdp.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Description: 地图-河流信息VO
 * @Author: 在信汇通
 * @Date:   2020-12-10
 * @Version: V1.0
 */
@Data
@ApiModel(value="RiverVo", description="地图-河流信息VO")
public class RiverVo {

	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	private String id;
	/**河流名字*/
	@Excel(name = "河流名字", width = 15)
    @ApiModelProperty(value = "河流名字")
	private String riverName;
	/**河流枝干等级*/
	@Excel(name = "河流枝干等级", width = 15)
    @ApiModelProperty(value = "河流枝干等级")
	private Double riverLevel;
	/**河流开始位置-经度*/
	@Excel(name = "河流开始位置-经度", width = 15)
    @ApiModelProperty(value = "河流开始位置-经度")
	private String riverStartLongitude;
	/**河流开始位置-纬度*/
	@Excel(name = "河流开始位置-纬度", width = 15)
    @ApiModelProperty(value = "河流开始位置-纬度")
	private String riverStartLatitude;
	/**河流结束位置-经度*/
	@Excel(name = "河流结束位置-经度", width = 15)
    @ApiModelProperty(value = "河流结束位置-经度")
	private String riverEndLongitude;
	/**河流结束位置-纬度*/
	@Excel(name = "河流结束位置-纬度", width = 15)
    @ApiModelProperty(value = "河流结束位置-纬度")
	private String riverEndLatitude;
	/**河流经纬度信息*/
	@Excel(name = "河流经纬度信息", width = 15)
    @ApiModelProperty(value = "河流经纬度信息")
	private Object riverAddressInfo;
	/**添加人员*/
	@Excel(name = "添加人员", width = 15)
    @ApiModelProperty(value = "添加人员")
	private String createBy;
	/**添加时间*/
	@Excel(name = "添加时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "添加时间")
	private java.util.Date createTime;
	/**修改人员*/
	@Excel(name = "修改人员", width = 15)
    @ApiModelProperty(value = "修改人员")
	private String updateBy;
	/**修改时间*/
	@Excel(name = "修改时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间")
	private java.util.Date updateTime;
	/**标识位:true 有效/false无效*/
	@Excel(name = "标识位:true 有效/false无效", width = 15)
    @ApiModelProperty(value = "标识位:true 有效/false无效")
	private String active;
	/**删除标识0-正常,1-已删除*/
	@Excel(name = "删除标识0-正常,1-已删除", width = 15)
    @ApiModelProperty(value = "删除标识0-正常,1-已删除")
	private Integer delFlag;
	/**地图绘制线的样式*/
	@Excel(name = "地图绘制线的样式", width = 15)
    @ApiModelProperty(value = "地图绘制线的样式")
	private String riverStyle;
	/**地图绘制线的宽度*/
	@Excel(name = "地图绘制线的宽度", width = 15)
    @ApiModelProperty(value = "地图绘制线的宽度")
	private Double riverWeight;
	/**地图绘制线的颜色*/
	@Excel(name = "地图绘制线的颜色", width = 15)
    @ApiModelProperty(value = "地图绘制线的颜色")
	private String riverColor;
	/**地图绘制线的不透明度*/
	@Excel(name = "地图绘制线的不透明度", width = 15)
    @ApiModelProperty(value = "地图绘制线的不透明度")
	private Double riverOpacity;
}
